package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;

public class BuyResources extends Action {
    private final int position;
    private final MarketSelection marketSelection;
    private final ArrayList<ResourcePosition> gainedRes;
    private final ArrayList<ResourcePosition> extraRes;                                                                       //it represents the resource gained thanks to the effect of a Marble Leader

    public BuyResources(ArrayList<LeaderEffect> leaderEffects, int position, MarketSelection marketSelection,
                        ArrayList<ResourcePosition> gainedRes) {
        super(leaderEffects);
        this.position = position;
        this.marketSelection = marketSelection;
        this.gainedRes = gainedRes;
        this.extraRes = new ArrayList<>();
    }

    @Override
    public boolean doAction(Player player) {
        ArrayList<ResourcePosition> boughtResources = new ArrayList<>(gainedRes);
        boughtResources.addAll(extraRes);
        for (ResourcePosition resourcePosition : boughtResources) {
            if (resourcePosition.getResource() == Resource.FAITHPOINT) {
                player.getBoard().getItinerary().updatePosition(1);
                boughtResources.remove(resourcePosition);
            }
        }
        player.getBoard().getWarehouse().incrementResource(boughtResources);
        player.getGame().getMarket().setDisposition(marketSelection, position);
        return true;
    }

    @Override
    public void checkAction(Player player) throws WrongActionException {
        if (player.isActionDone())
            throw new WrongActionException("The player has already done an exclusive action this turn");
        if (position < 0 || (marketSelection == MarketSelection.ROW && position > 3) ||
                (marketSelection == MarketSelection.COLUMN && position > 4)) {
            throw new WrongActionException("The player must select an existing row or column");
        }
        ArrayList<ResourcePosition> boughtResources = new ArrayList<>(gainedRes);
        boughtResources.addAll(extraRes);
        ArrayList<Resource> marketResources = new ArrayList<>();
        int whiteMarbles = 0;
        Marble[][] disposition = player.getGame().getMarket().getDisposition();
        if (marketSelection == MarketSelection.ROW) {
            for (int i = 0; i < 4; i++) {
                if (disposition[position][i] == Marble.YELLOW)
                    marketResources.add(Resource.COIN);
                if (disposition[position][i] == Marble.GREY)
                    marketResources.add(Resource.STONE);
                if (disposition[position][i] == Marble.PURPLE)
                    marketResources.add(Resource.SERVANT);
                if (disposition[position][i] == Marble.BLUE)
                    marketResources.add(Resource.SHIELD);
                if (disposition[position][i] == Marble.RED)
                    marketResources.add(Resource.FAITHPOINT);
                if (disposition[position][i] == Marble.WHITE)
                    whiteMarbles++;
            }
        } else if (marketSelection == MarketSelection.COLUMN) {
            for (int i = 0; i < 3; i++) {
                if (disposition[i][position] == Marble.YELLOW)
                    marketResources.add(Resource.COIN);
                if (disposition[i][position] == Marble.GREY)
                    marketResources.add(Resource.STONE);
                if (disposition[i][position] == Marble.PURPLE)
                    marketResources.add(Resource.SERVANT);
                if (disposition[i][position] == Marble.BLUE)
                    marketResources.add(Resource.SHIELD);
                if (disposition[i][position] == Marble.RED)
                    marketResources.add(Resource.FAITHPOINT);
                if (disposition[i][position] == Marble.WHITE)
                    whiteMarbles++;
            }
        }
        ArrayList<Resource> gainedRes1 = new ArrayList<>();
        for (ResourcePosition resourcePosition : gainedRes)
            gainedRes1.add(resourcePosition.getResource());
        if (whiteMarbles != extraRes.size())
            throw new WrongActionException("Extra resources from marble leader do not match white marbles number");
        if (!marketResources.containsAll(gainedRes1))
            throw new WrongActionException("Gained resources do not match marbles in the selected row/column");
        player.getBoard().getWarehouse().checkIncrement(boughtResources);
    }

    /* sets the extra resources gained from marble leader card */
    public void setExtraRes(ResourcePosition extraRes) {
        this.extraRes.add(extraRes);
    }
}
