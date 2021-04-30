package it.polimi.ingsw.server.serverNetwork;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.newElement.ChangeMessage;
import it.polimi.ingsw.messages.serverMessages.ActionDone;
import it.polimi.ingsw.server.observer.Observer;

import java.util.ArrayList;
import java.util.List;
//import jdk.internal.event.Event;

public class VirtualView implements Observer {
    private String nickname;
    private ClientConnection clientConnection;
    private List<ChangeMessage> newElements;

    public VirtualView(String nickname, ClientConnection clientConnection) {
        this.nickname = nickname;
        this.clientConnection = clientConnection;
        newElements = new ArrayList<>();
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
        if (message instanceof ActionDone){
            ((ActionDone) message).setNewElements(newElements);
            clientConnection.sendSocketMessage(message);
            newElements.clear();
        }
        else {
            newElements.add((ChangeMessage) message);
        }
    }

}
