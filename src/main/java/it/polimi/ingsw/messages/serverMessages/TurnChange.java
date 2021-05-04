package it.polimi.ingsw.messages.serverMessages;

import it.polimi.ingsw.messages.Message;

public class TurnChange implements Message {
    private final String newPlayer;
    private final String oldPlayer;

    public TurnChange(String newPlayer, String oldPlayer) {
        this.newPlayer = newPlayer;
        this.oldPlayer = oldPlayer;
    }

    public String getNewPlayer() {
        return newPlayer;
    }

    public String getOldPlayer() {
        return oldPlayer;
    }
}
