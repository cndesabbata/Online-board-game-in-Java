package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnectionSocket implements Runnable {
    private int port;
    private Server server;
    private boolean active;
    private ExecutorService executorService;
    private ServerSocket serverSocket;

    public ServerConnectionSocket(int port, Server server) {
        this.port = port;
        this.server = server;
        executorService = Executors.newCachedThreadPool();
        active = true;
    }

    public void initializeConnection(ServerSocket serverSocket){
        while (active) {
            try {
                ClientConnection clientConnection = new ClientConnection(serverSocket.accept(), server);
                executorService.submit(clientConnection);
            } catch (IOException e) {
                System.err.println("Error during connection initialization, quitting...");
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
            System.err.println("Error during Socket initialization, quitting...");
            System.exit(0);
        }
    }
}
