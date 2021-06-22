package it.polimi.ingsw.server.model;

import it.polimi.ingsw.messages.serverMessages.newElement.NewPlayers;
import it.polimi.ingsw.server.observer.Observable;

import java.util.*;

/**
 * Class Game represents a game instance. It contains information about the
 * players, the card decks, the market and the methods to handle them.
 *
 */
public class Game extends Observable {
    private final LeaderDeck leaderDeck;
    private final DevDeck[] devDecks;
    private final List<Player> players;
    private final List<Player> winners;
    private final Market market;
    private boolean finalTurn;

    /**
     * Creates a game object instance that includes the card decks
     * and the market.
     *
     */
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
        winners = new ArrayList<>();
    }

    /**
     * Adds a player to the player list.
     *
     * @param newPlayer the player to add
     */
    public void addPlayer(Player newPlayer) {
        players.add(newPlayer);
    }

    /**
     * Returns the player identified by his nickname.
     *
     * @param nickname the player's nickname
     * @return the player with the desired nickname, {@code null} if no player is found
     */
    public Player getPlayerByNickname(String nickname) {
        for (Player player : players) {
            if (nickname.equalsIgnoreCase(player.getNickname())) {
                return player;
            }
        }
        return null;
    }

    /**
     * Notifies a player's virtual view with a NewPlayers message containing
     * the nicknames of the active players.
     *
     * @param activePlayers the list of active players
     * @param nickname      the nickname of the player to notify
     */
    public void notifyNewPlayers(List<Player> activePlayers, String nickname){
        List <String> nicknames = new ArrayList<>();
        for(Player p : activePlayers){
            nicknames.add(p.getNickname());
        }
        notifySingleObserver(new NewPlayers(nicknames), nickname);
    }

    /**
     * Returns the list of players.
     *
     * @return the list of players
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Returns a copy of the array of development card decks.
     *
     * @return a copy of the array od development card decks
     */
    public DevDeck[] getDevDecks() {
        return Arrays.copyOf(devDecks, devDecks.length);
    }

    /**
     * Returns the leader card deck.
     *
     * @return the leader card deck
     */
    public LeaderDeck getLeaderDeck() {
        return leaderDeck;
    }

    /**
     * Returns a development card drawn from a desired deck of development cards.
     *
     * @param colour the colour of the desired deck
     * @param level  the level of the desired deck
     * @return the drawn development card
     */
    public DevCard drawDevCard(Colour colour, int level) {
        return devDecks[(level - 1) * Colour.values().length + colour.ordinal()].drawCard();
    }

    /**
     * Returns the market.
     *
     * @return the market
     */
    public Market getMarket() {
        return market;
    }

    /**
     * Check if the game has reached the final turn.
     *
     * @return {@code true} if the game has reached the final turn, {@code false} otherwise
     */
    public boolean isFinalTurn() {
        return finalTurn;
    }

    /**
     * Sets the new value for the finalTurn attribute.
     *
     * @param finalTurn the new value of the finalTurn attribute
     */
    public void setFinalTurn(boolean finalTurn) {
        this.finalTurn = finalTurn;
    }

    /**
     * Sets the winners for this game object.
     *
     * @param winners the list of winner players
     */
    public void setWinners(List<Player> winners) {
        this.winners.addAll(winners);
    }

    /**
     * Returns the list of winner players.
     *
     * @return the list of winner players
     */
    public List<Player> getWinners() {
        return winners;
    }
}
