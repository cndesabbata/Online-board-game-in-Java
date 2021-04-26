package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.messages.ClientMessage;
import it.polimi.ingsw.controller.messages.ServerMessage;
//import jdk.internal.event.Event;

public class VirtualClient {
    private String nickname;
    private ClientConnection clientConnection;

    public VirtualClient(String nickname, ClientConnection clientConnection) {
        this.nickname = nickname;
        this.clientConnection = clientConnection;
    }

    public void send(ServerMessage serverMessage){
        clientConnection.sendSocketMessage(serverMessage);
    }

    public void sendAllExcept(ServerMessage serverMessage, String nickname){
        for (ClientConnection c : clientConnection.getGameController().getActiveConnections()){
            if (!c.getPlayerNickname().equals(nickname)) c.sendSocketMessage(serverMessage);
        }
    }

    public void sendAll(ServerMessage serverMessage){
        for (ClientConnection c : clientConnection.getGameController().getActiveConnections()){
            c.sendSocketMessage(serverMessage);
        }
    }

    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public void win(ServerMessage serverMessage){

    }

 /* public void propertyChange (Event event){

    }*/
}
