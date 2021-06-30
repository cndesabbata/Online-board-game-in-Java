package it.polimi.ingsw.messages.actions;

import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.gameboard.Warehouse;

import java.util.List;

/**
 * Class PlayLeadcard is an {@link Action}. It's created and sent to the server when the player
 * wants to play a leader card.
 *
 */
public class PlayLeadCard implements Action {
    private final int index;
    private final UserAction type;

    /**
     * Creates a new PlayLeadCard instance.
     *
     * @param index the index of the leader card to play
     */
    public PlayLeadCard(int index) {
        this.index = index;
        this.type = UserAction.PLAY_LEADCARD;
    }

    @Override
    public UserAction getType() {
        return type;
    }

    /**
     * Removes the leader cards from the player's hand to the played leader cards.
     * If the card is a depot leader, calls the {@link Warehouse#addDepot(Resource)}
     * method on the player's warehouse.
     *
     * @param player the player performing the action
     * @return {@code false}
     */
    @Override
    public boolean doAction(Player player) {
        LeaderCard l = player.getHandLeaderCards().get(index - 1);
        if(l.getType() == LeaderType.DEPOT){
            player.getBoard().getWarehouse().addDepot(l.getResource());
        }
        player.playLeadCard(index - 1);
        return false;
    }

    /**
     * Checks if the leader card can be played.
     *
     * @param player the player who wants to perform the action
     * @throws WrongActionException if the index is not valid or if the card requirements are not satisfied
     */
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
