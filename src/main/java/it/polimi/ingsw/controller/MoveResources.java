package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.*;

public class MoveResources implements Action{
  private NumOfShelf srcShelf;
  private NumOfShelf destShelf;

    public MoveResources(NumOfShelf srcShelf, NumOfShelf destShelf) {
        this.srcShelf = srcShelf;
        this.destShelf = destShelf;
    }

    @Override
    public boolean doAction(Player player){
        Warehouse warehouse = player.getBoard().getWarehouse();
        warehouse.moveResource(srcShelf,destShelf);
        return false;
    }

    @Override
    public void checkAction(Player player) {

    }
}
