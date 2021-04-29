package it.polimi.ingsw.messages.actions;

import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.*;

import java.io.Serializable;

public interface Action extends Serializable {

     boolean doAction(Player player);

     void checkAction(Player player) throws WrongActionException;

}

