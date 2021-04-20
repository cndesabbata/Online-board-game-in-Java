package it.polimi.ingsw.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClientConnection {
    private Server server;
    private Socket socket;
    private boolean active;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String playerNickname;

    public SocketClientConnection(Server server, Socket socket, boolean active, ObjectInputStream input,
                                  ObjectOutputStream output, String playerNickname) {
        this.server = server;
        this.socket = socket;
        this.active = active;
        this.input = input;
        this.output = output;
        this.playerNickname = playerNickname;
    }

    public void getFromStream(){

    }

    public void close(){

    }

    public void run(){

    }

    public void actionHandler(Message message){

    }

    public void checkConnection(){

    }

    public void sendSocketMessage(){

    }

}
