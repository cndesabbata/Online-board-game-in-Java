package it.polimi.ingsw.messages.serverMessages;

import it.polimi.ingsw.messages.Message;

public class CustomMessage implements Message {
    private final String message;

    public CustomMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
