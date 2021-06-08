package it.polimi.ingsw.messages.actions;

import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.gameboard.NumOfShelf;
import it.polimi.ingsw.server.model.gameboard.Warehouse;

public class MoveResources implements Action {
    private final NumOfShelf srcShelf;
    private final NumOfShelf destShelf;
    private final int quantity;
    private final UserAction type;

    public MoveResources(NumOfShelf srcShelf, NumOfShelf destShelf, int quantity) {
        this.srcShelf = srcShelf;
        this.destShelf = destShelf;
        this.quantity = quantity;
        this.type = UserAction.MOVE_RESOURCES;
    }

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

    @Override
    public void checkAction(Player player) throws WrongActionException {
        Warehouse warehouse = player.getBoard().getWarehouse();
        warehouse.checkMove(srcShelf, destShelf, quantity);
    }
}
