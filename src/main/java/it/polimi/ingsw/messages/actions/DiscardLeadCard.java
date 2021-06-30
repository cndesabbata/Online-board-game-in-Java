package it.polimi.ingsw.messages.actions;

import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.exceptions.WrongActionException;

import java.util.List;

/**
 * Class DiscardLeadcard is an {@link Action}. It's created and sent to the server when the player
 * wants to discard a leader card to obtain a faithpoint.
 *
 */
public class DiscardLeadCard implements Action {
    private final int index;
    List<LeaderCard> hand;
    private final UserAction type;

    /**
     * Creates a new DiscardLeadCard instance.
     *
     * @param index the index of the hand leader card to discard
     */
    public DiscardLeadCard(int index) {
        this.index = index;
        this.hand = null;
        this.type = UserAction.DISCARD_LEADCARD;
    }

    /**
     * Discards the leader card from the player's hand and updates the player's position
     * on the itinerary by 1.
     *
     * @param player the player performing the action
     * @return {@code false}
     */
    @Override
    public boolean doAction(Player player) {
        player.discardLeadCard(index - 1);
        player.getBoard().getItinerary().updatePosition(1, null,
                player.getBoard().getItinerary().toNotify(player.getBoard().getItinerary().getPosition(), 1));
        return false;
    }

    @Override
    public UserAction getType() {
        return type;
    }

    /**
     * Checks if the provided index is valid.
     *
     * @param player the player who wants to perform the action
     * @throws WrongActionException if the index is not valid
     */
    @Override
    public void checkAction(Player player) throws WrongActionException {
        hand = player.getHandLeaderCards();
        if (index <= 0 || index > hand.size())
            throw new WrongActionException("The specified index is out of bounds. ");
    }
}
