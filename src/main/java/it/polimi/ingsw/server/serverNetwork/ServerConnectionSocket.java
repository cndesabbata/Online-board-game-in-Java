package it.polimi.ingsw.server.serverNetwork;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Class ServerConnectionSocket creates and handles the server socket, which
 * accepts connections from clients and creates threads that contain them.
 *
 */
public class ServerConnectionSocket implements Runnable {
    private final int port;
    private final Server server;
    private boolean active;
    private ServerSocket serverSocket;

    /**
     * Creates a ServerConnectionSocket instance.
     *
     * @param port   the port the server is listening on
     * @param server the main server object
     */
    public ServerConnectionSocket(int port, Server server) {
        this.port = port;
        this.server = server;
        active = true;
    }

    /**
     * Accepts new client connections and creates threads that contain them.
     *
     * @param serverSocket the server socket accepting the connections
     */
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

    /**
     * Sets the active attribute of this ServerConnectionSocket object.
     *
     * @param active the new value of the attribute
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Creates a new ServerSocket object and calls the {@link #initializeConnection(ServerSocket)} method.
     *
     * @see Runnable#run()
     */
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
