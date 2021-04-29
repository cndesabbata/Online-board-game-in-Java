package it.polimi.ingsw.server.controller.leaders;

import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.messages.actions.BuyDevCard;
import it.polimi.ingsw.server.model.*;

import java.util.List;

public class DiscountEffect implements LeaderEffect {
    private final Resource resource;

    public DiscountEffect(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void doLeaderEffect(Player player, Action action) throws WrongActionException {
        if (action instanceof BuyDevCard){
            List<LeaderCard> playerCards = player.getHandLeaderCards();
            boolean check = false;
            for(LeaderCard Lc : playerCards){
                if (Lc.getResource() == resource && Lc.getType() == LeaderType.DISCOUNT && Lc.isPlayed()) {
                    check = true;
                    break;
                }
            }
            if(!check) throw new WrongActionException("The player does not have the corresponding leadCard");
            else {
                for (ResourceQuantity res : ((BuyDevCard) action).getReq()){
                    if (res.getResource() == resource){
                        ((BuyDevCard) action).getReq().remove(res);
                        break;
                    }
                }
            }
        }
    }
}
