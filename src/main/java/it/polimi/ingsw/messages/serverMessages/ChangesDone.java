package it.polimi.ingsw.messages.serverMessages;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.serverMessages.newElement.ChangeMessage;
import it.polimi.ingsw.server.controller.UserAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Class ChangesDone is a {@link Message} used to communicate that a player has
 * performed an action. It contains a list of {@link ChangeMessage} objects which
 * represent all the elements the action has changed that need to be sent to the
 * players to update their view.
 *
 */
public class ChangesDone implements Message {
    private final String nickname;
    private final UserAction type;
    private List<ChangeMessage> newElements;

    /**
     * Creates a new ChangesDone instance.
     *
     * @param nickname   the nickname of the player that has performed an action
     * @param userAction the type of action performed by the player
     */
    public ChangesDone(String nickname, UserAction userAction) {
        this.nickname = nickname;
        this.type = userAction;
        newElements = new ArrayList<>();
    }

    /**
     * Returns the list of changed elements.
     *
     * @return the list of changed elements
     */
    public List<ChangeMessage> getNewElements() {
        return newElements;
    }

    /**
     * Returns the nickname of the player that has performed the action.
     *
     * @return the nickname of the player that has performed the action
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Returns the type of action performed by the player.
     *
     * @return the type of action performed by the player
     */
    public UserAction getType() {
        return type;
    }

    /**
     * Sets the list of changed elements.
     *
     * @param newElements the new list of changed elements
     */
    public void setNewElements(List<ChangeMessage> newElements) {
        this.newElements = newElements;
    }
}
