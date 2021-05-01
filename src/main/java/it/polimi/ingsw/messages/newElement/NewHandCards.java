package it.polimi.ingsw.messages.newElement;

import it.polimi.ingsw.server.model.LeaderCard;

import java.util.List;

public class NewHandCards implements ChangeMessage{
    private final List<LeaderCard> handLeaderCards;
    private final String nickname;

    public NewHandCards(List<LeaderCard> handLeaderCards, String nickname) {
        this.handLeaderCards = handLeaderCards;
        this.nickname = nickname;
    }

    public List<LeaderCard> getHandLeaderCards() {
        return handLeaderCards;
    }

    public String getNickname() {
        return nickname;
    }
}