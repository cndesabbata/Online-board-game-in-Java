package it.polimi.ingsw.messages.serverMessages.newElement;

import it.polimi.ingsw.server.model.LeaderCard;

import java.util.List;

public class NewHandCards implements ChangeMessage{
    private final List<LeaderCard> handLeaderCards;
    private final String owner;
    private boolean mustDiscard;

    public NewHandCards(List<LeaderCard> handLeaderCards, String nickname, boolean mustDiscard) {
        this.handLeaderCards = handLeaderCards;
        this.owner = nickname;
        this.mustDiscard = mustDiscard;
    }

    public List<LeaderCard> getHandLeaderCards() {
        return handLeaderCards;
    }

    public String getOwner() {
        return owner;
    }
}
