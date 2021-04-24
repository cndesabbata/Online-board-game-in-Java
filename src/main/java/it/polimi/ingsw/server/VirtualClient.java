package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.ClientMessage;
import it.polimi.ingsw.controller.ServerMessage;
import it.polimi.ingsw.model.Game;
//import jdk.internal.event.Event;

public class VirtualClient {
    private String nickname;
    private Integer clientID;
    private ClientConnection clientConnection;

    public VirtualClient(String nickname, Integer clientID, ClientConnection clientConnection) {
        this.nickname = nickname;
        this.clientID = clientID;
        this.clientConnection = clientConnection;
    }

    public void send(ServerMessage serverMessage){

    }

    public void sendAll(ServerMessage serverMessage){

    }

    public void win(ServerMessage serverMessage){

    }

 /* public void propertyChange (Event event){

    }*/
}
