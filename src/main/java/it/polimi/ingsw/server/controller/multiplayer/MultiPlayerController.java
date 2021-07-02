package it.polimi.ingsw.server.controller.multiplayer;

import it.polimi.ingsw.messages.serverMessages.CloseMessage;
import it.polimi.ingsw.messages.serverMessages.newElement.NewPlayers;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.server.controller.GamePhase;
import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.serverNetwork.ClientConnection;
import it.polimi.ingsw.server.serverNetwork.Server;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class Multiplayer controller represents a game controller that manages
 * multiplayer games.
 *
 */
public class MultiPlayerController extends GameController {
    private Player currentPlayer;
    private int currentPlayerIndex;

    /**
     * Default constructor.
     *
     * @param server the server that is hosting the game
     */
    public MultiPlayerController(Server server) {
        super(server);
    }

    /**
     * Sets the index of the current player.
     *
     * @param currentPlayerIndex the index of the current player
     */
    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    /**
     * Returns the index of the current player.
     *
     * @return the index of the current player
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Ends the turn of the current player and starts the turn of the next player.
     * If the player was the last of the final turn of the game, it calls the
     * {@link #endGame()} method.
     *
     */
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

    /**
     * Send all view elements to a player. Used when a player is trying
     * to reconnect to the game.
     *
     * @param nickname the player that is reconnecting to the game
     */
    @Override
    public void sendReloadedView(String nickname) {
        reloadView(nickname);
        Player p = getGame().getPlayerByNickname(nickname);
        p.setActionDone(UserAction.RECONNECT_DISPOSITION);
    }

    /**
     * Returns the current player.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
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
        boolean actionDone = action.doAction(currentPlayer);
        if (actionDone) currentPlayer.setExclusiveActionDone(true);
        checkAllPapalReports();
        checkEndGame();
        currentPlayer.setActionDone(action.getType());
    }

    /**
     * Returns the next player.
     *
     * @return the next player
     */
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

    /**
     * Checks if the end game conditions are met.
     *
     */
    @Override
    public void checkEndGame() {
        if (currentPlayer.getBoard().getItinerary().getPosition() == 24 ||
                currentPlayer.getBoard().getDevSpace().countCards() == 7) {
            getGame().setFinalTurn(true);
        }
    }

    /**
     * Sets the game phase to SETUP, picks a random player as sets him as first. Notifies each
     * player of the development decks and send them a {@link NewPlayers} message.
     * It then starts the draw setup phase.
     *
     */
    public void setup() {
        setPhase(GamePhase.SETUP);
        Collections.shuffle(getActivePlayers());
        currentPlayer = getActivePlayers().get(0);
        currentPlayerIndex = 0;
        getGame().getMarket().notifyNew();
        for (DevDeck d : getGame().getDevDecks()) {
            d.notifyNew();
        }
        for(Player p : getActivePlayers()){
            getGame().notifyNewPlayers(getActivePlayers(), p.getNickname());
        }
        currentPlayer.setActionDone(UserAction.INITIAL_DISPOSITION);
        initialDraw();
    }

    /**
     * Draws four leader cards for the current player and calls the
     * {@link Player#setActionDone(UserAction)} method on him.
     *
     */
    private void initialDraw() {
        currentPlayer.setupDraw();
        currentPlayer.setActionDone(UserAction.SETUP_DRAW);
    }

    /**
     * Removes the leader cards from the current player's hand that he
     * has chosen to discard during the draw setup phase.
     *
     * @param indexes the index of the two leader cards to discard
     */
    public void initialDiscardLeader(int[] indexes) {
        currentPlayer.setupDiscard(indexes[0], indexes[1], currentPlayerIndex);
        currentPlayer.setActionDone(UserAction.SELECT_LEADCARD);
        if (currentPlayerIndex == 0) {                                                                                   //the first player will not receive any resources, hence, after having sent the new LeadCards to him, we can move on to the next player.
            changeTurn();
            initialDraw();
        }
    }

    /**
     * Add the initial resources the player has chosen in the setup phase.
     *
     * @param rps the resources chosen by the player
     */
    public void addInitialResources(List<ResourcePosition> rps) {
        currentPlayer.getBoard().getWarehouse().incrementResource(rps);
        if (currentPlayerIndex == 2 || currentPlayerIndex == 3)
            currentPlayer.getBoard().getItinerary().updatePosition(1, null, true);
        currentPlayer.setActionDone(UserAction.RESOURCE_SELECTION);
        changeTurn();
        if (getPhase() == GamePhase.SETUP) initialDraw();
    }

    /**
     * Computes victory points for every player and sets the game winner,
     * sending a {@link CloseMessage} to all players. It then removes this
     * controller from the server.
     *
     */
    @Override
    public void endGame() {
        setPhase(GamePhase.ENDED);
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
        synchronized (getServer()) {
            getServer().removeGame(this);
        }
    }
}