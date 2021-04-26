package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final Game game;
    private final String nickname;
    private final Integer ID;
    private final GameBoard board;
    private final List<LeaderCard> handLeaderCards;
    private boolean actionDone;
    private boolean turnActive;

    /* constructor Player creates a new Player instance with a given nickname */
    public Player(String nickname, Integer ID, Game game) {
        this.board = new GameBoard();
        this.ID = ID;
        this.nickname = nickname;
        this.game = game;
        handLeaderCards = new ArrayList<>();
    }

    public String getNickname() {
        return nickname;
    }

    public Game getGame() { return game; }

    public GameBoard getBoard() {
        return board;
    }

    public boolean isActionDone() {
        return actionDone;
    }

    public boolean isTurnActive() {
        return turnActive;
    }

    public void setActionDone(boolean actionDone) {
        this.actionDone = actionDone;
    }

    public void setTurnActive(boolean turnActive) {
        this.turnActive = turnActive;
    }

    public List<LeaderCard> getHandLeaderCards() { return handLeaderCards; }

    public boolean hasPlayedLeaderCard(LeaderType type, Resource resource) {
        for (LeaderCard leaderCard : handLeaderCards) {
            if (leaderCard.getType() == type && leaderCard.getResource() == resource && leaderCard.isPlayed())
                return true;
        }
        return false;
    }

    public void addFakeLeaderCard(LeaderCard leaderCard) {
        handLeaderCards.add(leaderCard);
    }
}
