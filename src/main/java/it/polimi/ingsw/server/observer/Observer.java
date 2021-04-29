package it.polimi.ingsw.server.observer;

import it.polimi.ingsw.messages.Message;

public interface Observer {
    void update(Message message);
}
