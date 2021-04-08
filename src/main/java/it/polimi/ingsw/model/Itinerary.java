package it.polimi.ingsw.model;
import java.util.*;

//ricorda che le carte papali sono sempre 2, 3 e 4 (in questo ordine)
public class Itinerary {
    private int position;
    private CardStatus[] cardstatus;

    public Itinerary(){
        position = 0;
        CardStatus[] cardstatus = new CardStatus[3];        //usa il for
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

    public void setCardStatus(CardStatus cardStatus, int index) {       //Metto index per capire quale carta devo girare. Ovviamente, utilizzando la sola posizione,
        cardstatus[index] = cardStatus;                                 //io potrei capire quale carta girare. Tuttavia, questo controllo sulla posizione è meglio farlo fare al controller (per coerenza).
    }

    public void updatePosition(int amount) {                //ci sono carte che fanno avanzare di 1 o 2.
        position += amount;
    }               //dopo l'update il controller verifica se il giocatore ha oltrepassato o raggiunto un rapporto col vaticano. Se sì,
                    //controlla le posizioni degli altri giocatori e in base a quello chiama le setCardStatus su gli itinerary di tutti i giocatori
                    //passando gli appropriati cardstatus.
}
