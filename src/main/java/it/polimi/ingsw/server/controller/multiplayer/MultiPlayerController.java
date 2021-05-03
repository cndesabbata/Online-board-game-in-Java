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
            //currentPlayerIndex = 1;
            //return getActivePlayers().get(1);
            //else if(isStarted() == "SETUP" && currentPlayer.getActionDone == UserAction.REQUEST_RESOURCE_FAITHPOINT)
            //currentPlayerIndex = 1;
            //setStarted("IN CORSO");
            //return getActivePlayers().get(1);
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
        changeTurn();
        if(currentPlayer.getActionDone() == null)
            add();
        else if(currentPlayer.getActionDone() == UserAction.SETUP_DRAW)
            currentPlayer.requestResource();
    }*/

    /*public void incrementResourceFaithpoint(ResourcePosition rp){
        List<ResourcePosition> rps = new ArrayList<ResourcePosition>();
        rps.add(rp);
        currentPlayer.getBoard().getWarehouse().incrementResource(rps);
        if(currentPlayerIndex == 2)
            currentPlayer.getBoard().getItinerary().updatePosition(1);
        else if(currentPlayerIndex == 3)
            currentPlayer.getBoard().getItinerary().updatePosition(2);
        currentPlayer.setActionDone(UserAction.SETUP_RESOURCE_FAITHPOINT);
        changeTurn();
        if(currentPlayer.getActionDone() == UserAction.SETUP_DRAW)
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
            List<List<DevCard>> playerDevCards = player.getBoard().getDevSpace().getCards();
            for (List<DevCard> devSlotCard : playerDevCards) {
                for (DevCard devCard : devSlotCard) {
                    playersPoints.put(nickname, playersPoints.get(nickname) + devCard.getVictoryPoints());
                }
            }
            addItineraryPoints(playersPoints, player);
            addPapalCardPoints(playersPoints, player);
            for (LeaderCard leaderCard : player.getHandLeaderCards()) {
                if (leaderCard.isPlayed())
                    playersPoints.put(nickname, playersPoints.get(nickname) + leaderCard.getVictoryPoints());
            }
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

    /* private method called by endgame to compute victory points gained from itinerary */
    private void addItineraryPoints(Map<String, Integer> playersPoints, Player player) {
        int[] itineraryVP = {1,2,4,6,9,12,16,20};
        for(int i = 3, j = 0; i <= 21; i = i + 3, j++) {
            if (player.getBoard().getItinerary().getPosition() >= i && player.getBoard().getItinerary().getPosition() < i + 3)
                playersPoints.put(player.getNickname(), playersPoints.get(player.getNickname()) + itineraryVP[j]);
        }
        if (player.getBoard().getItinerary().getPosition() == 24)
            playersPoints.put(player.getNickname(), playersPoints.get(player.getNickname()) + 20);
    }

    /* private method called by endgame to compute victory points gained from itinerary */
    private void addItineraryPoints(Map<String, Integer> playersPoints, Player player, int cell, int victoryPoints) {
        if (player.getBoard().getItinerary().getPosition() >= cell && player.getBoard().getItinerary().getPosition() < cell + 3)
            playersPoints.put(player.getNickname(), playersPoints.get(player.getNickname()) + victoryPoints);
    }

    /* private method called by endgame to compute victory points gained from papal cards */
    private void addPapalCardPoints(Map<String, Integer> playersPoints, Player player) {
        CardStatus[] papalCardStatus = player.getBoard().getItinerary().getCardStatus();
        for (int i = 0; i < 3; i++){
            if (papalCardStatus[i] == CardStatus.FACE_UP)
                playersPoints.put(player.getNickname(), playersPoints.get(player.getNickname()) + i + 2);
        }
    }
}