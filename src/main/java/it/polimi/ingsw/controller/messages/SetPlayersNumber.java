package it.polimi.ingsw.controller.messages;

public class SetPlayersNumber implements ClientMessage {
    private final int numOfPlayers;

    public SetPlayersNumber(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }
}
