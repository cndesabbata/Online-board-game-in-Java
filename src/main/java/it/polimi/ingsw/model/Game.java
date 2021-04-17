package it.polimi.ingsw.model;

import java.util.*;

public class Game {
    private final LeaderDeck leaderDeck;
    private DevDeck[] devDecks;
    private int warehouseDim;
    private final List<Player> players;
    private List<Player> activePlayers;
    private Player currentPlayer;
    private int currentPlayerIndex;
    private final Market market;

    public Game() { // costruttore da riscrivere
        leaderDeck = new LeaderDeck();
        devDecks = new DevDeck[12];
        currentPlayer = null;
        market = new Market();
        currentPlayerIndex = 0;
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

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() { return currentPlayer; }

    public DevDeck[] getDevDecks() { return devDecks; }

    public DevCard drawDevCard (Colour colour, int level){
        return devDecks[(level-1) * Colour.values().length + colour.ordinal()].drawCard();
    }

    public Player nextPlayer() {
        if(currentPlayerIndex == activePlayers.size()) {
            currentPlayerIndex = 0;
            return activePlayers.get(0);
        }
        else {
            currentPlayerIndex++;
            return activePlayers.get(currentPlayerIndex);
        }
    }

}
