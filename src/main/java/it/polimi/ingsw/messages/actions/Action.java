package it.polimi.ingsw.messages.actions;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.*;

public interface Action extends Message {

     boolean doAction(Player player);

     UserAction getType();

     void checkAction(Player player) throws WrongActionException;

}

