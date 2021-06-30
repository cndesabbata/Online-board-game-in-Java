package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.messages.Message;

/**
 * Class LeaderCardSelection is a {@link Message} sent from the player to server. It contains
 * the indexes of the leaders cards the player wants to discard in the setup phase.
 *
 */
public class LeaderCardSelection implements Message {
    private final int[] indexes;

    /**
     * Creates a new LeaderCardSelection instance.
     *
     * @param indexes the index of the leader cards the player wants to discard
     */
    public LeaderCardSelection(int[] indexes) {
        this.indexes = indexes;
    }

    /**
     * Returns the indexes of the leader cards the player wants to discard.
     *
     * @return the indexes of the leader cards the player want to discard
     */
    public int[] getIndexes() {
        return indexes;
    }

}
