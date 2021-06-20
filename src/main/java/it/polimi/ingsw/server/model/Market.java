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

    /**
     * Method getDisposition returns a copy of the marble disposition.
     *
     * @return a copy (type Marble[][]) of the marble disposition.
     */
    public Marble[][] getDisposition() {
        Marble[][] dispositionCopy;

        dispositionCopy = Arrays.stream(disposition).map(Marble[]::clone).toArray(Marble[][]::new);
        return dispositionCopy;
    }

    /**
     * Method notifyNew notifies all the observers of the new marble disposition
     * and the new external marble.
     */
    public void notifyNew (){
        notifyObservers(new NewMarket(disposition, external));
    }

    /**
     * Method notifyNew notifies the VirtualView object associated with a player
     * of the new marble disposition and the new external marble.
     *
     * @param nickname of type String - the player's nickname.
     */
    public void notifyNew (String nickname){
        notifySingleObserver(new NewMarket(disposition, external), nickname);
    }

    /**
     * Method getExternal returns the external marble.
     *
     * @return of type Marble - the external marble.
     */
    public Marble getExternal() {
        return external;
    }

    /**
     * Method setDisposition updates the market disposition and the external marble.
     *
     * @param selection of type MarketSelection - the player's choice.
     * @param position of type int - the number of the row/column selected by the player.
     */
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

    /**
     * Method setFakeDisposition updates the market disposition with a desired one.
     * Used in unit tests only.
     *
     * @param disposition of type Marble[][] - the desired disposition.
     */
    public void setFakeDisposition(Marble[][] disposition) {
        this.disposition = disposition;
    }

    /**
     * Method setFakeExternal updates the external marble with a desired one.
     * Used in unit tests only.
     *
     * @param marble of type Marble - the desired marble.
     */
    public void setFakeExternal(Marble marble) {
        this.external = marble;
    }
}
