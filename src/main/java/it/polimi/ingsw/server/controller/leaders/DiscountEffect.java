package it.polimi.ingsw.server.controller.leaders;

import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.messages.actions.BuyDevCard;
import it.polimi.ingsw.server.model.*;

public class DiscountEffect implements LeaderEffect {
    private final Resource resource;

    public DiscountEffect(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void doLeaderEffect(Player player, Action action) throws WrongActionException {
        if (action instanceof BuyDevCard){
            if(!(player.hasPlayedLeaderCard(LeaderType.DISCOUNT, resource)))
                throw new WrongActionException("The user does not have the played Discount Leader Card.");
            for (ResourceQuantity res : ((BuyDevCard) action).getReq()){
                if (res.getResource() == resource){
                    res.setQuantity(res.getQuantity() - 1);
                    break;
                }
            }
        }
    }

}
