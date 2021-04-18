package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;

public class MoveResources extends Action{
  private NumOfShelf srcShelf;
  private NumOfShelf destShelf;
  private int quantity;

    public MoveResources(NumOfShelf srcShelf, NumOfShelf destShelf, int quantity, ArrayList <LeaderEffect> leaderEffects) {
        super(leaderEffects);
        this.srcShelf = srcShelf;
        this.destShelf = destShelf;
        this.quantity = quantity;
    }

    @Override
    public boolean doAction(Player player){
        Warehouse warehouse = player.getBoard().getWarehouse();
        warehouse.moveResource(srcShelf, destShelf, quantity);
        return false;
    }

    @Override
    public void checkAction(Player player) throws WrongActionException {
        Warehouse warehouse = player.getBoard().getWarehouse();
        warehouse.checkMove(srcShelf,destShelf,quantity);
    }
}
