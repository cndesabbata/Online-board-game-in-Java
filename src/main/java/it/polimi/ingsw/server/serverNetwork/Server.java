package it.polimi.ingsw.server.serverNetwork;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.messages.serverMessages.*;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.controller.multiplayer.MultiPlayerController;
import it.polimi.ingsw.server.controller.singleplayer.SinglePlayerController;

import java.util.*;
import java.util.stream.Collectors;

public class Server {
    private final Map<VirtualView, ClientConnection> clientToConnection;
    private final ServerConnectionSocket serverConnectionSocket;
    private final List<GameController> gameControllers;
    private final ArrayList<String> totalNicknames;                                                                     //used to check the availability of a nickname
    private final ArrayList<Lobby> lobbies;

    public Server() {
        this.clientToConnection = new HashMap<>();
        this.gameControllers = new ArrayList<>();
        this.serverConnectionSocket = new ServerConnectionSocket(Constants.getPort(), this);
        this.totalNicknames = new ArrayList<>();
        this.lobbies = new ArrayList<>();
        Thread thread = new Thread(this::serverQuitter);
        thread.start();
    }

    public void serverQuitter() {                                                                                       //DO NOT SYNCHRONIZE
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.next().equalsIgnoreCase("QUIT")) {
                getSocketServer().setActive(false);
                System.exit(0);
                break;
            }
        }
    }

    public synchronized ArrayList<String> getTotalNicknames() {
        return totalNicknames;
    }

    public synchronized ArrayList<Lobby> getLobbies() {
        return lobbies;
    }

    public synchronized void removeClient(ClientConnection connection) {
        if (connection.getGameController() != null){
            connection.getGameController().getGame().getPlayers()
                    .removeIf(p -> p.getNickname().equals(connection.getPlayerNickname()));
        }
        totalNicknames.removeIf(n -> n.equalsIgnoreCase(connection.getPlayerNickname()));
        for(int i = 0; i < lobbies.size(); i++){
            Lobby l = lobbies.get(i);
            if(l.getOwner().equalsIgnoreCase(connection.getPlayerNickname())){
                lobbies.remove(l);
                for(ClientConnection c : l.getWaitingList()){
                    if(c != connection)
                        c.sendSocketMessage(new PlayersNumberMessage("The host of the selected lobby " +
                                "has disconnected, please select another one", lobbies));
                }
                break;
            }
            else {
                for(int j = 1; j < l.getWaitingList().size(); j++){
                    ClientConnection c = l.getWaitingList().get(j);
                    if(c == connection){
                        l.removeFromWaitingList(connection);
                        find(l.getWaitingList().get(0), clientToConnection).sendAllExcept
                                (new SetupMessage("PLayer " +connection.getPlayerNickname()+ " has disconnected"),
                                        connection.getPlayerNickname());
                        break;
                    }
                }
            }
        }
        unregisterClient(connection);
    }

    public synchronized void unregisterClient(ClientConnection connection) {
        VirtualView view = find(connection, clientToConnection);
        String nickname = connection.getPlayerNickname();
        connection.close();
        if (connection.getGameController() != null) {
            view.sendAllExcept(new Disconnection("Player " + nickname + " disconnected."), nickname);
            connection.getGameController().getActiveConnections().removeIf(c -> c == connection);
            connection.getGameController().removeObserver(find(connection, clientToConnection));
            connection.getGameController().getActivePlayers().removeIf(p -> p.getNickname().equals(nickname));
        }
        view.setClientConnection(null);
        clientToConnection.remove(view);
    }

    public synchronized ServerConnectionSocket getSocketServer() {
        return serverConnectionSocket;
    }

    public synchronized void setTotalPlayers(int totalPlayers, ClientConnection connection) {
        if (totalPlayers == 1) {
            gameControllers.add(0, new SinglePlayerController(this));
            gameControllers.get(0).setUpPlayer(connection);
            connection.setGameController(gameControllers.get(0));
            gameControllers.get(0).addObserver(find(connection, clientToConnection));
            gameControllers.get(0).setup();
        } else {
            gameControllers.add(0, new MultiPlayerController(this));
            Lobby newLobby = new Lobby(totalPlayers, connection, gameControllers.get(0));
            lobbies.add(newLobby);
            gameControllers.get(0).setUpPlayer(connection);
            connection.setGameController(gameControllers.get(0));
            connection.sendSocketMessage(new SetupMessage("Please wait for other players to join...\n"));
        }
    }

    public void join(ClientConnection connection, String lobbyHost){
        Lobby lobby = null;
        for(Lobby l : lobbies){
            if(l.getOwner().equalsIgnoreCase(lobbyHost)){
                l.addToWaitingList(connection);
                lobby = l;
                break;
            }
        }
        if(lobby != null ){
            find(lobby.getWaitingList().get(0), clientToConnection)
                    .sendAll(new SetupMessage(connection.getPlayerNickname().toUpperCase() + " joined the game."));
            lobby.getGameController().setUpPlayer(connection);
            connection.setGameController(lobby.getGameController());
            connection.sendSocketMessage(new SetupMessage("You have successfully joined the match."));
            if(lobby.getWaitingList().size() == lobby.getTotalPlayers()) {
                VirtualView v = find(connection, clientToConnection);
                v.sendAll(new SetupMessage("Player number reached. The match is starting."));
                for(ClientConnection c : lobby.getWaitingList()){
                    lobby.getGameController().addObserver(find(c,clientToConnection));
                }
                lobbies.remove(lobby);
                lobby.getGameController().setup();
            }
        }
    }

    private VirtualView find(ClientConnection connection, Map<VirtualView, ClientConnection> map) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), connection))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()).get(0);
    }

    public synchronized void reconnectClient(String nickname, ClientConnection connection) throws InterruptedException {
        for (GameController gc : gameControllers) {
            if (gc.getGame().getPlayers().stream().anyMatch(p -> p.getNickname().equalsIgnoreCase(nickname)) &&
                    gc.getActivePlayers().stream().noneMatch(p -> p.getNickname().equalsIgnoreCase(nickname))) {
                gc.addActivePlayer(gc.getGame().getPlayerByNickname(nickname));
                gc.addActiveConnection(connection);
                connection.setGameController(gc);
                VirtualView virtualView = new VirtualView(nickname, connection);
                clientToConnection.put(virtualView, connection);
                gc.addObserver(find(connection, clientToConnection));
                connection.sendSocketMessage(new SetupMessage("Connection was successfully set-up!" +
                        " You are now reconnected."));
                gc.sendReloadedView(nickname);
                return;
            }
        }
        ErrorMessage error = new ErrorMessage("There is no active match with an inactive player with such nickname", ErrorType.RECONNECTION);
        connection.sendSocketMessage(error);
    }

    public synchronized void registerClient(String nickname, ClientConnection connection) throws InterruptedException {
        for (String s : totalNicknames) {
            if (s.equalsIgnoreCase(nickname)) {
                ErrorMessage error = new ErrorMessage("This nickname is already in use, please choose another one.", ErrorType.DUPLICATE_NICKNAME);
                connection.sendSocketMessage(error);
                return;
            }
        }
        totalNicknames.add(nickname);
        VirtualView virtualView = new VirtualView(nickname, connection);
        clientToConnection.put(virtualView, connection);
        connection.sendSocketMessage(new SetupMessage("Connection was successfully set-up!" +
                " You are now connected."));
        if(!lobbies.isEmpty())
            connection.sendSocketMessage(new PlayersNumberMessage(connection.getPlayerNickname().toUpperCase() +
                    ", these are the available lobbies: ", lobbies));
        else
            connection.sendSocketMessage(new PlayersNumberMessage(connection.getPlayerNickname().toUpperCase() +
                    ", there is no lobby available.", lobbies));
    }

    public static void main(String[] args) {
        System.out.println("Welcome to our server!");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert the port which the server will listen on.");
        System.out.print(">");
        int port = 0;
        boolean active = true;
        while (active) {
            try {
                port = scanner.nextInt();
            } catch (InputMismatchException e) {
                port = -1;
            }
            if (port <= 1024) {
                if (port == -1) {
                    System.out.print("Numeric format requested, please try again:\n>");
                } else {
                    System.out.print("Ports accepted started from 1024! Please try again.\n>");
                }
                scanner.next();
            }
            else
                active = false;
        }
        Constants.setPort(port);
        System.out.println("Starting Server...\n");
        Server server = new Server();
        Thread thread = new Thread(server.serverConnectionSocket);
        thread.start();
    }
}
