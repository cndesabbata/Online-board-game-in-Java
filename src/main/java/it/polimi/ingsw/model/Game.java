package it.polimi.ingsw.model;

import java.util.*;

public class Game {
    private final LeaderDeck leaderDeck;
    private final DevDeck[] devDecks;
    private final List<Player> players;
    private final List<Player> activePlayers;
    private Player currentPlayer;
    private List<Player> winners;
    private int currentPlayerIndex;
    private final Market market;
    private boolean finalTurn;

    public Game() {
        leaderDeck = new LeaderDeck();
        devDecks = new DevDeck[12];
        int j = 0;
        for (int i = 1; i <= 3; i++) {
            for (Colour colour : Colour.values()) {
                devDecks[j] = new DevDeck(i, colour);
                j++;
            }
        }
        market = new Market();
        players = new ArrayList<>();
        activePlayers = new ArrayList<>();
    }

    public void removePlayer(Player offlinePlayer) {
        activePlayers.remove(offlinePlayer);
    }

    public Player getPlayerByNickname(String nickname) {
        for (Player player : players) {
            if (nickname.equals(player.getNickname())) {
                return player;
            }
        }
        return null;
    }

    public List<Player> getActivePlayers() {
        return new ArrayList<>(activePlayers);
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Player nextPlayer() {
        if (currentPlayerIndex == activePlayers.size() - 1) {
            currentPlayerIndex = 0;
            return activePlayers.get(0);
        } else {
            currentPlayerIndex++;
            return activePlayers.get(currentPlayerIndex);
        }
    }

    public void setWinners(List<Player> winners) {
        this.winners = winners;
    }

    public DevDeck[] getDevDecks() {
        return devDecks;
    }

    public LeaderDeck getLeaderDeck() { return leaderDeck; }

    public DevCard drawDevCard(Colour colour, int level) {
        return devDecks[(level - 1) * Colour.values().length + colour.ordinal()].drawCard();
    }

    public Market getMarket() {
        return market;
    }

    public boolean isFinalTurn() {
        return finalTurn;
    }

    public void setFinalTurn(boolean finalTurn) {
        this.finalTurn = finalTurn;
    }
}
