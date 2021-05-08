package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.messages.Message;

public class ViewMessage implements Message {
    private final String message;

    public ViewMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
