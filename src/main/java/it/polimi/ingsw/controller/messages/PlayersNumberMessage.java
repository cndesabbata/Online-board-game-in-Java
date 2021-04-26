package it.polimi.ingsw.controller.messages;

public class PlayersNumberMessage implements ServerMessage {
    private String message;

    public PlayersNumberMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}