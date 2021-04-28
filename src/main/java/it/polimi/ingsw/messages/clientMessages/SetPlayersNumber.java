package it.polimi.ingsw.messages.clientMessages;

import java.io.Serializable;

public class SetPlayersNumber implements Serializable {
    private final int numOfPlayers;

    public SetPlayersNumber(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }
}
