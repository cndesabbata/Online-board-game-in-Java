package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.exceptions.WrongActionException;

import java.util.ArrayList;
import java.util.List;

public class DiscardLeadCard extends Action{
    private final int index;
    List<LeaderCard> hand;

    public DiscardLeadCard(int index, List <LeaderEffect> leaderEffects ) {
        super(leaderEffects);
        this.index = index;
        this.hand = null;
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
