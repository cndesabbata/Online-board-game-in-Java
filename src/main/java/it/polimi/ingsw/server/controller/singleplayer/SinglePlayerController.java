package it.polimi.ingsw.server.controller.singleplayer;

import it.polimi.ingsw.messages.clientMessages.LeaderCardSelection;
import it.polimi.ingsw.messages.serverMessages.CloseMessage;
import it.polimi.ingsw.messages.serverMessages.CustomMessage;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.messages.serverMessages.SetupMessage;
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

    public SinglePlayerController(Server server) {
        super(server);
        tokens = new ArrayList<>();
        tokens.add(new UpdateAndShuffle(this));
        tokens.add(new UpdateItinerary(this));
        for (Colour c : Colour.values()) {
            tokens.add(new DiscardDevCard(this, c));
        }
        Collections.shuffle(tokens);
    }

    public List<SoloActionToken> getTokens() {
        return tokens;
    }

    public void makeTokenAction() {
        tokens.add(tokens.remove(0));
        UserAction actionType = tokens.get(tokens.size() - 1).doSoloAction();
        getActivePlayers().get(0).setLorenzoActionDone(actionType);
        getGame().getPlayers().get(0).setExclusiveActionDone(false);
    }

    @Override
    public void makeAction(Action action) {
        boolean actionDone = action.doAction(getActivePlayers().get(0));
        if (actionDone) getActivePlayers().get(0).setExclusiveActionDone(true);
        checkAllPapalReports();
        checkEndGame();
        getActivePlayers().get(0).setActionDone(action.getType());
    }

    @Override
    public void checkEndGame() {
        List<DevDeck> d;
        for (Colour c : Colour.values()) {
            d = new ArrayList<>(Arrays.asList(getGame().getDevDecks()));
            d.removeIf(DevDeck -> (DevDeck.isEmpty() || DevDeck.getColour() != c));
            if (d.isEmpty()) {
                win = false;
                endGame();
            }
        }
        GameBoard board = getActivePlayers().get(0).getBoard();
        if (board.getItinerary().getBlackCrossPosition() == 24) {
            win = false;
            endGame();
        }
        if (board.getItinerary().getPosition() == 24 || board.getDevSpace().countCards() == 7) {
            win = true;
            endGame();
        }
    }

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

    private void initialDraw(){
        getActivePlayers().get(0).setupDraw();
        getActivePlayers().get(0).setActionDone(UserAction.SETUP_DRAW);
    }

    public void initialDiscardLeader(int[] indexes){
        getActivePlayers().get(0).setupDiscard(indexes[0], indexes[1], 0);
        getActivePlayers().get(0).setActionDone(UserAction.SELECT_LEADCARD);
        startMatch();
    }

    private void startMatch(){
        setPhase(GamePhase.STARTED);
        getActivePlayers().get(0).setTurnActive(true , false, null);
    }

    @Override
    public void endGame() {
        if (!win) {
            getActiveConnections().get(0).sendSocketMessage(new CloseMessage("You lost the game: Lorenzo has won!"));
        } else {
            int score = calculateScore(getActivePlayers().get(0));
            getActiveConnections().get(0).sendSocketMessage(new CloseMessage("You won the game! Your score is " + score));
        }
        setPhase(GamePhase.ENDED);
    }

    private int calculateScore(Player player) {
        int score = addItineraryVP(player) + addPapalVP(player) + addLeaderVP(player) + addDevCardVP(player);
        score += player.getBoard().getTotalResources() / 5;
        return score;
    }
}
