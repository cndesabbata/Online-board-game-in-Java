package it.polimi.ingsw.model;

public class Player {
    private final Game game;
    private final String nickname;
    private final GameBoard board;
    private LeaderCard[] handLeaderCards;
    private boolean actionAlreadyDone = false;

    /* constructor Player creates a new Player instance with a given nickname */
    public Player(String nickname, GameBoard board, Game game) {
        this.board = board;
        this.nickname = nickname;
        this.game = game;
        handLeaderCards = new LeaderCard[2];
    }

    public String getNickname() {
        return nickname;
    }

    public Game getGame() { return game; }

    public GameBoard getBoard() {
        return board;
    }

    public void setActionAlreadyDone(boolean actionAlreadyDone) { this.actionAlreadyDone = actionAlreadyDone; }

    public LeaderCard[] getHandLeaderCards() { return handLeaderCards; }

    public LeaderCard[] getLeaderCards() {
        LeaderCard[] tempHand = new LeaderCard[2];
        System.arraycopy(handLeaderCards, 0, tempHand, 0, 2);
        return tempHand;
    }

    public void setLeaderCards(LeaderCard[] newHand) {
        System.arraycopy(newHand, 0, handLeaderCards, 0, 2);
    }
}
