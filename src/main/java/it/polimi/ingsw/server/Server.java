package it.polimi.ingsw.server;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.controller.GameHandler;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private Map<VirtualClient, SocketClientConnection> clientToConnection;
    private SocketServer socketServer;
    private GameHandler gameHandler;
    private int totalPlayers;
    private ArrayList<SocketClientConnection> waitingList;

    public Server() {
        this.clientToConnection = new HashMap<>();
        this.socketServer = new SocketServer(Constants.getPort(), this);
        this.totalPlayers = -1;
        this.waitingList = new ArrayList<>();
    }

    /*
    public void sendAll (Message message){

    }

    public void singleSend (Message message, String nickname){

    }

    public void sendAllExcept (Message message, String nickname){

    }
    */
    public void lobby (SocketClientConnection firstConnection){

    }

    public void register(String nickname){

    }

    public void unRegister (String nickname){

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
                else System.err.println("Error: ports accepted started from 1024! Please tyr again.\n>");
            }
        }
        Constants.setPort(port);
        System.err.println("Starting Socket Server...\n");
        Server server = new Server();
        ExecutorService executor = Executors.newCachedThreadPool();
        System.out.println("Instantiating server class...\n");
        executor.submit(server.socketServer);
    }
}
