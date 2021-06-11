package it.polimi.ingsw.server.model;

import it.polimi.ingsw.messages.serverMessages.newElement.NewMarket;
import it.polimi.ingsw.server.observer.Observable;

import java.util.*;

/**
 * Market class represents the market. It contains information
 * about the current disposition and the external marble.
 *
 */
public class Market extends Observable {
    private Marble[][] disposition;
    private Marble external;

    /**
     * Constructor Market creates a new Market instance with a
     * random disposition.
     *
     */
    public Market() {
        disposition = new Marble[3][4];
        List<Marble> temp = new ArrayList<>();

        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++){ temp.add(Marble.WHITE); }
            temp.add(Marble.BLUE);
            temp.add(Marble.GREY);
            temp.add(Marble.YELLOW);
            temp.add(Marble.PURPLE);
        }
        temp.add(Marble.RED);
        Collections.shuffle(temp);

        external = temp.remove(0);
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 4; j++) {
                disposition[i][j] = temp.remove(0);
            }
        }
        notifyObservers(new NewMarket(disposition, external));
    }

    /* returns a copy of the current market disposition */
    public Marble[][] getDisposition() {
        Marble[][] dispositionCopy;

        dispositionCopy = Arrays.stream(disposition).map(Marble[]::clone).toArray(Marble[][]::new);
        return dispositionCopy;
    }

    public void notifyNew (){
        notifyObservers(new NewMarket(disposition, external));
    }

    public void notifyNew (String nickname){
        notifySingleObserver(new NewMarket(disposition, external), nickname);
    }

    /* return the external marble */
    public Marble getExternal() {
        return external;
    }

    /* sets the new market disposition depending on user selection */
    public void setDisposition(MarketSelection selection, int position) {
        Marble tempLast;

        if(selection == MarketSelection.ROW) {
            tempLast = disposition[position - 1][0];
            for(int i = 0; i < 3; i++) {
                disposition[position - 1][i] = disposition[position - 1][i + 1];
            }
            disposition[position - 1][3] = external;
            external = tempLast;
        }
        else if(selection == MarketSelection.COLUMN) {
            tempLast = disposition[0][position-1];
            for(int i = 0; i < 2; i++) {
                disposition[i][position - 1] = disposition[i + 1][position - 1];
            }
            disposition[2][position - 1] = external;
            external = tempLast;
        }
        notifyObservers(new NewMarket(disposition, external));
    }

    /* method used in testing */
    public void setFakeDisposition(Marble[][] disposition) {
        this.disposition = disposition;
    }

    /* method used in testing*/
    public void setFakeExternal(Marble marble) {
        this.external = marble;
    }
}
