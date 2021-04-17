package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Player {
    private final Game game;
    private final String nickname;
    private final GameBoard board;
    private final ArrayList<LeaderCard> handLeaderCards;
    private boolean actionAlreadyDone = false;

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

    public void setActionAlreadyDone(boolean actionAlreadyDone) { this.actionAlreadyDone = actionAlreadyDone; }

    public ArrayList<LeaderCard> getHandLeaderCards() { return handLeaderCards; }

}
