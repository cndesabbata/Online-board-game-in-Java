package it.polimi.ingsw.messages.newElement;

import it.polimi.ingsw.server.model.CardStatus;

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
}
