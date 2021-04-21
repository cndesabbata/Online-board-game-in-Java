package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class GameController {
    private Game game;
    private Player currentPlayer;
    private GameHandler gameHandler;
    private boolean actionDone;

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void makeAction(Action action) {
        currentPlayer.setActionDone(action.doAction(currentPlayer));
        checkAllPapalReports();
        checkEndGame();
    }

    public void changeTurn() {
        currentPlayer.setTurnActive(false);
        currentPlayer.setActionDone(false);
        if (game.isFinalTurn() && game.getCurrentPlayerIndex() == game.getActivePlayers().size() - 1) {
            endGame();
        } else {
            currentPlayer = game.nextPlayer();
            currentPlayer.setTurnActive(true);
        }
    }

    /* checks if any papalReport needs to be triggered */
    private void checkAllPapalReports() {
        checkPapalReport(8, 5, 0);
        checkPapalReport(16, 12, 1);
        checkPapalReport(24, 19, 2);
    }

    /* checks if a specified papalReport needs to be triggered */
    private void checkPapalReport(int papalReportTrigger, int papalReportStart, int cardStatusIndex) {
        List<Player> players = gameHandler.getModel().getPlayers();

        for (Player player : players) {
            int position = player.getBoard().getItinerary().getPosition();
            CardStatus[] cardStatuses = player.getBoard().getItinerary().getCardStatus();
            if (position >= papalReportTrigger) {
                if (cardStatuses[cardStatusIndex] == CardStatus.FACE_DOWN) {
                    for (Player otherPlayer : players) {
                        int playerPosition = otherPlayer.getBoard().getItinerary().getPosition();
                        if (playerPosition >= papalReportStart)
                            otherPlayer.getBoard().getItinerary().setCardStatus(CardStatus.FACE_UP, 0);
                        else
                            otherPlayer.getBoard().getItinerary().setCardStatus(CardStatus.DISCARDED, 0);
                    }
                }
            }
        }
    }

    private void checkEndGame() {
        if (currentPlayer.getBoard().getItinerary().getPosition() == 24 ||
                currentPlayer.getBoard().getDevSpace().countDevCards() == 7) {

            game.setFinalTurn(true);
        }
    }

    /* computes victory points for every player and sets the game winner */
    public void endGame() {
        Map<String, Integer> playersPoints = new HashMap<>();
        for (Player player : game.getPlayers()) {
            String nickname = player.getNickname();
            playersPoints.put(nickname, 0);
            List<List<DevCard>> playerDevCards = player.getBoard().getDevSpace().getDevCards();
            for (List<DevCard> devSlotCard : playerDevCards) {
                for (DevCard devCard : devSlotCard) {
                    playersPoints.put(nickname, playersPoints.get(nickname) + devCard.getVictoryPoints());
                }
            }
            addItineraryPoints(playersPoints, player, 3, 1);
            addItineraryPoints(playersPoints, player, 6, 2);
            addItineraryPoints(playersPoints, player, 9, 4);
            addItineraryPoints(playersPoints, player, 12, 6);
            addItineraryPoints(playersPoints, player, 15, 9);
            addItineraryPoints(playersPoints, player, 18, 12);
            addItineraryPoints(playersPoints, player, 21, 16);
            addItineraryPoints(playersPoints, player, 24, 20);
            addPapalCardPoints(playersPoints, player, 0, 2);
            addPapalCardPoints(playersPoints, player, 1, 3);
            addPapalCardPoints(playersPoints, player, 2, 4);
            for (LeaderCard leaderCard : player.getHandLeaderCards()) {
                if (leaderCard.isPlayed())
                    playersPoints.put(nickname, playersPoints.get(nickname) + leaderCard.getVictoryPoints());
            }
            playersPoints.put(nickname, playersPoints.get(nickname) + player.getBoard().getTotalResources());
            int max = Collections.max(playersPoints.values());
            List<String> potentialWinners = playersPoints.entrySet().stream()
                    .filter(entry -> entry.getValue() == max)
                    .map(entry -> entry.getKey())
                    .collect(Collectors.toList());
            Map<String, Integer> potentialWinnersResources = new HashMap<>();
            for (String playerNickname : potentialWinners) {
                potentialWinnersResources.put(playerNickname, game.getPlayerByNickname(playerNickname).getBoard().getTotalResources());
            }
            List<String> winnersNickname = potentialWinnersResources.entrySet().stream()
                    .filter(entry -> entry.getValue() == max)
                    .map(entry -> entry.getKey())
                    .collect(Collectors.toList());
            List<Player> winners = new ArrayList<>();
            for (String winnerNickname : winnersNickname) {
                winners.add(game.getPlayerByNickname(winnerNickname));
            }
            game.setWinners(winners);
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