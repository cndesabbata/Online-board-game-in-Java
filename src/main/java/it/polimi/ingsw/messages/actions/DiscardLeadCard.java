package it.polimi.ingsw.messages.actions;

import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.exceptions.WrongActionException;

import java.util.List;

public class DiscardLeadCard implements Action {
    private final int index;
    List<LeaderCard> hand;
    private UserAction type;

    public DiscardLeadCard(int index) {
        this.index = index;
        this.hand = null;
        this.type = UserAction.DISCARD_LEADCARD;
    }

    @Override
    public boolean doAction(Player player) {
        player.discardLeadCard(index - 1);
        player.getBoard().getItinerary().updatePosition(1);
        return false;
    }

    @Override
    public UserAction getType() {
        return type;
    }

    @Override
    public void checkAction(Player player) throws WrongActionException {
        hand = player.getHandLeaderCards();
        if (index <= 0 || index > hand.size())
            throw new WrongActionException("The specified index is out of bounds. ");
    }
}
