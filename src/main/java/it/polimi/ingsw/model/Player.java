package it.polimi.ingsw.model;

public class Player {
    private final Game game;
    private final String nickname;
    private final GameBoard board;
    private boolean actionDone;
    private boolean turnActive;
    private LeaderCard[] handLeaderCards;

    /* constructor Player creates a new Player instance with a given nickname */
    public Player(String nickname, GameBoard board, Game game) {
        this.game = game;
        this.board = board;
        this.nickname = nickname;
        handLeaderCards = new LeaderCard[2];
    }

    public String getNickname() {
        return nickname;
    }

    public Game getGame() {
        return game;
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

    public boolean hasLeaderCard(String type, Resource resource) {
        for (LeaderCard leaderCard : handLeaderCards) {
            if (leaderCard.type.equals(type) && leaderCard.resource == resource)
                return true;
        }
        return false;
    }
}
