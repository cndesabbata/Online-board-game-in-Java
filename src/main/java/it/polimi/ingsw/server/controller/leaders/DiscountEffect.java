package it.polimi.ingsw.server.controller.leaders;

import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.messages.actions.BuyDevCard;
import it.polimi.ingsw.server.model.*;

/**
 * Class DiscountEffect represents the effect of the discount leader card.
 *
 */
public class DiscountEffect implements LeaderEffect {
    private final Resource resource;

    /**
     * Default constructor.
     *
     * @param resource the resource this effect can discount
     */
    public DiscountEffect(Resource resource) {
        this.resource = resource;
    }

    /**
     * When buying a development card, it reduces the amount of resources needed of a specified type
     * by 1.
     *
     * @param player the player performing the action
     * @param action the action performed by the player
     * @throws WrongActionException when the user has not played the corresponding leader card
     */
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
