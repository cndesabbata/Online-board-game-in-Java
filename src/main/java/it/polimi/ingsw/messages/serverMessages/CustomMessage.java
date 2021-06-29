package it.polimi.ingsw.messages.serverMessages;

import it.polimi.ingsw.messages.Message;

/**
 * Abstract class Custom message represents a generic {@link Message} that needs
 * to be showed to the player.
 *
 */
public abstract class CustomMessage implements Message {
    private final String message;

    /**
     * Default constructor.
     *
     * @param message the message to show to the player
     */
    public CustomMessage(String message){
        this.message = message;
    }

    /**
     * Returns the message that needs to be showed to the player.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
