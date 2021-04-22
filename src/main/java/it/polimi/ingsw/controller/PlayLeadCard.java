package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;

public class PlayLeadCard implements Action {
    private final int index;
    private LeaderCard card;


    public PlayLeadCard(int index) {
        this.index = index;
        card = null;
    }

    @Override
    public boolean doAction(Player player) {
        card.setPlayed(true);
        return true;
    }

    @Override
    public void checkAction(Player player) throws WrongActionException {
        ArrayList<LeaderCard> hand = player.getHandLeaderCards();
        if (index <= 0 || index > hand.size())
            throw new WrongActionException("The specified index is out of bounds");
        card = hand.get(index - 1);
        if (null == card.getCardRequirements()){
            ArrayList<ResourceQuantity> requirements = card.getResourceRequirements();
            if (!player.getBoard().checkResources(requirements))
                throw new WrongActionException("The player does not have the required resources");
        }
        else {
            ArrayList<DevCard> requirements = card.getCardRequirements();
            for (DevCard card : requirements){
                if (!player.getBoard().getDevSpace().checkCards(card))
                    throw new WrongActionException("You don't have the required cards");
            }

        }

    }
}
