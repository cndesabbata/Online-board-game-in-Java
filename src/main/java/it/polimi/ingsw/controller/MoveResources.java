package it.polimi.ingsw.controller;
import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;

public class MoveResources implements Action{
  private NumOfShelf srcShelf;
  private NumOfShelf destShelf;
  private int quantity;
  private ArrayList<LeaderEffect> leaderEffects;

    public MoveResources(NumOfShelf srcShelf, NumOfShelf destShelf, int quantity, ArrayList <LeaderEffect> leaderEffects) {
        this.srcShelf = srcShelf;
        this.destShelf = destShelf;
        this.quantity = quantity;
        this.leaderEffects = leaderEffects;
    }

    @Override
    public boolean doAction(Player player){
        Warehouse warehouse = player.getBoard().getWarehouse();
        warehouse.moveResource(srcShelf, destShelf, quantity);
        return false;
    }

    @Override
    public void checkAction(Player player) throws WrongActionException {
        for(LeaderEffect leaderEffect : leaderEffects){                                                                 //it's used for depotLeader
            leaderEffect.doLeaderEffect(player, this);
        }
        Warehouse warehouse = player.getBoard().getWarehouse();
        warehouse.checkMove(srcShelf, destShelf, quantity);
    }
}
