package it.polimi.ingsw.messages.serverMessages.newElement;

import it.polimi.ingsw.server.model.gameboard.CardStatus;

/**
 * Class NewItinerary is a {@link ChangeMessage} that contains an updated copy of
 * a player's itinerary.
 *
 */
public class NewItinerary implements ChangeMessage{
    private final int position;
    private final CardStatus[] cardStatus;
    private final Integer blackCrossPosition;
    private final String owner;

    /**
     * Creates a NewItinerary instance.
     *
     * @param position           the new player's position
     * @param cardStatus         the new papal cards states
     * @param blackCrossPosition the new black cross position
     * @param owner              the nickname of the player who owns the itinerary
     */
    public NewItinerary(int position, CardStatus[] cardStatus, Integer blackCrossPosition, String owner) {
        this.position = position;
        this.cardStatus = cardStatus;
        this.blackCrossPosition = blackCrossPosition;
        this.owner = owner;
    }

    /**
     * Returns the new player's position.
     *
     * @return the new player's position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Returns the new array of papal cards states.
     *
     * @return the new array of papal cards states
     */
    public CardStatus[] getCardStatus() {
        return cardStatus;
    }

    /**
     * Returns the new black cross position.
     *
     * @return the new black cross position
     */
    public Integer getBlackCrossPosition() {
        return blackCrossPosition;
    }

    /**
     * Returns the owner of the itinerary.
     *
     * @return the owner of the itinerary
     */
    public String getOwner() {
        return owner;
    }
}
