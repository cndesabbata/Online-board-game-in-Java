package it.polimi.ingsw.messages.actions;

import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.gameboard.NumOfShelf;
import it.polimi.ingsw.server.model.gameboard.Warehouse;

/**
 * Class MoveResources is an {@link Action}. It's created and sent to the server when the player wants
 * to move resources from one of shelf of the warehouse to another shelf or to a depot leader.
 *
 */
public class MoveResources implements Action {
    private final NumOfShelf srcShelf;
    private final NumOfShelf destShelf;
    private final int quantity;
    private final UserAction type;

    /**
     * Creates a new MoveResources instance.
     *
     * @param srcShelf  the source shelf
     * @param destShelf the destination shelf
     * @param quantity  the amount of resources to move
     */
    public MoveResources(NumOfShelf srcShelf, NumOfShelf destShelf, int quantity) {
        this.srcShelf = srcShelf;
        this.destShelf = destShelf;
        this.quantity = quantity;
        this.type = UserAction.MOVE_RESOURCES;
    }

    /**
     * Moves an amount of resources from a shelf to another, or to a depot leader.
     *
     * @param player the player performing the action
     * @return {@code false}
     */
    @Override
    public boolean doAction(Player player){
        Warehouse warehouse = player.getBoard().getWarehouse();
        warehouse.moveResource(srcShelf, destShelf, quantity);
        return false;
    }

    @Override
    public UserAction getType() {
        return type;
    }

    /**
     * Checks if the resources can be moved.
     *
     * @param player the player who wants to perform the action
     * @throws WrongActionException if the resources cannot be moved
     */
    @Override
    public void checkAction(Player player) throws WrongActionException {
        Warehouse warehouse = player.getBoard().getWarehouse();
        warehouse.checkMove(srcShelf, destShelf, quantity);
    }
}
