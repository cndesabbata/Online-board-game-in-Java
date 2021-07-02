package it.polimi.ingsw.messages.actions;

import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.controller.leaders.LeaderEffect;
import it.polimi.ingsw.server.controller.Place;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class BuyResources is an {@link Action}. It's created and sent to the server when the player
 * wants to buy resources from the market.
 *
 */
public class BuyResources implements Action {
    private final int position;
    private final MarketSelection marketSelection;
    private final List<ResourcePosition> gainedRes;
    private final List<ResourcePosition> extraRes;
    private final List<LeaderEffect> leaderEffects;
    private boolean leaderUsed;
    private final UserAction type;

    /**
     * Creates a new BuyResources instance.
     *
     * @param leaderEffects   the list of leader effects that can modify the action
     * @param position        the number of the row or column selected
     * @param marketSelection the player's choice (row or column)
     * @param gainedRes       the resources gained from the market
     */
    public BuyResources(List<LeaderEffect> leaderEffects, int position, MarketSelection marketSelection,
                        List<ResourcePosition> gainedRes) {
        this.leaderEffects = leaderEffects;
        this.position = position;
        this.marketSelection = marketSelection;
        this.gainedRes = gainedRes;
        this.extraRes = new ArrayList<>();
        this.type = UserAction.BUY_RESOURCES;
    }

    /**
     * Adds the resources gained from the market and sets the new market disposition.
     * If the player has decided to discard some of the resources, updates other players' position
     * on the itinerary and checks if a papal report will be triggered in order to
     * avoid notifying the virtual views multiple times.
     *
     * @param player the player performing the action
     * @return {@code true}
     */
    @Override
    public boolean doAction(Player player) {
        List<ResourcePosition> boughtResources = new ArrayList<>(gainedRes);
        boughtResources.addAll(extraRes);
        int updateOthers = (int) boughtResources.stream().filter(r -> r.getPlace() == Place.TRASH_CAN).count();
        int updateOwn = (int) boughtResources.stream().filter(r -> r.getResource() == Resource.FAITHPOINT).count();
        boolean notify = true;
        if(player.getBoard().getItinerary().getBlackCrossPosition() == null) {                                          //case Multiplayer
            for(Player p : player.getGame().getPlayers()){                                                              //check if a papal report will be triggered
                if (!p.getNickname().equalsIgnoreCase(player.getNickname())){
                    int oldPosition = p.getBoard().getItinerary().getPosition();
                    notify = notify && p.getBoard().getItinerary().toNotify(oldPosition, updateOthers);
                }
                else
                    notify = notify && player.getBoard().getItinerary().toNotify(player.getBoard().getItinerary().getPosition(), updateOwn);
            }
            for (Player otherPlayer : player.getGame().getPlayers()) {
                if (!otherPlayer.equals(player))
                    otherPlayer.getBoard().getItinerary().updatePosition(updateOthers, null, notify);
                else
                    player.getBoard().getItinerary().updatePosition(updateOwn, null, notify);
            }
        }
        else {
            int ownOldPosition = player.getBoard().getItinerary().getPosition();
            int oldBlackPosition = player.getBoard().getItinerary().getBlackCrossPosition();
            notify = player.getBoard().getItinerary().toNotify(ownOldPosition, updateOwn)
                    && player.getBoard().getItinerary().toNotify(oldBlackPosition, updateOthers);
            player.getBoard().getItinerary().updatePosition(updateOwn, updateOthers, notify);
        }
        player.getBoard().getWarehouse().incrementResource(boughtResources);
        player.getGame().getMarket().setDisposition(marketSelection, position);
        return true;
    }

    @Override
    public UserAction getType() {
        return type;
    }

    /**
     * Checks if the user has selected a valid row or column, if the arguments provided when creating
     * the action match the state of the model objects and if the bought resources can be stored in the
     * places specified by the player.
     *
     * @param player the player who wants to perform the action
     * @throws WrongActionException if one of the checks fails
     */
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

    /**
     * Adds the extra resources gained from the marble leader card.
     *
     * @param extraRes the extra resources gained from the marble leader card
     */
    public void addExtraRes(List<ResourcePosition> extraRes) {
        this.extraRes.addAll(extraRes);
    }

    /**
     * Sets the leaderUsed attribute.
     *
     * @param leaderUsed the new leaderUsed attribute
     */
    public void setLeaderUsed(boolean leaderUsed) {
        this.leaderUsed = leaderUsed;
    }

}

