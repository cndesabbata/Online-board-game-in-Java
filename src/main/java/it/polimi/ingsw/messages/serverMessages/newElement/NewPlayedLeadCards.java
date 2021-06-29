package it.polimi.ingsw.messages.serverMessages.newElement;

import it.polimi.ingsw.server.model.LeaderCard;

import java.util.List;

/**
 * Class NewPlayedLeadCards is a {@link ChangeMessage} that contains the updated copy
 * of a player's played leader cards.
 *
 */
public class NewPlayedLeadCards implements ChangeMessage{
    private final List<LeaderCard> playedLeadCards;
    private final String nickname;

    /**
     * Creates a NewPlayedLeadCards instance.
     *
     * @param playedLeadCards the new list of played leader cards
     * @param nickname        the nickname of the player who owns the leader cards
     */
    public NewPlayedLeadCards(List<LeaderCard> playedLeadCards, String nickname) {
        this.playedLeadCards = playedLeadCards;
        this.nickname = nickname;
    }

    /**
     * Returns the owner of the leader cards.
     *
     * @return the owner of the leader cards
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Returns the new played leader cards.
     *
     * @return the new played leader cards.
     */
    public List<LeaderCard> getPlayedLeadCards() {
        return playedLeadCards;
    }
}
