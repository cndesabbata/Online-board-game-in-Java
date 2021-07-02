package it.polimi.ingsw.messages.actions;

import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.controller.leaders.LeaderEffect;
import it.polimi.ingsw.server.controller.Place;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.gameboard.DevSpaceSlot;

import java.util.List;

/**
 * Class BuyDevCard is an {@link Action}. It's created and sent to the server when the player
 * wants to buy a development card.
 *
 */
public class BuyDevCard implements Action {
    private final int level;
    private final Colour colour;
    private final DevSpaceSlot slot;
    private final List<ResourcePosition> cost;
    private List<ResourceQuantity> req;                                                                                 //it is used by the DiscountEffect
    private final List<LeaderEffect> leaderEffects;
    private final UserAction type;

    /**
     * Creates a BuyDevCard instance.
     *
     * @param level  the level of the development card
     * @param colour the colour of the development card
     * @param slot   the slot where the player wants to place the development card
     * @param cost   the list of {@link ResourcePosition} objects which represent the resources payed to buy the card
     * @param leaderEffects the list of leader effects that modify the action
     */
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

    /**
     * Returns the list of resources required to buy the card.
     *
     * @return the list of resources required to buy the card
     */
    public List<ResourceQuantity> getReq() {
        return req;
    }

    @Override
    public UserAction getType() {
        return type;
    }

    /**
     * Expends the player's resources to buy the card and places it in the specified slot.
     *
     * @param player the player performing the action
     * @return {@code true}
     */
    @Override
    public boolean doAction(Player player) {
        player.getBoard().expendResources(cost);
        DevCard card = player.getGame().drawDevCard(colour, level);
        player.getBoard().getDevSpace().addCard(card, slot);
        return true;
    }

    /**
     * Checks if the development card can be bought.
     *
     * @param player the player who wants to perform the action
     * @throws WrongActionException if performing the action would break one of the game rules, or if the arguments provided
     * when creating this object are not valid
     */
    @Override
    public void checkAction(Player player) throws WrongActionException {
        if (player.isExclusiveActionDone())
            throw new WrongActionException("The player has already done an exclusive action this turn. ");
        if (level <= 0 || level >= 4) throw new WrongActionException("There are no cards of such level. ");
        DevDeck deck = player.getGame().getDevDecks()[(level - 1) * Colour.values().length + colour.ordinal()];
        if (deck.isEmpty())
            throw new WrongActionException("The selected deck is empty. ");
        if (!player.getBoard().getDevSpace().checkPlace(level, slot))
            throw new WrongActionException("Incorrect Development Space placement. ");
        checkCost(player);
        player.getBoard().getWarehouse().checkDecrement(cost);
        player.getBoard().getChest().checkDecrement(cost);
    }

    /**
     * Applies all the leader effects and checks if the resources provided by the player can be expended
     * to buy the development card.
     *
     * @param player the player performing the action
     * @throws WrongActionException if the resources provided cannot be expended to buy the development card
     */
    private void checkCost(Player player) throws WrongActionException {
        req = player.getGame().getDevDecks()[(level - 1) * Colour.values().length + colour.ordinal()].peepRequirements();
        for (LeaderEffect effect : leaderEffects) {
            effect.doLeaderEffect(player, this);
        }
        if (cost.stream().anyMatch(Rp -> Rp.getPlace() == Place.TRASH_CAN))
            throw new WrongActionException("Resources from the trashcan cannot be withdrawn. ");
        for (ResourceQuantity Rq : req) {
            if (cost.stream().filter(Rp -> Rp.getResource() == Rq.getResource())
                    .map(ResourcePosition::getQuantity).reduce(0, Integer::sum) < Rq.getQuantity())
                throw new WrongActionException("The resources specified by the user are different from the ones required by the Development Card. ");
        }
        int costQuantity = cost.stream().map(ResourcePosition::getQuantity).reduce(0, Integer::sum);
        int reqQuantity = req.stream().map(ResourceQuantity::getQuantity).reduce(0, Integer::sum);
        if (costQuantity != reqQuantity)
            throw new WrongActionException("The resources specified by the user are more than the ones required by the Development Card. ");
    }

}
