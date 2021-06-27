package it.polimi.ingsw.server.observer;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.serverNetwork.VirtualView;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Observer represents represents a generic class that can be observed by
 * implementing the {@link Observer} interface.
 *
 */
public class Observable {
    private final List<Observer> observers = new ArrayList<>();

    /**
     * Adds an observer.
     *
     * @param obs the observer to add
     */
    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    /**
     * Removes an observer.
     *
     * @param obs the observer to remove
     */
    public void removeObserver(Observer obs) {
        observers.remove(obs);
    }

    /**
     * Notifies all the observers using their update method passing them a {@link Message}.
     *
     * @param message the message passed to the observer
     */
    protected void notifyObservers(Message message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    /**
     * Returns the list of observers.
     *
     * @return the list of observers
     */
    public List<Observer> getObservers() {
        return observers;
    }

    /**
     * Notifies a single observer identified by the nickname of the player to notify.
     *
     * @param message  the message passed to the observer
     * @param nickname the nickname of the player
     */
    protected void notifySingleObserver(Message message, String nickname){
        for (Observer observer : observers) {
            if (observer instanceof VirtualView){
                if (((VirtualView) observer).getNickname().equals(nickname)) observer.update(message);
            }
        }
    }
}
