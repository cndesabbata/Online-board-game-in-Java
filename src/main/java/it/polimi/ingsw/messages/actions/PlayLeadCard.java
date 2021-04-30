package it.polimi.ingsw.messages.actions;

import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.*;

import java.util.List;

public class PlayLeadCard implements Action {
    private final int index;


    public PlayLeadCard(int index) {
        this.index = index;
    }

    @Override
    public boolean doAction(Player player) {
        player.getHandLeaderCards().get(index - 1).setPlayed(true);
        return true;
    }

    @Override
    public void checkAction(Player player) throws WrongActionException {
        List<LeaderCard> hand = player.getHandLeaderCards();
        if (index <= 0 || index > hand.size())
            throw new WrongActionException("The specified index is out of bounds.");
        LeaderCard card = hand.get(index - 1);
        if (null == card.getCardRequirements()){
            List<ResourceQuantity> requirements = card.getResourceRequirements();
            if (!player.getBoard().checkResources(requirements))
                throw new WrongActionException("The player does not have the required resources.");
        }
        else {
            List<DevCard> requirements = card.getCardRequirements();
            for (DevCard dc : requirements){
                if (!player.getBoard().getDevSpace().checkCards(dc))
                    throw new WrongActionException("The player does not have the required cards.");
            }
        }
    }
}
