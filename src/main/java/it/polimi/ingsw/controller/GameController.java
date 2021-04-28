package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.messages.actions.Action;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.*;

import java.util.*;
import java.util.stream.Collectors;

public class GameController {
    private final Game game;
    private Player currentPlayer;
    private final Server server;
    private boolean actionDone;
    private boolean started;
    private final List<Player> activePlayers;
    private final List<ClientConnection> activeConnections;
    private int currentPlayerIndex;


    public GameController(Server server) {
        this.server = server;
        this.game = new Game();
        activePlayers = new ArrayList<>();
        activeConnections = new ArrayList<>();
    }

    public void setUpPlayer(ClientConnection connection) {
        Player newPlayer = new Player(connection.getPlayerNickname(), game);
        addActivePlayer(newPlayer);
        addActiveConnection(connection);
        game.addPlayer(newPlayer);
    }

    public List<ClientConnection> getActiveConnections() {
        return activeConnections;
    }

    public Game getGame() {
        return game;
    }

    public List<Player> getActivePlayers() {
        return activePlayers;
    }

    public void addActiveConnection(ClientConnection connection) {
        activeConnections.add(connection);
    }

    public void addActivePlayer(Player player) {
        activePlayers.add(player);
    }

    /* handles action messages from the current player */
    public void makeAction(Action action) {
        currentPlayer.setActionDone(action.doAction(currentPlayer));
        checkAllPapalReports();
        checkEndGame();
    }

    public boolean isStarted() {
        return started;
    }

    /* changes turn and sets the new current player */
    public void changeTurn() {
        currentPlayer.setTurnActive(false);
        currentPlayer.setActionDone(false);
        if (game.isFinalTurn() && currentPlayerIndex == activePlayers.size() - 1) {
            endGame();
        } else {
            currentPlayer = nextPlayer();
            currentPlayer.setTurnActive(true);
        }
    }

    /* returns the next current player */
    private Player nextPlayer() {
        if (currentPlayerIndex == activePlayers.size() - 1) {
            currentPlayerIndex = 0;
            return activePlayers.get(0);
        } else {
            currentPlayerIndex++;
            return activePlayers.get(currentPlayerIndex);
        }
    }

    /* checks if any papalReport needs to be triggered */
    private void checkAllPapalReports() {
        checkPapalReport(8, 5, 0);
        checkPapalReport(16, 12, 1);
        checkPapalReport(24, 19, 2);
    }

    /* checks if a specified papalReport needs to be triggered */
    private void checkPapalReport(int vaticanReportTrigger, int vaticanReportStart, int cardStatusIndex) {
        List<Player> players = game.getPlayers();

        for (Player player : players) {
            int position = player.getBoard().getItinerary().getPosition();
            CardStatus[] cardStatuses = player.getBoard().getItinerary().getCardStatus();
            if (position >= vaticanReportTrigger) {
                if (cardStatuses[cardStatusIndex] == CardStatus.FACE_DOWN) {
                    for (Player otherPlayer : players) {
                        int playerPosition = otherPlayer.getBoard().getItinerary().getPosition();
                        if (playerPosition >= vaticanReportStart)
                            otherPlayer.getBoard().getItinerary().setCardStatus(CardStatus.FACE_UP, 0);
                        else
                            otherPlayer.getBoard().getItinerary().setCardStatus(CardStatus.DISCARDED, 0);
                    }
                }
            }
        }
    }

    /* checks if requirements for the end game phase are matched */
    private void checkEndGame() {
        if (currentPlayer.getBoard().getItinerary().getPosition() == 24 ||
                currentPlayer.getBoard().getDevSpace().countCards() == 7) {
            game.setFinalTurn(true);
        }
    }


    public void setup() {
    }

    /* computes victory points for every player and sets the game winner */
    public void endGame() {
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
            addPapalCardPoints(playersPoints, player, 0, 2);
            addPapalCardPoints(playersPoints, player, 1, 3);
            addPapalCardPoints(playersPoints, player, 2, 4);
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

    /* private method called by endgame to compute victory points gained from itinerary position */
    private void addItineraryPoints(Map<String, Integer> playersPoints, Player player) {
        if (player.getBoard().getItinerary().getPosition() >= 3 && player.getBoard().getItinerary().getPosition() < 6)
            playersPoints.put(player.getNickname(), playersPoints.get(player.getNickname()) + 1);
        else if (player.getBoard().getItinerary().getPosition() >= 6 && player.getBoard().getItinerary().getPosition() < 9)
            playersPoints.put(player.getNickname(), playersPoints.get(player.getNickname()) + 2);
        else if (player.getBoard().getItinerary().getPosition() >= 9 && player.getBoard().getItinerary().getPosition() < 12)
            playersPoints.put(player.getNickname(), playersPoints.get(player.getNickname()) + 4);
        else if (player.getBoard().getItinerary().getPosition() >= 12 && player.getBoard().getItinerary().getPosition() < 15)
            playersPoints.put(player.getNickname(), playersPoints.get(player.getNickname()) + 6);
        else if (player.getBoard().getItinerary().getPosition() >= 15 && player.getBoard().getItinerary().getPosition() < 18)
            playersPoints.put(player.getNickname(), playersPoints.get(player.getNickname()) + 9);
        else if (player.getBoard().getItinerary().getPosition() >= 18 && player.getBoard().getItinerary().getPosition() < 21)
            playersPoints.put(player.getNickname(), playersPoints.get(player.getNickname()) + 12);
        else if (player.getBoard().getItinerary().getPosition() >= 21 && player.getBoard().getItinerary().getPosition() < 24)
            playersPoints.put(player.getNickname(), playersPoints.get(player.getNickname()) + 16);
        else if (player.getBoard().getItinerary().getPosition() >= 24)
            playersPoints.put(player.getNickname(), playersPoints.get(player.getNickname()) + 20);
    }

    /* private method called by endgame to compute victory points gained from papal cards */
    private void addPapalCardPoints(Map<String, Integer> playersPoints, Player player, int index, int victoryPoints) {
        CardStatus[] papalCardStatus = player.getBoard().getItinerary().getCardStatus();
        if (papalCardStatus[index] == CardStatus.FACE_UP)
            playersPoints.put(player.getNickname(), playersPoints.get(player.getNickname()) + victoryPoints);
    }
}