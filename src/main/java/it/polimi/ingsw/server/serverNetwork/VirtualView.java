package it.polimi.ingsw.server.serverNetwork;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.observer.Observer;
//import jdk.internal.event.Event;

public class VirtualView implements Observer {
    private String nickname;
    private ClientConnection clientConnection;

    public VirtualView(String nickname, ClientConnection clientConnection) {
        this.nickname = nickname;
        this.clientConnection = clientConnection;
    }

    public void send(Message message){
        clientConnection.sendSocketMessage(message);
    }

    public void sendAllExcept(Message message, String nickname){
        for (ClientConnection c : clientConnection.getGameController().getActiveConnections()){
            if (!c.getPlayerNickname().equals(nickname)) c.sendSocketMessage(message);
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void sendAll(Message message){
        for (ClientConnection c : clientConnection.getGameController().getActiveConnections()){
            c.sendSocketMessage(message);
        }
    }

    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public void win(Message message){

    }

    @Override
    public void update(Message message) {

    }

 /* public void propertyChange (Event event){

    }*/
}
