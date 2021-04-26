package it.polimi.ingsw.controller.leaders;

import it.polimi.ingsw.controller.messages.actions.Action;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.exceptions.WrongActionException;

public interface LeaderEffect {

    public void doLeaderEffect (Player player, Action action) throws WrongActionException;
}

