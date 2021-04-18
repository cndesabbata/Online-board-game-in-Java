package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

public interface Action {

    boolean doAction(Player player);

    void checkAction(Player player) throws WrongActionException;

}

