package it.polimi.ingsw.messages.clientMessages.internal;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Message;

/**
 * Abstract class ViewMessage represents a message sent by the {@link ClientView} object,
 * observed by both CLI and GUI, to instruct them on what is the next thing to do.
 *
 */
public abstract class ViewMessage implements Message {
    private final String message;

    public ViewMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
