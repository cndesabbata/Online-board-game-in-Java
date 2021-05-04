package it.polimi.ingsw.server.controller.multiplayer;

import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.serverNetwork.Server;

import java.util.*;
import java.util.stream.Collectors;

public class MultiPlayerController extends GameController {
    private Player currentPlayer;
    private int currentPlayerIndex;

    public MultiPlayerController(Server server) {
        super(server);
    }

    public void changeTurn() {
        //String oldPlayer = currentPlayer.getNickname();
        currentPlayer.setTurnActive(false);
        currentPlayer.setExclusiveActionDone(false);
        if (getGame().isFinalTurn() && currentPlayerIndex == getActivePlayers().size() - 1) {
            endGame();
        } else {
            currentPlayer = nextPlayer();
            //if(isStarted() == "SETUP")
                currentPlayer.setTurnActive(true /*, true, oldPlayer*/);
            //else
                //currentPlayer.setTurnActive(true, /*false, oldPlayer*/);
        }
    }

    @Override
    public void makeAction(Action action) {
        boolean actionDone = action.doAction(currentPlayer);
        currentPlayer.setExclusiveActionDone(actionDone);
        checkAllPapalReports();
        checkEndGame();
    }

    private Player nextPlayer() {
        if (currentPlayerIndex == getActivePlayers().size() - 1) {
            //if(isStarted() == "IN CORSO")
            currentPlayerIndex = 0;
            return getActivePlayers().get(0);
            //else if(isStarted() == "SETUP" && currentPlayer.getActionDone == UserAction.SETUP_DISCARD)                                                                           //used when we have to pass from the last player that has to discard the leadcards to the first one that has to choose the initial resources
            //getActivePlayers().get(0).setActionDone = UserAction.REQUEST_RESOURCE_FAITHPOINT;
            //currentPlayerIndex = 1;
            //return getActivePlayers().get(1);
            //else if(isStarted() == "SETUP" && currentPlayer.getActionDone == UserAction.REQUEST_RESOURCE_FAITHPOINT)
            //currentPlayerIndex = 0;
            //setStarted("IN CORSO");
            //return getActivePlayers().get(0);
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

    public void setup(){
        //shuffle()
        //notifyObservers()                                                                                             //notify to all the virtual views the common parts of the model (market, devdecks)
        //add()
    }

    /*public void add(){
        currentPlayer.setupDraw();
        currentPlayer.setActionDone(UserAction.SETUP_DRAW);
    }*/

    /*public void remove(int index1, int index2){
        currentPlayer.setupDiscard(index1, index2);
        currentPlayer.setActionDone(UserAction.SETUP_DISCARD);
        changeTurn();
        if(currentPlayer.getActionDone() == null)
            add();
        else if(currentPlayer.getActionDone() == UserAction.SETUP_DISCARDED)
            currentPlayer.requestResource();
    }*/

    /*public void incrementResourceFaithpoint(List<ResourcePosition> rps){
        currentPlayer.getBoard().getWarehouse().incrementResource(rps);
        if(currentPlayerIndex == 2 || currentPlayerIndex == 3)
            currentPlayer.getBoard().getItinerary().updatePosition(1);
        currentPlayer.setActionDone(UserAction.SETUP_RESOURCE_FAITHPOINT);
        changeTurn();
        if(currentPlayer.getActionDone() == UserAction.SETUP_DISCARD)
            currentPlayer.requestResource();
        else if(currentPlayer.getActionDone() == UserAction.SETUP_RESOURCE_FAITHPOINT)
            currentPlayer.requestFirstTurn();
    }*/

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
    }
}