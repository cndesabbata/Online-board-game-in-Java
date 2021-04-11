package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

public interface Action {
    boolean doAction(Player player, boolean actionDone);
}
