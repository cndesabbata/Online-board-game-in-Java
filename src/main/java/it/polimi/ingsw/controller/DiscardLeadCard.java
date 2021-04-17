package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.Player;

public class DiscardLeadCard implements Action{
    private int index;

    public DiscardLeadCard(int index) {
        this.index = index;
    }

    @Override
    public boolean doAction(Player player) {
        player.getHandLeaderCards().remove(index);
        player.getBoard().getItinerary().updatePosition(1);
        return false;
    }

    @Override
    public void checkAction(Player player) {

    }
}
