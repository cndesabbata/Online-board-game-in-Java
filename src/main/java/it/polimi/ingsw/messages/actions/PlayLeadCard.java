package it.polimi.ingsw.messages.actions;

import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.*;

import java.util.List;

public class PlayLeadCard implements Action {
    private final int index;
    private final UserAction type;

    public PlayLeadCard(int index) {
        this.index = index;
        this.type = UserAction.PLAY_LEADCARD;
    }

    @Override
    public UserAction getType() {
        return type;
    }

    @Override
    public boolean doAction(Player player) {
        LeaderCard l = player.getHandLeaderCards().get(index - 1);
        if(l.getType() == LeaderType.DEPOT){
            player.getBoard().getWarehouse().addDepot(l.getResource());
        }
        player.playLeadCard(index - 1);
        return false;
    }

    @Override
    public void checkAction(Player player) throws WrongActionException {
        List<LeaderCard> hand = player.getHandLeaderCards();
        if (index <= 0 || index > hand.size())
            throw new WrongActionException("The specified index is out of bounds. ");
        LeaderCard card = hand.get(index - 1);
        if (card.getCardRequirements() == null){
            List<ResourceQuantity> requirements = card.getResourceRequirements();
            if (!player.getBoard().checkResources(requirements))
                throw new WrongActionException("The player does not have the required resources. ");
        }
        else {
            List<DevCard> requirements = card.getCardRequirements();
            for (DevCard dc : requirements){
                if (!player.getBoard().getDevSpace().checkCards(dc))
                    throw new WrongActionException("The player does not have the required cards. ");
            }
        }
        if (card.getType() == LeaderType.DEPOT){
            if(!player.getBoard().getWarehouse().checkDepot(card.getResource()))
                throw new WrongActionException("The player has already played this depot leader card. ");
        }
    }
}
