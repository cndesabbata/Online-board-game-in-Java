package it.polimi.ingsw.controller;

public class CustomMessage implements ServerMessage{
    private String message;
    private int status;

    public CustomMessage(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
