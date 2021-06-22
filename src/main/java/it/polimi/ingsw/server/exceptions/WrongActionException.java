package it.polimi.ingsw.server.exceptions;

/**
 * Class WrongActionException is thrown when a player tries to perform
 * an action that breaks one or more rules of the game.
 *
 */
public class WrongActionException extends Exception{

    /**
     * Default constructor.
     *
     * @param message the short message explaining why the action cannot be performed
     */
    public WrongActionException(String message) {
        super(message);
    }

}
