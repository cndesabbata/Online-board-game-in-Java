package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.messages.Message;

/**
 * Class SetPlayersNumber is a {@link Message} sent from the player to server when he wants to
 * create a new lobby.
 *
 */
public class SetPlayersNumber implements Message {
    private final int numOfPlayers;

    /**
     * Creates a new SetPlayersNumber instance.
     *
     * @param numOfPlayers the lobby size chosen by the player
     */
    public SetPlayersNumber(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    /**
     * Returns the lobby size chosen by the player.
     *
     * @return the lobby size chosen by the player
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }
}
