package it.polimi.ingsw.model;

public class Player {
    private final String nickname;
    private final GameBoard board;
    private LeaderCard[] handLeaderCards;

    /* constructor Player creates a new Player instance with a given nickname */
    public Player(String nickname, GameBoard board) {
        this.board = board;
        this.nickname = nickname;
        handLeaderCards = new LeaderCard[2];
    }

    public String getNickname() {
        return nickname;
    }

    public GameBoard getBoard() {
        return board;
    }

    public LeaderCard[] getLeaderCards() {
        LeaderCard[] tempHand = new LeaderCard[2];
        System.arraycopy(handLeaderCards, 0, tempHand, 0, 2);
        return tempHand;
    }

    public void setLeaderCards(LeaderCard[] newHand) {
        System.arraycopy(newHand, 0, handLeaderCards, 0, 2);
    }
}
