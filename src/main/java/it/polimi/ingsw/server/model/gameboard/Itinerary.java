package it.polimi.ingsw.server.model.gameboard;
import it.polimi.ingsw.messages.serverMessages.newElement.NewItinerary;
import it.polimi.ingsw.server.observer.Observable;

/**
 * Class Itinerary represents the player's itinerary on the game board.
 */
public class Itinerary extends Observable {
    private int position;
    private final CardStatus[] cardStatus;
    private Integer blackCrossPosition;
    private final String owner;

    /**
     * Default constructor. Note that the black cross position is set
     * to null.
     *
     * @param nickname the nickname of the player associated with the itinerary
     */
    public Itinerary(String nickname){
        owner = nickname;
        position = 0;
        blackCrossPosition = null;
        cardStatus = new CardStatus[3];
        for(int i = 0; i < 3; i++) {
            cardStatus[i] = CardStatus.FACE_DOWN;
        }
    }

    /**
     * Sets the black cross position and updates all players' virtual view of
     * a new itinerary with a NewItinerary message. Used in the setup phase
     * of a single player match.
     *
     * @param blackCrossPosition the new position for the black cross
     */
    public void setBlackCrossPosition(Integer blackCrossPosition) {
        this.blackCrossPosition = blackCrossPosition;
        notifyObservers(new NewItinerary(position, cardStatus, blackCrossPosition, owner));
    }

    /**
     * Notifies a player's virtual view with a NewItinerary message. Used when
     * player is reconnecting to a game.
     *
     * @param nickname the nickname of the player to notify
     */
    public void notifyNew(String nickname){
        notifySingleObserver(new NewItinerary(position, cardStatus, blackCrossPosition, owner), nickname);
    }

    /**
     * Returns the black cross position on the itinerary.
     *
     * @return the black cross position
     */
    public Integer getBlackCrossPosition() {
        return blackCrossPosition;
    }

    /**
     * Returns the player's position on the itinerary.
     *
     * @return the player's position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Returns a copy of the array of papal card states.
     *
     * @return
     */
    public CardStatus[] getCardStatus() {
         CardStatus[] copy = new CardStatus[3];
         System.arraycopy(cardStatus,0,copy,0, 3);
         return copy;
    }

    /**
     * Sets the state of a specified papal card.
     *
     * @param cardStatus the new state of the card
     * @param index      the index of the card
     */
    public void setCardStatus(CardStatus cardStatus, int index) {
        this.cardStatus[index] = cardStatus;
        notifyObservers(new NewItinerary(position, this.cardStatus, blackCrossPosition, owner));
    }

    /**
     * Updates the position of the player's cross and the black cross (note that
     * the maximum position 24). If necessary, it notifies all players'
     * virtual views with a NewItinerary message.
     *
     * @param amountCross the amount of positions by which the player's cross will be increased
     * @param amountBlack the amount of positions by which the black cross will be increased
     * @param toNotify set to {@code true} if all players' virtual views need to be notified, {@code false} otherwise
     */
    public void updatePosition(int amountCross, Integer amountBlack, boolean toNotify) {
        if (position + amountCross > 24) position = 24;
        else position += amountCross;
        if(amountBlack != null) {
            if (blackCrossPosition + amountBlack > 24) blackCrossPosition = 24;
            else blackCrossPosition += amountBlack;
        }
        if(toNotify)
            notifyObservers(new NewItinerary(position, cardStatus, blackCrossPosition, owner));
    }

    /**
     * Checks if increasing the player's position on the itinerary will cause a change
     * of state for one of the papal cards.
     *
     * @param oldPosition the old position of the player's cross
     * @param amount      the amount of positions by which the player's cross will be increased
     * @return {@code false} if increasing the position causes a change of state for a papal card, {@code true} otherwise
     */
    public boolean toNotify(int oldPosition, int amount){
        if(oldPosition < 8 && oldPosition + amount >= 8 && cardStatus[0] == CardStatus.FACE_DOWN)
            return false;
        else if(oldPosition < 16 && oldPosition + amount >= 16 && cardStatus[1] == CardStatus.FACE_DOWN)
            return false;
        else if(oldPosition < 24 && oldPosition + amount >= 24 && cardStatus[2] == CardStatus.FACE_DOWN)
            return false;
        return true;
    }

}
