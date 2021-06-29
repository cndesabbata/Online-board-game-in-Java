package it.polimi.ingsw.messages.serverMessages.newElement;

import it.polimi.ingsw.server.model.LeaderCard;

import java.util.List;

/**
 * Class NewHandCards is a {@link ChangeMessage} that contains an updated copy of
 * a player's hand leader cards.
 *
 */
public class NewHandCards implements ChangeMessage{
    private final List<LeaderCard> handLeaderCards;
    private final String owner;
    private boolean mustDiscard;

    /**
     * Creates a NewHandCards instance.
     *
     * @param handLeaderCards the new hand leader cards
     * @param nickname        the owner of the leader cards
     * @param mustDiscard     {@code true} if the cards must be discarded, {@code false} otherwise
     */
    public NewHandCards(List<LeaderCard> handLeaderCards, String nickname, boolean mustDiscard) {
        this.handLeaderCards = handLeaderCards;
        this.owner = nickname;
        this.mustDiscard = mustDiscard;
    }

    /**
     * Returns the new hand leader cards.
     *
     * @return the new hand leader cards.
     */
    public List<LeaderCard> getHandLeaderCards() {
        return handLeaderCards;
    }

    /**
     * Returns the owner of the leader cards.
     *
     * @return the owner of the leader cards
     */
    public String getOwner() {
        return owner;
    }
}
