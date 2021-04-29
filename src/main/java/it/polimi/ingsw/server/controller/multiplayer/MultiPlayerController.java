package it.polimi.ingsw.server.controller.multiplayer;

import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.messages.actions.Action;
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
        currentPlayer.setTurnActive(false);
        currentPlayer.setExclusiveActionDone(false);
        if (getGame().isFinalTurn() && currentPlayerIndex == getActivePlayers().size() - 1) {
            endGame();
        } else {
            currentPlayer = nextPlayer();
            currentPlayer.setTurnActive(true);
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