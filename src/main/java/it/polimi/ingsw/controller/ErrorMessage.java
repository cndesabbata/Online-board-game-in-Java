package it.polimi.ingsw.controller;

public class ErrorMessage implements ServerMessage {
    private String error;

    public ErrorMessage(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
