package it.polimi.ingsw.messages.actions;

import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.controller.leaders.LeaderEffect;
import it.polimi.ingsw.server.controller.Place;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.*;

import java.util.ArrayList;
import java.util.List;

public class BuyResources implements Action {
    private final int position;
    private final MarketSelection marketSelection;
    private final List<ResourcePosition> gainedRes;
    private final List<ResourcePosition> extraRes;
    private final List<LeaderEffect> leaderEffects;
    private boolean leaderUsed;
    private final UserAction type;

    public BuyResources(List<LeaderEffect> leaderEffects, int position, MarketSelection marketSelection,
                        List<ResourcePosition> gainedRes) {
        this.leaderEffects = leaderEffects;
        this.position = position;
        this.marketSelection = marketSelection;
        this.gainedRes = gainedRes;
        this.extraRes = new ArrayList<>();
        this.type = UserAction.BUY_RESOURCES;
    }

    @Override
    public boolean doAction(Player player) {
        List<ResourcePosition> boughtResources = new ArrayList<>(gainedRes);
        boughtResources.addAll(extraRes);
        for(int i = 0; i < boughtResources.size(); i++) {
            if (boughtResources.get(i).getResource() == Resource.FAITHPOINT)
                player.getBoard().getItinerary().updatePosition(1);
            if(boughtResources.get(i).getPlace() == Place.TRASH_CAN) {
                for(Player otherPlayer : player.getGame().getPlayers()) {
                    if(!otherPlayer.equals(player))
                        otherPlayer.getBoard().getItinerary().updatePosition(1);
                }
            }
        }
        player.getBoard().getWarehouse().incrementResource(boughtResources);
        player.getGame().getMarket().setDisposition(marketSelection, position);
        return true;
    }

    @Override
    public UserAction getType() {
        return type;
    }

    @Override
    public void checkAction(Player player) throws WrongActionException {
        if (player.isExclusiveActionDone())
            throw new WrongActionException("The player has already done an exclusive action this turn. ");
        if (position <= 0 || (marketSelection == MarketSelection.ROW && position > 3) ||
                (marketSelection == MarketSelection.COLUMN && position > 4)) {
            throw new WrongActionException("The player must select an existing row or column. ");
        }
        for(LeaderEffect leaderEffect : leaderEffects){
            leaderEffect.doLeaderEffect(player, this);
        }
        List<ResourcePosition> boughtResources = new ArrayList<>(gainedRes);
        boughtResources.addAll(extraRes);
        List<Resource> marketResources = new ArrayList<>();
        int whiteMarbles = 0;
        Marble[][] disposition = player.getGame().getMarket().getDisposition();
        if (marketSelection == MarketSelection.ROW) {
            for (int i = 0; i < 4; i++) {
                if (disposition[position - 1][i] == Marble.YELLOW)
                    marketResources.add(Resource.COIN);
                if (disposition[position - 1][i] == Marble.GREY)
                    marketResources.add(Resource.STONE);
                if (disposition[position - 1][i] == Marble.PURPLE)
                    marketResources.add(Resource.SERVANT);
                if (disposition[position - 1][i] == Marble.BLUE)
                    marketResources.add(Resource.SHIELD);
                if (disposition[position - 1][i] == Marble.RED)
                    marketResources.add(Resource.FAITHPOINT);
                if (disposition[position - 1][i] == Marble.WHITE)
                    whiteMarbles++;
            }
        } else if (marketSelection == MarketSelection.COLUMN) {
            for (int i = 0; i < 3; i++) {
                if (disposition[i][position - 1] == Marble.YELLOW)
                    marketResources.add(Resource.COIN);
                if (disposition[i][position - 1] == Marble.GREY)
                    marketResources.add(Resource.STONE);
                if (disposition[i][position - 1] == Marble.PURPLE)
                    marketResources.add(Resource.SERVANT);
                if (disposition[i][position - 1] == Marble.BLUE)
                    marketResources.add(Resource.SHIELD);
                if (disposition[i][position - 1] == Marble.RED)
                    marketResources.add(Resource.FAITHPOINT);
                if (disposition[i][position - 1] == Marble.WHITE)
                    whiteMarbles++;
            }
        }
        List<Resource> gainedRes1 = new ArrayList<>();
        for (ResourcePosition resourcePosition : gainedRes)
            gainedRes1.add(resourcePosition.getResource());
        if (whiteMarbles != extraRes.size() && leaderUsed)
            throw new WrongActionException("Extra resources from marble leader do not match white marbles number. ");
        if (!marketResources.containsAll(gainedRes1) || marketResources.size() != gainedRes1.size())
            throw new WrongActionException("Gained resources do not match marbles in the selected row/column. ");
        player.getBoard().getWarehouse().checkIncrement(boughtResources);
    }

    /* adds the extra resources gained from marble leader card */
    public void addExtraRes(List<ResourcePosition> extraRes) {
        this.extraRes.addAll(extraRes);
    }

    public void setLeaderUsed(boolean leaderUsed) {
        this.leaderUsed = leaderUsed;
    }
}

