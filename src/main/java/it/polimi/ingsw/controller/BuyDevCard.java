package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;

public class BuyDevCard implements Action {
    private final int level;
    private final Colour colour;
    private final int slot;
    private final ArrayList<ResourcePosition> cost;
    private final ArrayList<LeaderEffect> leaderEffects;
    private ArrayList<ResourceQuantity> req;

    public BuyDevCard(int level, Colour colour, int slot, ArrayList<ResourcePosition> cost,
                      ArrayList<LeaderEffect> leaderEffects) {
        this.level = level;
        this.colour = colour;
        this.slot = slot;
        this.cost = cost;
        this.leaderEffects = leaderEffects;
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
        if (player.isActionAlreadyDone())
            throw new WrongActionException("You have already done an exclusive action this turn");
        if (level <= 0 || level >= 4) throw new WrongActionException("There are no cards of such level");
        if (slot <= 0 || slot >= 4) throw new WrongActionException("There are only three slots available");
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
            throw new WrongActionException("The number of required resources is wrong");
        int l = cost.size();
        for (int i = 0; i < l; i++){
            if (cost.get(i).getPlace().equals(Place.TRASH_CAN))
                throw new WrongActionException("You cannot withdraw resources from the trashcan");
            if (!cost.get(i).getResource().equals(req.get(i).getResource()))
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
