package it.polimi.ingsw.server.controller.leaders;

import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.exceptions.WrongActionException;

import java.io.Serializable;

public interface LeaderEffect extends Serializable {

    public void doLeaderEffect (Player player, Action action) throws WrongActionException;
}

