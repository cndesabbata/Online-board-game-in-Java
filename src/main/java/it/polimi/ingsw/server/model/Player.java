package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.gameboard.GameBoard;
import it.polimi.ingsw.server.observer.Observable;

import java.util.ArrayList;
import java.util.List;

public class Player extends Observable {
    private final Game game;
    private final String nickname;
    private final GameBoard board;
    private final List<LeaderCard> handLeaderCards;
    private boolean exclusiveActionDone;
    private boolean actionDone;
    private boolean turnActive;
    // private List<GameElement> changedElement;

    /* constructor Player creates a new Player instance with a given nickname */
    public Player(String nickname, Game game) {
        this.board = new GameBoard();
        this.nickname = nickname;
        this.game = game;
        this.actionDone = false;
        handLeaderCards = new ArrayList<>();
    }

    public boolean isActionDone() {
        return actionDone;
    }

    public void setActionDone(boolean actionDone) {
        this.actionDone = actionDone;
    }

    public String getNickname() {
        return nickname;
    }

    public Game getGame() { return game; }

    public GameBoard getBoard() {
        return board;
    }

    public boolean isExclusiveActionDone() {
        return exclusiveActionDone;
    }

    public boolean isTurnActive() {
        return turnActive;
    }

    public void setExclusiveActionDone(boolean exclusiveActionDone) {
        this.exclusiveActionDone = exclusiveActionDone;
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
