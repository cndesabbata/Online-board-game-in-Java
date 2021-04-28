package it.polimi.ingsw.server.model.gameboard;
import it.polimi.ingsw.server.model.CardStatus;
import it.polimi.ingsw.server.observer.Observable;

//ricorda che le carte papali sono sempre 2, 3 e 4 (in questo ordine)
public class Itinerary extends Observable {
    private int position;
    private final CardStatus[] cardstatus;
    private Integer blackCrossPosition;

    public Itinerary(){
        position = 0;
        blackCrossPosition = null;
        cardstatus = new CardStatus[3];        //usa il for
        for(int i = 0; i < 3; i++) {
            cardstatus[i] = CardStatus.FACE_DOWN;
        }
    }

    public void setBlackCrossPosition(Integer blackCrossPosition) {
        this.blackCrossPosition = blackCrossPosition;
    }

    public void updateBlackCross(int amount){
        if (blackCrossPosition + amount > 24) blackCrossPosition = 24;
        else blackCrossPosition += amount;
    }

    public Integer getBlackCrossPosition() {
        return blackCrossPosition;
    }

    public int getPosition() {
        return position;
    }

    public CardStatus[] getCardStatus() {
         CardStatus[] copy = new CardStatus[3];
         System.arraycopy(cardstatus,0,copy,0, 3);
         return copy;
    }

    public void setCardStatus(CardStatus cardStatus, int index) {
        cardstatus[index] = cardStatus;
    }

    public void updatePosition(int amount) {
        if (position + amount > 24) position = 24;
        else position += amount;
    }
}
