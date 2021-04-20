package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Player {
    private final Game game;
    private final String nickname;
    private final GameBoard board;
    private final ArrayList<LeaderCard> handLeaderCards;
    private boolean actionDone;
    private boolean turnActive;

    /* constructor Player creates a new Player instance with a given nickname */
    public Player(String nickname, GameBoard board, Game game) {
        this.board = board;
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

    public ArrayList<LeaderCard> getHandLeaderCards() { return handLeaderCards; }

    public boolean hasPlayedLeaderCard(LeaderType type, Resource resource) {
        for (LeaderCard leaderCard : handLeaderCards) {
            if (leaderCard.getType() == type && leaderCard.getResource() == resource && leaderCard.isPlayed())
                return true;
        }
        return false;
    }
}
