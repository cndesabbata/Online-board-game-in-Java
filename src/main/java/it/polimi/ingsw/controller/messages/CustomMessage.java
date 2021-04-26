package it.polimi.ingsw.controller.messages;

public class CustomMessage implements ServerMessage {
    private String message;

    public CustomMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
