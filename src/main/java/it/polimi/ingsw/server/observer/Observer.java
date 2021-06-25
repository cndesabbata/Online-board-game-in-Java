package it.polimi.ingsw.server.observer;

import it.polimi.ingsw.messages.Message;

/**
 * Interface Observer represents a generic observer in the Observer-Observable
 * pattern. It supports a generic method of update.
 *
 */
public interface Observer {
    void update(Message message);
}
