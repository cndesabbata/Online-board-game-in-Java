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
