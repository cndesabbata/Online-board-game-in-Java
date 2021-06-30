package it.polimi.ingsw.server.serverNetwork;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.messages.serverMessages.*;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.controller.multiplayer.MultiPlayerController;
import it.polimi.ingsw.server.controller.singleplayer.SinglePlayerController;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.observer.Observer;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class Server is the main server class. It registers, unregisters, removes and reconnects
 * clients, it manages the lobbies and starts the games.
 *
 */
public class Server {
    private final Map<VirtualView, ClientConnection> clientToConnection;
    private final ServerConnectionSocket serverConnectionSocket;
    private final List<GameController> gameControllers;
    private final ArrayList<String> totalNicknames;                                                                     //used to check the availability of a nickname
    private final ArrayList<Lobby> lobbies;

    /**
     * Creates a new Server instance. It also creates a new thread that allows
     * to manually close the server.
     *
     */
    public Server() {
        this.clientToConnection = new HashMap<>();
        this.gameControllers = new ArrayList<>();
        this.serverConnectionSocket = new ServerConnectionSocket(Constants.getPort(), this);
        this.totalNicknames = new ArrayList<>();
        this.lobbies = new ArrayList<>();
        Thread thread = new Thread(this::serverQuitter);
        thread.start();
    }

    /**
     * Waits for user input and terminates the server if the word "quit" is received.
     *
     */
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

    /**
     * Returns the list of lobbies.
     *
     * @return the list of lobbies
     */
    public synchronized ArrayList<Lobby> getLobbies() {
        return lobbies;
    }

    /**
     * Removes a player from the game and removes his nickname from the list of nicknames. If
     * the player was the owner of a lobby not yet filled, it removes his lobby from the list
     * of lobbies and ask all the other players to select another one, otherwise it removes him
     * from the waiting list of the lobby. If the player was the only one left in a multiplayer
     * game, it calls the {@link #removeGame(GameController)} method. Once all of this is done,
     * it calls the {@link #unregisterClient(ClientConnection)} method.
     *
     * @param connection the ClientConnection object to remove
     */
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
                                "has disconnected, please select another one.", lobbies));
                }
                break;
            }
            else {
                for(int j = 1; j < l.getWaitingList().size(); j++){
                    ClientConnection c = l.getWaitingList().get(j);
                    if(c == connection){
                        l.removeFromWaitingList(connection);
                        find(l.getWaitingList().get(0), clientToConnection).sendAllExcept
                                (new SetupMessage("Player " +connection.getPlayerNickname()+ " has disconnected"),
                                        connection.getPlayerNickname());
                        break;
                    }
                }
            }
        }
        GameController gc = connection.getGameController();
        if (gc instanceof MultiPlayerController && gc.getActiveConnections().size() == 1){
            for (Player p : gc.getGame().getPlayers()){
                totalNicknames.removeIf(n -> n.equalsIgnoreCase(p.getNickname()));
            }
            gameControllers.remove(gc);
        }
        unregisterClient(connection);
    }

    /**
     * Closes the socket in the ClientConnection object provided. Removes the connection from
     * the list of active connections and calls the {@link GameController#removeObserver(Observer)}
     * method on the associated virtual view. It also updates the index of the current player of the game
     * in case of multiplayer game. Once this is done it removes the mapping in the clientToConnection map.
     *
     * @param connection the ClientConnection object to unregister
     */
    public synchronized void unregisterClient(ClientConnection connection) {
        VirtualView view = find(connection, clientToConnection);
        String nickname = connection.getPlayerNickname();
        connection.close();
        if (connection.getGameController() != null) {
            GameController gc = connection.getGameController();
            view.sendAllExcept(new Disconnection("Player " + nickname + " disconnected.", nickname), nickname);
            gc.getActiveConnections().removeIf(c -> c == connection);
            gc.removeObserver(find(connection, clientToConnection));
            List<Player> unregistered = gc.getActivePlayers().stream().filter(p ->
                    p.getNickname().equalsIgnoreCase(connection.getPlayerNickname())).collect(Collectors.toList());
            if(gc instanceof MultiPlayerController) {                                                                   //currentPLayerIndex must be updated
                if (((MultiPlayerController) gc).getCurrentPlayerIndex() > gc.getActivePlayers().indexOf(unregistered.get(0)))
                    ((MultiPlayerController) gc).setCurrentPlayerIndex(((MultiPlayerController) gc).getCurrentPlayerIndex() - 1);
            }
            gc.getActivePlayers().removeIf(p -> p.getNickname().equals(nickname));
        }
        view.setClientConnection(null);
        clientToConnection.remove(view);
    }

    /**
     * Returns the ServerConnectionSocket object associated with this server.
     *
     * @return the associated ServerConnectionSocket object
     */
    public synchronized ServerConnectionSocket getSocketServer() {
        return serverConnectionSocket;
    }

    /**
     * Sets the number of total players for a game. If the number is 1, creates a single player
     * controller and starts the game, otherwise it creates a multiplayer controller and a lobby
     * others can join until the number of total players is reached.
     *
     * @param totalPlayers the number of total players
     * @param connection   the ClientConnection object of the player setting the number of total players
     */
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

    /**
     * Makes a player join a lobby and notifies all the other players in that lobby that a player has
     * joined. If the lobby is filled, notifies all players that the game will start, calls the
     * {@link GameController#addObserver(Observer)} method on their own virtual view and starts the game.
     *
     * @param connection the ClientConnection object associated with a player
     * @param lobbyHost  the nickname of the lobby host
     */
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

    /**
     * Returns the VirtualView object associated with a ClientConnection object.
     *
     * @param connection the ClientConnection object
     * @param map        the map that associates a connection with its virtual view
     * @return the VirtualView object found
     */
    private VirtualView find(ClientConnection connection, Map<VirtualView, ClientConnection> map) {
        if (map.isEmpty()) return null;
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), connection))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()).get(0);
    }

    /**
     * Reconnects a player to a game. Adds him to the list of active players and the list of active
     * connections in the game controller, creates a new virtual view and notifies all the other
     * players that he has reconnected. Adds the virtual view to the clientToConnection map and calls
     * the {@link GameController#addObserver(Observer)} on it. If no active game that contained the player
     * is found, it notifies him.
     *
     * @param nickname   the nickname of the player reconnecting
     * @param connection the ClientConnection object associated with the player
     * @throws InterruptedException if the thread is interrupted
     */
    public synchronized void reconnectClient(String nickname, ClientConnection connection) throws InterruptedException {
        for (GameController gc : gameControllers) {
            if (gc.getGame().getPlayers().stream().anyMatch(p -> p.getNickname().equalsIgnoreCase(nickname)) &&
                    gc.getActivePlayers().stream().noneMatch(p -> p.getNickname().equalsIgnoreCase(nickname))) {
                gc.addActivePlayer(gc.getGame().getPlayerByNickname(nickname));
                gc.addActiveConnection(connection);
                connection.setGameController(gc);
                VirtualView virtualView = new VirtualView(nickname, connection);
                virtualView.sendAllExcept(new Disconnection(nickname + " reconnected. ", nickname), nickname);
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

    /**
     * Register a player. If the nickname provided is already in use notifies the player and
     * returns, otherwise creates a new VirtualView object, adds it to the clientToConnection
     * map and sends the player all the available lobbies.
     *
     * @param nickname   the nickname of the player
     * @param connection the ClientConnection object associated with the player
     * @throws InterruptedException when the thread is interrupted
     */
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

    /**
     * Asks for the server port and creates a new Server instance, then it launches a new
     * thread that will execute the {@link ServerConnectionSocket#run()} method.
     *
     * @param args the main args
     */
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

    /**
     * Removes all clients associated with a game controller and removes the controller from
     * the list of game controllers. Used when a game has ended.
     *
     * @param gc the game controller
     */
    public void removeGame(GameController gc){
        for (ClientConnection c: gc.getActiveConnections()){
            removeClient(c);
        }
        gameControllers.remove(gc);
    }
}
