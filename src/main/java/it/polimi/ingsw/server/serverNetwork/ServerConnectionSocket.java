package it.polimi.ingsw.server.serverNetwork;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnectionSocket implements Runnable {
    private int port;
    private Server server;
    private boolean active;
    private ServerSocket serverSocket;

    public ServerConnectionSocket(int port, Server server) {
        this.port = port;
        this.server = server;
        active = true;
    }

    public void initializeConnection(ServerSocket serverSocket){
        while (active) {
            try {
                ClientConnection clientConnection = new ClientConnection(serverSocket.accept(), server);
                Thread newThread = new Thread(clientConnection);
                newThread.start();
            } catch (IOException e) {
                System.out.println("Error during connection initialization, quitting...");
            }
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void run(){
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Socket Server started; listening on port " + port);
            System.out.println("Type \"QUIT\" to close server");
            initializeConnection(serverSocket);
        } catch (IOException e) {
            System.out.println("Error during Socket initialization, quitting...");
            System.exit(0);
        }
    }
}
