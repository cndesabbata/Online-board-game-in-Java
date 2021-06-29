package it.polimi.ingsw.messages.serverMessages;

import it.polimi.ingsw.messages.Message;

/**
 * Class TurnChange is a {@link Message} used to communicate a turn change.
 *
 */
public class TurnChange implements Message {
    private final String newPlayer;
    private final String oldPlayer;

    /**
     * Creates a TurnChange instance.
     *
     * @param newPlayer the nickname of the new current player
     * @param oldPlayer the nickname of the player that has ended his turn
     */
    public TurnChange(String newPlayer, String oldPlayer) {
        this.newPlayer = newPlayer;
        this.oldPlayer = oldPlayer;
    }

    /**
     * Returns the nickname of the new current player.
     *
     * @return the nickname of the new current player
     */
    public String getNewPlayer() {
        return newPlayer;
    }

    /**
     * Returns the nickname of the player that has ended his turn.
     *
     * @return the nickname of the player that has ended his turn
     */
    public String getOldPlayer() {
        return oldPlayer;
    }
}
