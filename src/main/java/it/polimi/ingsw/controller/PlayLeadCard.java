package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class PlayLeadCard implements Action {
    private int index;

    public PlayLeadCard(int index) {
        this.index = index;
    }

    @Override
    public boolean doAction(Player player) {
        ArrayList<LeaderCard> hand = player.getHandLeaderCards();
        hand.get(index).setPlayed(true);
        return false;
    }

    @Override
    public void checkAction(Player player) {

    }
}
