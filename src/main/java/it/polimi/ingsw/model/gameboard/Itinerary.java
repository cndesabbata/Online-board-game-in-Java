package it.polimi.ingsw.model.gameboard;
import it.polimi.ingsw.model.CardStatus;

import java.util.*;

//ricorda che le carte papali sono sempre 2, 3 e 4 (in questo ordine)
public class Itinerary {
    private int position;
    private final CardStatus[] cardstatus;

    public Itinerary(){
        position = 0;
        cardstatus = new CardStatus[3];
        for(int i = 0; i < 3; i++) {
            cardstatus[i] = CardStatus.FACE_DOWN;
        }
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
        position += amount;
    }
}
