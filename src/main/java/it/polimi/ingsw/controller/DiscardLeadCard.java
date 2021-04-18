package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.WrongActionException;

import java.util.ArrayList;

public class DiscardLeadCard implements Action{
    private final int index;
    ArrayList<LeaderCard> hand;

    public DiscardLeadCard(int index) {
        this.index = index;
    }

    @Override
    public boolean doAction(Player player) {
        hand.remove(index);
        player.getBoard().getItinerary().updatePosition(1);
        return false;
    }

    @Override
    public void checkAction(Player player) throws WrongActionException {
        hand = player.getHandLeaderCards();
        if (index <= 0 || index > hand.size())
            throw new WrongActionException("The specified index is out of bounds");
    }
}
