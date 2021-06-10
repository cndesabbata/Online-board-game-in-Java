package it.polimi.ingsw.server.controller.multiplayer;

import it.polimi.ingsw.messages.serverMessages.CloseMessage;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.server.controller.GamePhase;
import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.serverNetwork.ClientConnection;
import it.polimi.ingsw.server.serverNetwork.Server;

import java.util.*;
import java.util.stream.Collectors;

public class MultiPlayerController extends GameController {
    private Player currentPlayer;
    private int currentPlayerIndex;

    public MultiPlayerController(Server server) {
        super(server);
    }


    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void changeTurn() {
        String oldPlayer = currentPlayer.getNickname();
        currentPlayer.setTurnActive(false, true, oldPlayer);
        currentPlayer.setExclusiveActionDone(false);
        if (getGame().isFinalTurn() && currentPlayerIndex == getActivePlayers().size() - 1) {
            endGame();
        } else {
            if (getPhase() == GamePhase.SETUP) oldPlayer = null;
            Player nextPlayer = nextPlayer();
            nextPlayer.setTurnActive(true, getPhase() == GamePhase.SETUP, oldPlayer);
            currentPlayer = nextPlayer;
        }
    }

    @Override
    public void sendReloadedView(String nickname) {
        reloadView(nickname);
        Player p = getGame().getPlayerByNickname(nickname);
        p.setActionDone(UserAction.RECONNECT_DISPOSITION);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void makeAction(Action action) {
        boolean actionDone = action.doAction(currentPlayer);
        if (actionDone) currentPlayer.setExclusiveActionDone(true);
        checkAllPapalReports();
        checkEndGame();
        currentPlayer.setActionDone(action.getType());
    }

    private Player nextPlayer() {
        if (currentPlayerIndex == getActivePlayers().size() - 1) {
            if (getPhase() == GamePhase.SETUP) setPhase(GamePhase.STARTED);
            currentPlayerIndex = 0;
            return getActivePlayers().get(0);
        } else {
            currentPlayerIndex++;
            return getActivePlayers().get(currentPlayerIndex);
        }
    }

    @Override
    public void checkEndGame() {
        if (currentPlayer.getBoard().getItinerary().getPosition() == 24 ||
                currentPlayer.getBoard().getDevSpace().countCards() == 7) {
            getGame().setFinalTurn(true);
        }
    }

    public void setup() {
        setPhase(GamePhase.SETUP);
        Collections.shuffle(getActivePlayers());
        currentPlayer = getActivePlayers().get(0);
        currentPlayerIndex = 0;
        getGame().getMarket().notifyNew();
        for (DevDeck d : getGame().getDevDecks()) {
            d.notifyNew();
        }
        getGame().notifyNewPlayers(getActivePlayers());
        currentPlayer.setActionDone(UserAction.INITIAL_DISPOSITION);
        initialDraw();
    }

    private void initialDraw() {
        currentPlayer.setupDraw();
        currentPlayer.setActionDone(UserAction.SETUP_DRAW);
    }

    public void initialDiscardLeader(int[] indexes) {
        currentPlayer.setupDiscard(indexes[0], indexes[1], currentPlayerIndex);
        currentPlayer.setActionDone(UserAction.SELECT_LEADCARD);
        if (currentPlayerIndex == 0) {                                                                                   //the first player will not receive any resources, hence, after having sent the new LeadCards to him, we can move on to the next player.
            changeTurn();
            initialDraw();
        }
    }

    public void addInitialResources(List<ResourcePosition> rps) {
        currentPlayer.getBoard().getWarehouse().incrementResource(rps);
        if (currentPlayerIndex == 2 || currentPlayerIndex == 3)
            currentPlayer.getBoard().getItinerary().updatePosition(1, null, true);
        currentPlayer.setActionDone(UserAction.RESOURCE_SELECTION);
        changeTurn();
        if (getPhase() == GamePhase.SETUP) initialDraw();
    }

    /* computes victory points for every player and sets the game winner */
    @Override
    public void endGame() {
        Game game = getGame();
        Map<String, Integer> playersPoints = new HashMap<>();
        for (Player player : game.getPlayers()) {
            String nickname = player.getNickname();
            playersPoints.put(nickname, 0);
            playersPoints.put(nickname, playersPoints.get(nickname) + addItineraryVP(player));
            playersPoints.put(nickname, playersPoints.get(nickname) + addDevCardVP(player));
            playersPoints.put(nickname, playersPoints.get(nickname) + addPapalVP(player));
            playersPoints.put(nickname, playersPoints.get(nickname) + addLeaderVP(player));
            playersPoints.put(nickname, playersPoints.get(nickname) + player.getBoard().getTotalResources() / 5);
        }
        int maxPoints = Collections.max(playersPoints.values());
        List<String> potentialWinners = playersPoints.entrySet().stream()
                .filter(entry -> entry.getValue() == maxPoints)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
        Map<String, Integer> potentialWinnersResources = new HashMap<>();
        for (String playerNickname : potentialWinners) {
            potentialWinnersResources.put(playerNickname, game.getPlayerByNickname(playerNickname).getBoard().getTotalResources());
        }
        int maxResources = Collections.max(potentialWinnersResources.values());
        List<String> winnersNickname = potentialWinnersResources.entrySet().stream()
                .filter(entry -> entry.getValue() == maxResources)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
        List<Player> winners = new ArrayList<>();
        for (String winnerNickname : winnersNickname) {
            winners.add(game.getPlayerByNickname(winnerNickname));
        }
        game.setWinners(winners);
        for(ClientConnection connection : getActiveConnections()) {
            if(winners.contains(getGame().getPlayerByNickname(connection.getPlayerNickname())))
                connection.sendSocketMessage(new CloseMessage("You won the game! Your score is " + playersPoints.get(connection.getPlayerNickname())));
            else
                connection.sendSocketMessage(new CloseMessage("You lost the game! Your score is " + playersPoints.get(connection.getPlayerNickname())));
        }
    }
}