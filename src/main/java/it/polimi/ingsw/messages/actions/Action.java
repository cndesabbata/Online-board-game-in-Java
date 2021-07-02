package it.polimi.ingsw.messages.actions;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.*;

/**
 * Interface Action is a {@link Message} sent from a client to server that represents a generic action
 * performed by the player.
 *
 */
public interface Action extends Message {

     /**
      * Performs the action chosen by the player. Safety is guaranteed if this method
      * is called after {@link #checkAction(Player)}, if the latter does not throw an
      * exception.
      *
      * @param player the player performing the action
      * @return {@code true} if the action is exclusive, {@code false} otherwise
      */
     boolean doAction(Player player);

     /**
      * Returns the type of the action performed by the player.
      *
      * @return the type of the action
      */
     UserAction getType();

     /**
      * Checks if the action can be performed.
      *
      * @param player the player who wants to perform the action
      * @throws WrongActionException if one of the game rules is broken
      */
     void checkAction(Player player) throws WrongActionException;

}

