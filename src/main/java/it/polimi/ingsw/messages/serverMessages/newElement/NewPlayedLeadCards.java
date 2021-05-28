package it.polimi.ingsw.messages.serverMessages.newElement;

import it.polimi.ingsw.server.model.LeaderCard;

import java.util.List;

public class NewPlayedLeadCards implements ChangeMessage{
    private final List<LeaderCard> playedLeadCards;
    private final String nickname;

    public NewPlayedLeadCards(List<LeaderCard> playedLeadCards, String nickname) {
        this.playedLeadCards = playedLeadCards;
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public List<LeaderCard> getPlayedLeadCards() {
        return playedLeadCards;


    }
}
