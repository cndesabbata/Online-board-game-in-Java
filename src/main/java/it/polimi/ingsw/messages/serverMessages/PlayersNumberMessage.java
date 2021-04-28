package it.polimi.ingsw.messages.serverMessages;

import it.polimi.ingsw.messages.Message;

public class PlayersNumberMessage implements Message {
    private String message;

    public PlayersNumberMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}