package it.polimi.ingsw.server.observer;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.serverNetwork.VirtualView;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    public void removeObserver(Observer obs) {
        observers.remove(obs);
    }

    protected void notifyObservers(Message message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public List<Observer> getObservers() {
        return observers;
    }

    protected void notifySingleObserver(Message message, String nickname){
        for (Observer observer : observers) {
            if (observer instanceof VirtualView){
                if (((VirtualView) observer).getNickname().equals(nickname)) observer.update(message);
            }
        }
    }
}
