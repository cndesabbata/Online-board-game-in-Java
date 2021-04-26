package it.polimi.ingsw.controller.messages.actions;

import it.polimi.ingsw.controller.messages.ClientMessage;
import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.model.*;

public interface Action extends ClientMessage {

     boolean doAction(Player player);

     void checkAction(Player player) throws WrongActionException;

}

