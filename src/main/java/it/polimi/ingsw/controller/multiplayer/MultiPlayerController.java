package it.polimi.ingsw.controller.multiplayer;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.messages.actions.Action;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.*;

import java.util.*;
import java.util.stream.Collectors;

public class MultiPlayerController extends GameController {
    private Player currentPlayer;
    private int currentPlayerIndex;

    public MultiPlayerController(Server server) {
        super(server);
    }

    public void changeTurn() {
        currentPlayer.setTurnActive(false);
        currentPlayer.setActionDone(false);
        if (getGame().isFinalTurn() && currentPlayerIndex == getActivePlayers().size() - 1) {
            endGame();
        } else {
            currentPlayer = nextPlayer();
            currentPlayer.setTurnActive(true);
        }
    }

    @Override
    public void makeAction(Action action) {
        currentPlayer.setActionDone(action.doAction(currentPlayer));
        checkAllPapalReports();
        checkEndGame();
    }

    private Player nextPlayer() {
        if (currentPlayerIndex == getActivePlayers().size() - 1) {
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

    public void setup(){
    }

    /* computes victory points for every player and sets the game winner */
    @Override
    public void endGame() {
        Map<String, Integer> playersPoints = new HashMap<>();
        for (Player player : getGame().getPlayers()) {
            String nickname = player.getNickname();
            playersPoints.put(nickname, 0);
            List<List<DevCard>> playerDevCards = player.getBoard().getDevSpace().getCards();
            for (List<DevCard> devSlotCard : playerDevCards) {
                for (DevCard devCard : devSlotCard) {
                    playersPoints.put(nickname, playersPoints.get(nickname) + devCard.getVictoryPoints());
                }
            }
            int itineraryVP[] = {1,2,4,6,9,12,16,20};
            for (int i = 0; i < itineraryVP.length; i++){
                addItineraryPoints(playersPoints, player, (i+1)*3, itineraryVP[i]);
            }
            /*
            addItineraryPoints(playersPoints, player, 3, 1);
            addItineraryPoints(playersPoints, player, 6, 2);
            addItineraryPoints(playersPoints, player, 9, 4);
            addItineraryPoints(playersPoints, player, 12, 6);
            addItineraryPoints(playersPoints, player, 15, 9);
            addItineraryPoints(playersPoints, player, 18, 12);
            addItineraryPoints(playersPoints, player, 21, 16);
            addItineraryPoints(playersPoints, player, 24, 20);*/
            for (int i = 0; i < 3; i++){
                addPapalCardPoints(playersPoints, player, i, i+2);
            }
            /*
            addPapalCardPoints(playersPoints, player, 0, 2);
            addPapalCardPoints(playersPoints, player, 1, 3);
            addPapalCardPoints(playersPoints, player, 2, 4);*/
            for (LeaderCard leaderCard : player.getHandLeaderCards()) {
                if (leaderCard.isPlayed())
                    playersPoints.put(nickname, playersPoints.get(nickname) + leaderCard.getVictoryPoints());
            }
            playersPoints.put(nickname, playersPoints.get(nickname) + player.getBoard().getTotalResources());
            int max = Collections.max(playersPoints.values());
            List<String> potentialWinners = playersPoints.entrySet().stream()
                    .filter(entry -> entry.getValue() == max)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            Map<String, Integer> potentialWinnersResources = new HashMap<>();
            for (String playerNickname : potentialWinners) {
                potentialWinnersResources.put(playerNickname, getGame().getPlayerByNickname(playerNickname).getBoard().getTotalResources());
            }
            List<String> winnersNickname = potentialWinnersResources.entrySet().stream()
                    .filter(entry -> entry.getValue() == max)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            List<Player> winners = new ArrayList<>();
            for (String winnerNickname : winnersNickname) {
                winners.add(getGame().getPlayerByNickname(winnerNickname));
            }
//            game.setWinners(winners);
        }
    }

    /* private method called by endgame to compute victory points gained from itinerary */
    private void addItineraryPoints(Map<String, Integer> playersPoints, Player player, int cell, int victoryPoints) {
        if (player.getBoard().getItinerary().getPosition() >= cell)
            playersPoints.put(player.getNickname(), playersPoints.get(player.getNickname()) + victoryPoints);
    }

    /* private method called by endgame to compute victory points gained from papal cards */
    private void addPapalCardPoints(Map<String, Integer> playersPoints, Player player, int index, int victoryPoints) {
        CardStatus[] papalCardStatus = player.getBoard().getItinerary().getCardStatus();
        if (papalCardStatus[index] == CardStatus.FACE_UP)
            playersPoints.put(player.getNickname(), playersPoints.get(player.getNickname()) + victoryPoints);
    }
}