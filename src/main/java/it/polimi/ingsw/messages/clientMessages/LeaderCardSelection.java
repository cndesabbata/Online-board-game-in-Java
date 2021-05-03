package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.messages.Message;

public class LeaderCardSelection implements Message {
    private final int[] indexes;

    public LeaderCardSelection(int[] indexes) {
        this.indexes = indexes;
    }

    public int[] getIndexes() {
        return indexes;
    }

}
