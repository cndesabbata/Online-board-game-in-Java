package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.WrongActionException;

public interface LeaderEffect {

    public void doLeaderEffect (Player player, Action action) throws WrongActionException;
}

