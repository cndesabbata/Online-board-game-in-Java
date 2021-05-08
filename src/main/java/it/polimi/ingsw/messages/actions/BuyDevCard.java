package it.polimi.ingsw.messages.actions;

import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.controller.leaders.LeaderEffect;
import it.polimi.ingsw.server.controller.Place;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.gameboard.DevSpaceSlot;

import java.util.List;

public class BuyDevCard implements Action {
    private final int level;
    private final Colour colour;
    private final DevSpaceSlot slot;
    private final List<ResourcePosition> cost;
    private List<ResourceQuantity> req;                                                                                 //it is used by the DiscountEffect
    private final List<LeaderEffect> leaderEffects;
    private final UserAction type;

    public BuyDevCard(int level, Colour colour, DevSpaceSlot slot, List<ResourcePosition> cost,
                      List<LeaderEffect> leaderEffects) {
        this.leaderEffects = leaderEffects;
        this.level = level;
        this.colour = colour;
        this.slot = slot;
        this.req = null;
        this.cost = cost;
        type = UserAction.BUY_DEVCARD;
    }

    public List<ResourceQuantity> getReq() {
        return req;
    }

    @Override
    public UserAction getType() {
        return type;
    }

    @Override
    public boolean doAction(Player player) {
        player.getBoard().expendResources(cost);
        DevCard card = player.getGame().drawDevCard(colour, level);
        player.getBoard().getDevSpace().addCard(card, slot);
        return true;
    }

    @Override
    public void checkAction(Player player) throws WrongActionException {
        if (player.isExclusiveActionDone())
            throw new WrongActionException("The player has already done an exclusive action this turn.");
        if (level <= 0 || level >= 4) throw new WrongActionException("There are no cards of such level.");
        DevDeck deck = player.getGame().getDevDecks()[(level - 1) * Colour.values().length + colour.ordinal()];
        if (deck.isEmpty())
            throw new WrongActionException("The selected deck is empty.");
        if (!player.getBoard().getDevSpace().checkPlace(level, slot))
            throw new WrongActionException("Incorrect Development Space placement.");
        checkCost(player);
        player.getBoard().getWarehouse().checkDecrement(cost);
        player.getBoard().getChest().checkDecrement(cost);
    }

    private void checkCost(Player player) throws WrongActionException {
        req = player.getGame().getDevDecks()[(level - 1) * Colour.values().length + colour.ordinal()].peepRequirements();
        for (LeaderEffect effect : leaderEffects) {
            effect.doLeaderEffect(player, this);
        }
        if (cost.stream().anyMatch(Rp -> Rp.getPlace() == Place.TRASH_CAN))
            throw new WrongActionException("Resources from the trashcan cannot be withdrawn.");
        for (ResourceQuantity Rq : req) {
            if (cost.stream().filter(Rp -> Rp.getResource() == Rq.getResource())
                    .map(ResourcePosition::getQuantity).reduce(0, Integer::sum) < Rq.getQuantity())
                throw new WrongActionException("The resources specified by the user are different from the ones required by the Development Card.");
        }
        int costQuantity = cost.stream().map(ResourcePosition::getQuantity).reduce(0, Integer::sum);
        int reqQuantity = req.stream().map(ResourceQuantity::getQuantity).reduce(0, Integer::sum);
        if (costQuantity != reqQuantity)
            throw new WrongActionException("The resources specified by the user are more than the ones required by the Development Card.");
    }

}
