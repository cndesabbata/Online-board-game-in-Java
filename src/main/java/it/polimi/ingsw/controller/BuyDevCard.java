package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;

public class BuyDevCard extends Action {
    private final int level;
    private final Colour colour;
    private final DevSpaceSlot slot;
    private final ArrayList<ResourcePosition> cost;
    private ArrayList<ResourceQuantity> req;

    public BuyDevCard(int level, Colour colour, DevSpaceSlot slot, ArrayList<ResourcePosition> cost,
                      ArrayList<LeaderEffect> leaderEffects) {
        super(leaderEffects);
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

    public ArrayList<ResourceQuantity> getReq() {
        return req;
    }

    @Override
    public void checkAction(Player player) throws WrongActionException {
        if (player.isActionDone())
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
        for (LeaderEffect effect : getLeaderEffects()){
            effect.doLeaderEffect(player, this);
        }
        if (cost.size() != req.size())
            throw new WrongActionException("The number of required resources is wrong");
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
