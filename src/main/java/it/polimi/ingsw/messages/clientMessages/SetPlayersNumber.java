package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.messages.Message;

public class SetPlayersNumber implements Message {
    private final int numOfPlayers;

    public SetPlayersNumber(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }
}
