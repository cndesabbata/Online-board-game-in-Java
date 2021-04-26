package it.polimi.ingsw.controller.messages.actions;
import it.polimi.ingsw.controller.leaders.LeaderEffect;
import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.gameboard.NumOfShelf;
import it.polimi.ingsw.model.gameboard.Warehouse;

import java.util.List;

public class MoveResources implements Action {
    private NumOfShelf srcShelf;
    private NumOfShelf destShelf;
    private int quantity;
    private List<LeaderEffect> leaderEffects;

    public MoveResources(NumOfShelf srcShelf, NumOfShelf destShelf, int quantity, List<LeaderEffect> leaderEffects) {
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
