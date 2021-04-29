package it.polimi.ingsw.messages.actions;

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
    private List<ResourceQuantity> req;
    private List<LeaderEffect> leaderEffects;

    public BuyDevCard(int level, Colour colour, DevSpaceSlot slot, List<ResourcePosition> cost,
                      List<LeaderEffect> leaderEffects) {
        this.leaderEffects = leaderEffects;
        this.level = level;
        this.colour = colour;
        this.slot = slot;
        this.cost = cost;
    }

    @Override
    public boolean doAction(Player player) {
        player.getBoard().expendResources(cost);
        DevCard card = player.getGame().drawDevCard(colour, level);
        player.getBoard().getDevSpace().addCard(card, slot);
        return true;
    }

    public List<ResourceQuantity> getReq() {
        return req;
    }

    @Override
    public void checkAction(Player player) throws WrongActionException {
        if (player.isExclusiveActionDone())
            throw new WrongActionException("The player has already done an exclusive action this turn");
        if (level <= 0 || level >= 4) throw new WrongActionException("There are no cards of such level");
        DevDeck deck = player.getGame().getDevDecks()[(level-1) * Colour.values().length + colour.ordinal()];
        if (deck.isEmpty())
            throw new WrongActionException("The selected deck is empty");
        if (!player.getBoard().getDevSpace().checkPlace(level, slot))
            throw new WrongActionException("Incorrect DevSpace placement");
        checkCost(player);
        checkAvailability(player);
    }

    private void checkCost (Player player) throws WrongActionException{
        req = player.getGame().getDevDecks()[(level-1) * Colour.values().length + colour.ordinal()].peepRequirements();
        for (LeaderEffect effect : leaderEffects){
            effect.doLeaderEffect(player, this);
        }
        if (cost.size() != req.size())
            throw new WrongActionException("The number of required resources is wrong: " + cost.size() + " vs " + req.size());
        for (int i = 0; i < cost.size(); i++){
            if (cost.get(i).getPlace() == Place.TRASH_CAN)
                throw new WrongActionException("You cannot withdraw resources from the trashcan");
            if (cost.get(i).getResource() != req.get(i).getResource())
                throw new WrongActionException("The required resources differ from the Development card requirements");
        }
    }

    private void checkAvailability(Player player) throws WrongActionException{
        if (!player.getBoard().checkResources(cost))
            throw new WrongActionException("You don't have the required resources");
        player.getBoard().getWarehouse().checkDecrement(cost);
        player.getBoard().getChest().checkDecrement(cost);
    }
}
