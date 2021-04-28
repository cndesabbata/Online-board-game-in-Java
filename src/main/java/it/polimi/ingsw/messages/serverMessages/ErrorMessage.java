package it.polimi.ingsw.messages.serverMessages;

import it.polimi.ingsw.messages.Message;

public class ErrorMessage implements Message {
    private String error;

    public ErrorMessage(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
