package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.MarketSelection;
import it.polimi.ingsw.model.NumOfShelf;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.ResourceQuantity;

import java.util.List;

public class BuyResources implements Action {
    private final int position;
    private final MarketSelection marketSelection;
    private final List<LeaderEffect> leaderEffects;
    private final List<ResourceQuantity> boughtResources;
    private final List<NumOfShelf> numOfShelves;

    public BuyResources(int position, MarketSelection marketSelection, List<LeaderEffect> leaderEffectList,
                        List<ResourceQuantity> boughtResources, List<NumOfShelf> numOfShelves) {
        this.position = position;
        this.marketSelection = marketSelection;
        this.leaderEffectList = leaderEffectList;
        this.boughtResources = boughtResources;
        this.numOfShelves = numOfShelves;
    }

    @Override
    public void doAction(Player player) {
        if (checkAction()) {

            player.getGame().getMarket().setDisposition(marketSelection, position);
        }
    }

    public boolean checkAction() {
        if (position < 0 || (marketSelection == MarketSelection.ROW && position > 3) ||
                (marketSelection == MarketSelection.COLUMN && position > 4)) {
            return false;
        }

    }
}
