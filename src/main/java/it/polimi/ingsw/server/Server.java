package it.polimi.ingsw.server;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.controller.CustomMessage;
import it.polimi.ingsw.controller.ErrorMessage;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.ClientMessage;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private Map<VirtualClient, ClientConnection> clientToConnection;
    private Map<String, Integer> nameMapId;
    private ServerConnectionSocket serverConnectionSocket;
    private List<GameController> gameControllers;
    private int totalPlayers;
    private int nextClientID;
    private ArrayList<ClientConnection> waitingList;

    public Server() {
        this.clientToConnection = new HashMap<>();
        this.nameMapId = new HashMap<>();
        this.gameControllers = new ArrayList<>();
        this.serverConnectionSocket = new ServerConnectionSocket(Constants.getPort(), this);
        this.totalPlayers = -1;
        this.waitingList = new ArrayList<>();
        this.nextClientID = 1;
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

    public void removeClient(int clientID) {

    }

    public void unregisterClient(int clientID) {

    }

    /*public GameController findGameByID(int clientID){

    }*/

    public ServerConnectionSocket getSocketServer() {
        return serverConnectionSocket;
    }

    public void sendAll(ClientMessage clientMessage) {

    }

    public void singleSend(ClientMessage clientMessage, String nickname) {

    }

    public void sendAllExcept(ClientMessage clientMessage, String nickname) {

    }

    private void lobby(ClientConnection connection) {
        waitingList.add(connection);
        if (waitingList.size() == 1) {

        } else if (waitingList.size() == totalPlayers) {

        } else {

        }
    }

    public synchronized Integer registerClient(String nickname, ClientConnection connection) throws InterruptedException {
        Integer clientID;
        if (waitingList.isEmpty()) {
            gameControllers.add(0, new GameController(this));
        }
        if (nameMapId.keySet().stream().anyMatch(nickname::equalsIgnoreCase)) {
            ErrorMessage error = new ErrorMessage("This nickname is already in use, please choose another one");
            connection.sendSocketMessage(error);
            return null;
        }
        clientID = nextClientID;
        nextClientID++;
        gameControllers.get(0).setUpPlayer(nickname, clientID);
        VirtualClient virtualClient = new VirtualClient(nickname, clientID, connection);
        nameMapId.put(nickname, clientID);
        clientToConnection.put(virtualClient, connection);
        connection.sendSocketMessage(new CustomMessage("Connection was successfully set-up!" +
                " You are now connected.", 0));
        if (waitingList.size() > 0) {
            virtualClient.sendAll(new CustomMessage(nickname + " joined the game", 0));
        }
        lobby(connection);
        return clientID;
    }

    public static void main(String[] args) {
        System.out.println("Welcome to our server!");
        Scanner scanner = new Scanner(System.in);
        System.out.println(">Insert the port which the server will listen on.");
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
                if (port == -1) System.err.println("Numeric format requested, please try again:\n>");
                else System.err.println("Ports accepted started from 1024! Please try again.\n>");
            } else
                active = false;
        }
        Constants.setPort(port);
        System.err.println("Starting Server...\n");
        Server server = new Server();
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(server.serverConnectionSocket);
    }
}
