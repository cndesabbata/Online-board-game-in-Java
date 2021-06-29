package it.polimi.ingsw.server.serverNetwork;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.serverMessages.newElement.ChangeMessage;
import it.polimi.ingsw.messages.serverMessages.ChangesDone;
import it.polimi.ingsw.messages.serverMessages.TurnChange;
import it.polimi.ingsw.server.observer.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Class VirtualView represents the virtual view of the player. It is an observer
 * of all the view elements and sends messages to the client.
 *
 */
public class VirtualView implements Observer {
    private final String nickname;
    private ClientConnection clientConnection;
    private final List<ChangeMessage> newElements;                                                                            //List of new parts of the model, which will be send when an ActionDone message is received.

    /**
     * Creates a new VirtualView object.
     * @param nickname         the nickname of the player
     * @param clientConnection the {@link ClientConnection} object associated with the player
     */
    public VirtualView(String nickname, ClientConnection clientConnection) {
        this.nickname = nickname;
        this.clientConnection = clientConnection;
        newElements = new ArrayList<>();
    }

    /**
     * Sends a message to the client through the socket.
     *
     * @param message the message to send
     */
    public void send(Message message){
        clientConnection.sendSocketMessage(message);
    }

    /**
     * Sends a message to all the players in the game, except one.
     *
     * @param message  the message to send
     * @param nickname the nickname of the player that will not receive the message
     */
    public void sendAllExcept(Message message, String nickname){
        for (ClientConnection c : clientConnection.getGameController().getActiveConnections()){
            if (!c.getPlayerNickname().equals(nickname)) c.sendSocketMessage(message);
        }
    }

    /**
     * Returns the nickname of the player associated to this VirtualView object.
     *
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sends a message to all the players in the game.
     *
     * @param message  the message to send
     */
    public void sendAll(Message message){
        for (ClientConnection c : clientConnection.getGameController().getActiveConnections()){
            c.sendSocketMessage(message);
        }
    }

    /**
     * Sets the ClientConnection object associated with this virtual view.
     *
     * @param clientConnection the ClientConnection object
     */
    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public void win(Message message){

    }

    /**
     * Called when the virtual view is notified. If the message is a {@link ChangeMessage},
     * it adds it to the list of new elements. If the message is a {@link ChangesDone} message,
     * it passes it the list of changed elements, sends the message to the client and clears the
     * list of new elements. If the message is a {@link TurnChange} one, it sends it to the client.
     *
     * @param message the notification message
     */
    @Override
    public void update(Message message) {
        if (message instanceof ChangesDone){
            ((ChangesDone) message).setNewElements(newElements);
            clientConnection.sendSocketMessage(message);
            newElements.clear();
        }
        else if (message instanceof TurnChange)
            clientConnection.sendSocketMessage(message);
        else {
            newElements.add((ChangeMessage) message);                                                                   //add new parts of the model to the list.
        }
    }

}
