package it.polimi.ingsw.server.controller.leaders;

import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.exceptions.WrongActionException;

import java.io.Serializable;

/**
 * Interface LeaderEffect represents the generic effect of a leader card.
 * It supports a generic method that applies the effect to an action.
 *
 */
public interface LeaderEffect extends Serializable {

    void doLeaderEffect(Player player, Action action) throws WrongActionException;
}

