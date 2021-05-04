package it.polimi.ingsw.messages.newElement;

public class NewIndex implements ChangeMessage{
    private int playerIndex;

    public NewIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}
