package it.polimi.ingsw.server.model;

import it.polimi.ingsw.messages.serverMessages.newElement.NewHandCards;
import it.polimi.ingsw.messages.serverMessages.newElement.NewIndex;
import it.polimi.ingsw.messages.serverMessages.ChangesDone;
import it.polimi.ingsw.messages.serverMessages.TurnChange;
import it.polimi.ingsw.server.controller.UserAction;
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
    private UserAction actionDone;
    private boolean turnActive;

    /* constructor Player creates a new Player instance with a given nickname */
    public Player(String nickname, Game game) {
        this.board = new GameBoard(nickname);
        this.nickname = nickname;
        this.game = game;
        handLeaderCards = new ArrayList<>();
    }

    public void setActionDone(UserAction actionDone) {
        this.actionDone = actionDone;
        notifyObservers(new ChangesDone(nickname, actionDone));
    }

    public String getNickname() {
        return nickname;
    }

    public Game getGame() { return game; }

    public GameBoard getBoard() {
        return board;
    }

    public void discardLeadCard(int index){
        handLeaderCards.remove(index);
        notifySingleObserver(new NewHandCards(handLeaderCards, nickname, false), nickname);
    }

    public void playLeadCard(int index){
        handLeaderCards.get(index).setPlayed(true);
        notifySingleObserver(new NewHandCards(handLeaderCards, nickname, false), nickname);
    }

    public boolean isExclusiveActionDone() {
        return exclusiveActionDone;
    }

    public synchronized boolean isTurnActive() {
        return turnActive;
    }

    public void setExclusiveActionDone(boolean exclusiveActionDone) {
        this.exclusiveActionDone = exclusiveActionDone;
    }

    public synchronized void setTurnActive(boolean turnActive , boolean gameSetup, String oldPlayer) {
        if(!gameSetup && turnActive)
            notifyObservers(new TurnChange(nickname, oldPlayer));
        this.turnActive = turnActive;
    }

    public void setLorenzoActionDone(UserAction action){
        notifyObservers(new ChangesDone("Lorenzo De Medici ", action));
    }

    public void addLeaderCard(LeaderCard card){
        handLeaderCards.add(card);
    }

    public List<LeaderCard> getHandLeaderCards() { return handLeaderCards; }

    public boolean hasPlayedLeaderCard(LeaderType type, Resource resource) {
        return handLeaderCards.stream().anyMatch(Lc -> Lc.getResource() == resource && Lc.getType() == type && Lc.isPlayed());
    }

   public void setupDraw(){
        for (int i = 0; i < 4; i++) {
            handLeaderCards.add(getGame().getLeaderDeck().drawCard());
        }
        notifySingleObserver(new NewHandCards(handLeaderCards, nickname, true), nickname);
    }

    public void setupDiscard(int index1, int index2, int playerIndex){
        handLeaderCards.remove(index1 - 1);
        handLeaderCards.remove(index2 - 1);
        notifySingleObserver(new NewHandCards(handLeaderCards, nickname, false), nickname);
        notifySingleObserver(new NewIndex(playerIndex), nickname);
    }

    public UserAction getActionDone() {
        return actionDone;
    }

}
