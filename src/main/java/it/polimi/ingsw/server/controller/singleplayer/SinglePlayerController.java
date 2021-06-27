package it.polimi.ingsw.server.controller.singleplayer;

import it.polimi.ingsw.messages.serverMessages.CloseMessage;
import it.polimi.ingsw.messages.serverMessages.newElement.NewPlayers;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.server.controller.GamePhase;
import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.gameboard.GameBoard;
import it.polimi.ingsw.server.serverNetwork.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SinglePlayerController extends GameController {
    List<SoloActionToken> tokens;
    boolean win;
    boolean lastAction;

    /**
     * Default constructor. In addition to a generic game controller constructor,
     * it creates the list of solo action tokens that will be used by Lorenzo il
     * Magnifico.
     *
     * @param server the server that is hosting the game.
     */
    public SinglePlayerController(Server server) {
        super(server);
        lastAction = false;
        tokens = new ArrayList<>();
        tokens.add(new UpdateAndShuffle(this));
        tokens.add(new UpdateItinerary(this));
        for (Colour c : Colour.values()) {
            tokens.add(new DiscardDevCard(this, c));
        }
        Collections.shuffle(tokens);
    }

    /**
     * Send all view elements to a player. Used when a player is trying
     * to reconnect to the game.
     *
     * @param nickname the player that is reconnecting to the game
     */
    @Override
    public void sendReloadedView(String nickname){
        reloadView(nickname);
        Player p = getGame().getPlayerByNickname(nickname);
        p.setActionDone(UserAction.RECONNECT_DISPOSITION);
    }

    /**
     * Returns the list of solo action tokens.
     *
     * @return the list of solo action tokens
     */
    public List<SoloActionToken> getTokens() {
        return tokens;
    }

    /**
     * Makes Lorenzo il Magnifico perform an action based on the first token of the list.
     * It checks if any papal report is triggered and checks if the end game conditions are met.
     * It then passes the turn to the player.
     *
     */
    public void makeTokenAction() {
        tokens.add(tokens.remove(0));
        UserAction actionType = tokens.get(tokens.size() - 1).doSoloAction();
        checkAllPapalReports();
        checkEndGame();
        if (getPhase()!= GamePhase.ENDED)
            getActivePlayers().get(0).setLorenzoActionDone(actionType);
        getGame().getPlayers().get(0).setExclusiveActionDone(false);
    }

    /**
     * Executes the action performed by the player and sets his actionDone and
     * exclusiveActionDone attributes. It checks if any papal report is triggered
     * and checks if the end game conditions are met.
     *
     * @param action the action performed by the player
     */
    @Override
    public void makeAction(Action action) {
        boolean actionDone = action.doAction(getActivePlayers().get(0));
        if (actionDone) getActivePlayers().get(0).setExclusiveActionDone(true);
        checkAllPapalReports();
        checkEndGame();
        if (getPhase()!= GamePhase.ENDED)
            getActivePlayers().get(0).setActionDone(action.getType());
    }

    /**
     * Checks if the end game conditions are met, if so it calls
     * the {@link #endGame()} method.
     *
     */
    @Override
    public void checkEndGame() {
        List<DevDeck> d;
        for (Colour c : Colour.values()) {
            d = new ArrayList<>(Arrays.asList(getGame().getDevDecks()));
            d.removeIf(DevDeck -> (DevDeck.isEmpty() || DevDeck.getColour() != c));
            if (d.isEmpty()) {
                lastAction = true;
                win = false;
                endGame();
            }
        }
        GameBoard board = getActivePlayers().get(0).getBoard();
        if (board.getItinerary().getBlackCrossPosition() == 24) {
            lastAction = true;
            win = false;
            endGame();
        }
        if (board.getItinerary().getPosition() == 24 || board.getDevSpace().countCards() == 7) {
            lastAction = true;
            win = true;
            endGame();
        }
    }

    /**
     * Sets the game phase to SETUP and sets the black cross position. It then notifies
     * the player of the development decks and starts the draw setup phase.
     *
     */
    @Override
    public void setup() {
        setPhase(GamePhase.SETUP);
        getActivePlayers().get(0).getBoard().getItinerary().setBlackCrossPosition(0);
        getGame().getMarket().notifyNew();
        for (DevDeck d : getGame().getDevDecks()){
            d.notifyNew();
        }
        getActivePlayers().get(0).setActionDone(UserAction.INITIAL_DISPOSITION);
        initialDraw();
    }

    /**
     * Draws four leader cards for the player and calls the
     * {@link Player#setActionDone(UserAction)} method on him.
     *
     */
    private void initialDraw(){
        getActivePlayers().get(0).setupDraw();
        getActivePlayers().get(0).setActionDone(UserAction.SETUP_DRAW);
    }

    /**
     * Removes the leader cards from the player's hand that he
     * has chosen to discard during the draw setup phase and
     * starts the game.
     *
     * @param indexes the index of the two leader cards to discard
     */
    public void initialDiscardLeader(int[] indexes){
        getActivePlayers().get(0).setupDiscard(indexes[0], indexes[1], 0);
        getActivePlayers().get(0).setActionDone(UserAction.SELECT_LEADCARD);
        startMatch();
    }

    /**
     * Sets the game phase to STARTED and activates the first turn of the game.
     *
     */
    private void startMatch(){
        setPhase(GamePhase.STARTED);
        getActivePlayers().get(0).setTurnActive(true , false, null);
    }

    /**
     * Sends a {@link CloseMessage} to the player. If the players has won, it
     * computes his victory points and sends them in the message. It then
     * removes this controller from the server.
     *
     */
    @Override
    public void endGame() {
        getActivePlayers().get(0).setActionDone(UserAction.LAST_ACTION);
        if (!win) {
            getActiveConnections().get(0).sendSocketMessage(new CloseMessage("You lost the game: Lorenzo has won!"));
        } else {
            int score = calculateScore(getActivePlayers().get(0));
            getActiveConnections().get(0).sendSocketMessage(new CloseMessage("You won the game! Your score is " + score));
        }
        setPhase(GamePhase.ENDED);
        synchronized (getServer()) {
            getServer().removeGame(this);
        }
    }

    /**
     * Computes the score for the player.
     *
     * @param player the player
     * @return the computed score
     */
    private int calculateScore(Player player) {
        int score = addItineraryVP(player) + addPapalVP(player) + addLeaderVP(player) + addDevCardVP(player);
        score += player.getBoard().getTotalResources() / 5;
        return score;
    }
}
