package it.polimi.ingsw.server;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer implements Runnable {
    private int port;
    private Server server;
    private boolean active;
    private ExecutorService executorService;

    public SocketServer(int port, Server server) {
        this.port = port;
        this.server = server;
        executorService = Executors.newCachedThreadPool();
        active = true;
    }

    public void acceptConnection (ServerSocket serverSocket){

    }

    public void run(){

    }
}
