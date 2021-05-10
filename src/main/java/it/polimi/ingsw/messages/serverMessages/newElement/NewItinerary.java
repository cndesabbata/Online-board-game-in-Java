package it.polimi.ingsw.messages.serverMessages.newElement;

import it.polimi.ingsw.server.model.gameboard.CardStatus;

public class NewItinerary implements ChangeMessage{
    private int position;
    private final CardStatus[] cardStatus;
    private Integer blackCrossPosition;
    private String owner;

    public NewItinerary(int position, CardStatus[] cardStatus, Integer blackCrossPosition, String owner) {
        this.position = position;
        this.cardStatus = cardStatus;
        this.blackCrossPosition = blackCrossPosition;
        this.owner = owner;
    }

    public int getPosition() {
        return position;
    }

    public CardStatus[] getCardStatus() {
        return cardStatus;
    }

    public Integer getBlackCrossPosition() {
        return blackCrossPosition;
    }

    public String getOwner() {
        return owner;
    }
}
