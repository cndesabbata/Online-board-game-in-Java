package it.polimi.ingsw.server;

//import jdk.internal.event.Event;

public class VirtualClient {
    private String nickname;
    private SocketClientConnection socketClientConnection;

    public VirtualClient(String nickname, SocketClientConnection socketClientConnection) {
        this.nickname = nickname;
        this.socketClientConnection = socketClientConnection;
    }

    /*
    public void send(Message message){

    }

    public void win(Message message){

    }

    public void propertyChange (Event event){

    }
    */
}
