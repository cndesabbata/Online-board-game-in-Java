package it.polimi.ingsw.server.serverNetwork;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.controller.multiplayer.MultiPlayerController;
import it.polimi.ingsw.server.controller.singleplayer.SinglePlayerController;
import it.polimi.ingsw.messages.serverMessages.CustomMessage;
import it.polimi.ingsw.messages.serverMessages.ErrorMessage;
import it.polimi.ingsw.messages.serverMessages.PlayersNumberMessage;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Server {
    private final Map<VirtualView, ClientConnection> clientToConnection;
    private final ServerConnectionSocket serverConnectionSocket;
    private final List<GameController> gameControllers;
    private int totalPlayers;
    private final ArrayList<ClientConnection> waitingList;

    public Server() {
        this.clientToConnection = new HashMap<>();
        this.gameControllers = new ArrayList<>();
        this.serverConnectionSocket = new ServerConnectionSocket(Constants.getPort(), this);
        this.totalPlayers = -1;
        this.waitingList = new ArrayList<>();
        Thread thread = new Thread(this::serverQuitter);
        thread.start();
    }

    public void serverQuitter() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.next().equalsIgnoreCase("QUIT")) {
                getSocketServer().setActive(false);
                System.exit(0);
                break;
            }
        }
    }

    public void removeClient(ClientConnection connection){
        connection.getGameController().getGame().getPlayers()
                .removeIf(p -> p.getNickname().equals(connection.getPlayerNickname()));
        waitingList.removeIf(c -> c == connection);
        unregisterClient(connection);
    }

    public void unregisterClient(ClientConnection connection){
        connection.getGameController().getActiveConnections().removeIf(c -> c == connection);
        connection.getGameController().getActivePlayers().
                removeIf(p -> p.getNickname().equals(connection.getPlayerNickname()));
        find(connection, clientToConnection).setClientConnection(null);
        clientToConnection.remove(connection);
    }

    public ServerConnectionSocket getSocketServer() {
        return serverConnectionSocket;
    }

    public void setTotalPlayers(int totalPlayers, ClientConnection connection) {
        this.totalPlayers = totalPlayers;
        if (totalPlayers == 1){
            gameControllers.add(0, new SinglePlayerController(this));
            gameControllers.get(0).setUpPlayer(connection);
            // gameControllers.get(0).newObserver(VirtualView ...);
            connection.setGameController(gameControllers.get(0));
            waitingList.clear();
            gameControllers.get(0).setup();
        }
        else {
            gameControllers.add(0, new MultiPlayerController(this));
            gameControllers.get(0).setUpPlayer(connection);
            // gameControllers.get(0).newObserver(VirtualView ...);
            connection.setGameController(gameControllers.get(0));
        }
    }

    private void lobby (ClientConnection connection){
        waitingList.add(connection);
        if (waitingList.size() == 1) {
            connection.sendSocketMessage(new PlayersNumberMessage(connection.getPlayerNickname() +
                    ", you are the lobby host, please choose the number of players: [1...4]"));
        } else if (waitingList.size() == totalPlayers) {
            VirtualView v = find(connection, clientToConnection);
            v.sendAll(new CustomMessage("Player number reached. The match is starting."));
            waitingList.clear();
            gameControllers.get(0).setup();
            // gameControllers.get(0).newObserver(VirtualView ...);
        } else {
            VirtualView v = find(connection, clientToConnection);
            v.sendAll(new CustomMessage((totalPlayers - waitingList.size()) + " slots left."));
            // gameControllers.get(0).newObserver(VirtualView ...);
        }
    }

    private VirtualView find(ClientConnection connection, Map<VirtualView, ClientConnection> map){
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), connection))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()).get(0);
    }

    /*public GameController getControllerByNickname(String nickname){
        for (GameController gc : gameControllers){
            if (gc.getGame().getPlayers().stream().anyMatch(p -> p.getNickname().equals(nickname)))
                return gc;
        }
        return null;
    }*/

    public synchronized void reconnectClient(String nickname, ClientConnection connection) throws InterruptedException{
        for (GameController gc : gameControllers){
            if (gc.getGame().getPlayers().stream().anyMatch(p -> p.getNickname().equals(nickname)) &&
                    gc.getActivePlayers().stream().noneMatch(p -> p.getNickname().equals(nickname))){
                gc.addActivePlayer(gc.getGame().getPlayerByNickname(nickname));
                gc.addActiveConnection(connection);
                connection.setGameController(gc);
                VirtualView virtualView = new VirtualView(nickname, connection);
                clientToConnection.put(virtualView, connection);
                connection.sendSocketMessage(new CustomMessage("Connection was successfully set-up!" +
                        " You are now reconnected."));
                return;
            }
        }
        ErrorMessage error = new ErrorMessage("There is no active match with an inactive player with such nickname");
        connection.sendSocketMessage(error);
    }

    public synchronized void registerClient(String nickname, ClientConnection connection) throws InterruptedException{
        for (GameController gc : gameControllers){
            if (gc.getGame().getPlayers().stream().anyMatch(p -> p.getNickname().equals(nickname))){
                ErrorMessage error = new ErrorMessage("This nickname is already in use, please choose another one");
                connection.sendSocketMessage(error);
            }
        }
        VirtualView virtualView = new VirtualView(nickname, connection);
        clientToConnection.put(virtualView, connection);
        connection.sendSocketMessage(new CustomMessage("Connection was successfully set-up!" +
                " You are now connected."));
        if (waitingList.size() > 0) {
            virtualView.sendAll(new CustomMessage(nickname + " joined the game"));
        }
        lobby(connection);
    }

    public static void main(String[] args){
        System.out.println("Welcome to our server!");
        Scanner scanner = new Scanner(System.in);
        System.out.println(">Insert the port which the server will listen on.");
        System.out.print(">");
        int port = 0;
        boolean active = true;
        while (active){
            try {
                port = scanner.nextInt();
            } catch (InputMismatchException e) {
                port = -1;
            }
            if (port <= 1024){
                if (port == -1) System.err.println("Numeric format requested, please try again:\n>");
                else {
                    System.err.println("Ports accepted started from 1024! Please try again.\n>");
                    active = false;
                }
            }
        }
        Constants.setPort(port);
        System.err.println("Starting Server...\n");
        Server server = new Server();
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(server.serverConnectionSocket);
    }
}
