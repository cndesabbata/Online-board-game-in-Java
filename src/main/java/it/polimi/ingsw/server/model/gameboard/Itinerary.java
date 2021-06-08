package it.polimi.ingsw.server.model.gameboard;
import it.polimi.ingsw.messages.serverMessages.newElement.NewItinerary;
import it.polimi.ingsw.server.observer.Observable;

public class Itinerary extends Observable {
    private int position;
    private final CardStatus[] cardStatus;
    private Integer blackCrossPosition;
    private final String owner;

    public Itinerary(String nickname){
        owner = nickname;
        position = 0;
        blackCrossPosition = null;
        cardStatus = new CardStatus[3];
        for(int i = 0; i < 3; i++) {
            cardStatus[i] = CardStatus.FACE_DOWN;
        }
    }

    public void setBlackCrossPosition(Integer blackCrossPosition) {
        this.blackCrossPosition = blackCrossPosition;
        notifyObservers(new NewItinerary(position, cardStatus, blackCrossPosition, owner));
    }

    public void notifyNew(String nickname){
        notifySingleObserver(new NewItinerary(position, cardStatus, blackCrossPosition, owner), nickname);
    }

    public void updateBlackCross(int amount){
        if (blackCrossPosition + amount > 24) blackCrossPosition = 24;
        else blackCrossPosition += amount;
        notifyObservers(new NewItinerary(position, cardStatus, blackCrossPosition, owner));
    }

    public Integer getBlackCrossPosition() {
        return blackCrossPosition;
    }

    public int getPosition() {
        return position;
    }

    public CardStatus[] getCardStatus() {
         CardStatus[] copy = new CardStatus[3];
         System.arraycopy(cardStatus,0,copy,0, 3);
         return copy;
    }

    public void setCardStatus(CardStatus cardStatus, int index) {
        this.cardStatus[index] = cardStatus;
        notifyObservers(new NewItinerary(position, this.cardStatus, blackCrossPosition, owner));
    }

    public void updatePosition(int amount) {
        if (position + amount > 24) position = 24;
        else position += amount;
        notifyObservers(new NewItinerary(position, cardStatus, blackCrossPosition, owner));
    }
}
