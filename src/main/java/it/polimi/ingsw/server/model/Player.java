package it.polimi.ingsw.server.model;

import it.polimi.ingsw.messages.serverMessages.newElement.NewHandCards;
import it.polimi.ingsw.messages.serverMessages.ChangesDone;
import it.polimi.ingsw.messages.serverMessages.TurnChange;
import it.polimi.ingsw.messages.serverMessages.newElement.NewPlayedLeadCards;
import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.model.gameboard.GameBoard;
import it.polimi.ingsw.server.observer.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Player represents a player of the game.
 *
 */
public class Player extends Observable {
    private final Game game;
    private final String nickname;
    private final GameBoard board;
    private final List<LeaderCard> handLeaderCards;
    private final List<LeaderCard> playedLeaderCards;
    private boolean exclusiveActionDone;
    private UserAction actionDone;
    private boolean turnActive;

    /**
     * Creates a new Player instance.
     *
     * @param nickname the player's nickname
     * @param game     the game the player has joined
     */
    public Player(String nickname, Game game) {
        this.board = new GameBoard(nickname);
        this.nickname = nickname;
        this.game = game;
        handLeaderCards = new ArrayList<>();
        playedLeaderCards = new ArrayList<>();
    }

    /**
     * Sets the actionDone attribute. It notifies the player's virtual view
     * with a ChangesDone message if the action was a setup one, otherwise
     * it notifies every player's VirtualView.
     *
     * @param actionDone the last action of the player
     */
    public void setActionDone(UserAction actionDone) {
        this.actionDone = actionDone;
        if (actionDone == UserAction.SETUP_DRAW || actionDone == UserAction.SELECT_LEADCARD)
            notifySingleObserver(new ChangesDone(nickname, actionDone), nickname);
        else notifyObservers(new ChangesDone(nickname, actionDone));
    }

    /**
     * Returns the player's nickname.
     *
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Returns the game the player has joined.
     *
     * @return the game joined by the player
     */
    public Game getGame() {
        return game;
    }

    /**
     * Returns the board of the player.
     *
     * @return the board of the player
     */
    public GameBoard getBoard() {
        return board;
    }

    /**
     * Removes a leader card from the player's hand.
     *
     * @param index the index of the leader card to discard
     */
    public void discardLeadCard(int index){
        handLeaderCards.remove(index);
        notifySingleObserver(new NewHandCards(handLeaderCards, nickname, false), nickname);
    }

    /**
     * Removes a leader card from the player's hand and adds it to the played leader cards.
     *
     * @param index the index of the leader card to play
     */
    public void playLeadCard(int index){
        playedLeaderCards.add(handLeaderCards.remove(index));
        notifySingleObserver(new NewHandCards(handLeaderCards, nickname, false), nickname);
        notifyObservers(new NewPlayedLeadCards(playedLeaderCards, nickname));
    }

    /**
     * Notifies a player's virtual view with the played leader cards of
     * this Player object. It also notifies the hand leader cards if the nickname provided
     * is the one on this Player Object.
     *
     * @param nickname the notified player's nickname
     */
    public void notifyNew (String nickname){
        notifySingleObserver(new NewPlayedLeadCards(playedLeaderCards, nickname), nickname);
        if(nickname.equalsIgnoreCase(this.nickname))
            notifySingleObserver(new NewHandCards(handLeaderCards, nickname, false), nickname);
    }

    /**
     * Returns the exclusiveActionDone attribute.
     *
     * @return {@code true} if the player has already performed a exclusive action, {@code false} otherwise
     */
    public boolean isExclusiveActionDone() {
        return exclusiveActionDone;
    }

    public synchronized boolean isTurnActive() {
        return turnActive;
    }

    /**
     * Sets the exclusiveActionDone attribute.
     *
     * @param exclusiveActionDone the new exclusiveActionDone value
     */
    public void setExclusiveActionDone(boolean exclusiveActionDone) {
        this.exclusiveActionDone = exclusiveActionDone;
    }

    /**
     * Sets the turnActive attribute for this Player object. It notifies
     * all the virtual views with a TurnChange message if the game is not
     * in the setup phase and the turnActive parameter is set to true.
     *
     * @param turnActive the new turnActive value
     * @param gameSetup  true if the game is in setup phase, false otherwise
     * @param oldPlayer  the nickname of the player that has ended his turn
     */
    public synchronized void setTurnActive(boolean turnActive , boolean gameSetup, String oldPlayer) {
        if(!gameSetup && turnActive)
            notifyObservers(new TurnChange(nickname, oldPlayer));
        this.turnActive = turnActive;
    }

    /**
     * It notifies the virtual view of the player with a {@link ChangesDone} message.
     * Used in single player mode only.
     *
     * @param action the last action performed by Lorenzo De Medici
     */
    public void setLorenzoActionDone(UserAction action){
        notifyObservers(new ChangesDone("Lorenzo De Medici ", action));
    }

    /**
     * Returns the hand leader cards of the player.
     *
     * @return the hand leader cards of the player
     */
    public List<LeaderCard> getHandLeaderCards() {
        return handLeaderCards;
    }

    /**
     * Returns the played leader cards of the player.
     *
     * @return the played leader cards of the player
     */
    public List<LeaderCard> getPlayedLeaderCards() {
        return playedLeaderCards;
    }

    /**
     * Checks if the player has played a specified leader card.
     *
     * @param type     the type of the leader card
     * @param resource the resource associated with the leader card
     * @return {@code true} if the player has played the specified card, {@code false} otherwise
     */
    public boolean hasPlayedLeaderCard(LeaderType type, Resource resource) {
        return playedLeaderCards.stream().anyMatch(Lc -> Lc.getResource() == resource && Lc.getType() == type);
    }

    /**
     * Adds four leader cards to the player's hand and notifies his virtual view
     * with a NewHandCards message. Used in the setup phase.
     *
     */
    public void setupDraw(){
        for (int i = 0; i < 4; i++) {
            handLeaderCards.add(getGame().getLeaderDeck().drawCard());
        }
        notifySingleObserver(new NewHandCards(handLeaderCards, nickname, true), nickname);
    }

    /**
     * Removes two leader cards from the player's hand and notifies his virtual
     * view with a NewHandCard message. Used in the setup phase.
     *
     * @param index1      the index of the first leader card to discard
     * @param index2      the index of the second leader card to discard
     * @param playerIndex the player's index
     */
    public void setupDiscard(int index1, int index2, int playerIndex){
        LeaderCard leaderCard1 = handLeaderCards.get(index1 - 1);
        LeaderCard leaderCard2 = handLeaderCards.get(index2 - 1);
        handLeaderCards.remove(leaderCard1);
        handLeaderCards.remove(leaderCard2);
        notifySingleObserver(new NewHandCards(handLeaderCards, nickname, false), nickname);
    }

    /**
     * Returns the last action performed by the player.
     *
     * @return the last action performed by the player
     */
    public UserAction getActionDone() {
        return actionDone;
    }
}
