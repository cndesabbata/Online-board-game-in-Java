package it.polimi.ingsw.server.serverNetwork;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.serverMessages.newElement.ChangeMessage;
import it.polimi.ingsw.messages.serverMessages.ChangesDone;
import it.polimi.ingsw.messages.serverMessages.TurnChange;
import it.polimi.ingsw.server.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class VirtualView implements Observer {
    private String nickname;
    private ClientConnection clientConnection;
    private List<ChangeMessage> newElements;                                                                            //List of new parts of the model, which will be send when an ActionDone message is received.

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
        if (message instanceof ChangesDone){
            ((ChangesDone) message).setNewElements(newElements);                                                         //passes the list of new parts of the model to the ActionDone message.
            clientConnection.sendSocketMessage(message);                                                                //sends ActionDone message with the list of new parts of the model.
            newElements.clear();
        }
        else if (message instanceof TurnChange)
            clientConnection.sendSocketMessage(message);
        else {
            newElements.add((ChangeMessage) message);                                                                   //add new parts of the model to the list.
        }
    }

}
