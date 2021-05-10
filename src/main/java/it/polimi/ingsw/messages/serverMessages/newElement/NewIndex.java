package it.polimi.ingsw.messages.serverMessages.newElement;

public class NewIndex implements ChangeMessage{
    private int playerIndex;

    public NewIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}
