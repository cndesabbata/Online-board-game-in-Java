package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.server.controller.multiplayer.MultiPlayerController;
import it.polimi.ingsw.server.controller.singleplayer.SinglePlayerController;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.gameboard.CardStatus;
import it.polimi.ingsw.server.observer.Observer;
import it.polimi.ingsw.server.serverNetwork.ClientConnection;
import it.polimi.ingsw.server.serverNetwork.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class GameController represents a generic game controller.
 * It contains method that are equal in both {@link SinglePlayerController}
 * and {@link MultiPlayerController}.
 *
 */
public abstract class GameController {
    private final Game game;
    private final Server server;
    private GamePhase phase;
    private final List<Player> activePlayers;
    private final List<ClientConnection> activeConnections;

    /**
     * Default constructor.
     *
     * @param server the server that is hosting the game
     */
    public GameController(Server server) {
        this.server = server;
        this.game = new Game();
        activePlayers = new ArrayList<>();
        activeConnections = new ArrayList<>();
        phase = GamePhase.NOT_STARTED;
    }

    /**
     * Notifies a player's virtual view with a set of messages containing all
     * the view elements. Used when a player is reconnecting to the game.
     *
     * @param nickname the nickname of the player to notify
     */
    public void reloadView(String nickname){
        game.getMarket().notifyNew(nickname);
        for (DevDeck d : game.getDevDecks()){
            d.notifyNew(nickname);
        }
        for (Player p : activePlayers){
            getGame().notifyNewPlayers(getActivePlayers(), p.getNickname());
            p.notifyNew(nickname);
            p.getBoard().getChest().notifyNew(nickname);
            p.getBoard().getDevSpace().notifyNew(nickname);
            p.getBoard().getWarehouse().notifyNew(nickname);
            p.getBoard().getItinerary().notifyNew(nickname);
        }
    }

    /**
     * Returns the server that is hosting the game.
     *
     * @return the server that is hosting the game
     */
    public Server getServer() {
        return server;
    }

    /**
     * Creates a new Player object associated with a {@link ClientConnection} object and adds
     * it to the list of players in the game, the list of active players and the list of active
     * connections.
     *
     * @param connection the ClientConnection object associated with the player
     */
    public void setUpPlayer(ClientConnection connection){
        Player newPlayer = new Player(connection.getPlayerNickname(), game);
        addActivePlayer(newPlayer);
        addActiveConnection(connection);
        game.addPlayer(newPlayer);
    }

    /**
     * Returns the list of active connections.
     *
     * @return the list of active connections.
     */
    public List<ClientConnection> getActiveConnections() {
        return activeConnections;
    }

    /**
     * Returns the game managed by this game controller.
     *
     * @return the game managed by this game controller
     */
    public Game getGame() {
        return game;
    }

    /**
     * Returns the list of active players.
     *
     * @return the list of active players
     */
    public List<Player> getActivePlayers() {
        return activePlayers;
    }

    /**
     * Adds a Connection object to the list of active connections.
     *
     * @param connection the connection to add
     */
    public void addActiveConnection(ClientConnection connection){
        activeConnections.add(connection);
    }

    /**
     * Adds a player to the list of active players.
     *
     * @param player the player to add
     */
    public void addActivePlayer(Player player){
        activePlayers.add(player);
    }

    public abstract void sendReloadedView(String nickname);

    public abstract void makeAction(Action action);

    /**
     * Returns the phase of the game.
     *
     * @return the phase of the game
     */
    public GamePhase getPhase() {
        return phase;
    }

    /**
     * Sets the phase of the game.
     *
     * @param phase the new phase of the game
     */
    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }

    /**
     * Checks if any papal report needs to be triggered. If one of them is
     * triggered it updates the status of papal cards for all the players.
     *
     */
    public void checkAllPapalReports() {
        checkPapalReport(8, 5, 0);
        checkPapalReport(16, 12, 1);
        checkPapalReport(24, 19, 2);
    }

    /**
     * Checks if a specified papal report needs to be triggered. If the papal report
     * is triggered it updates the status of the corresponding papal cards for all
     * the players.
     *
     * @param vaticanReportTrigger the index of the cell on the itinerary triggering the report
     * @param vaticanReportStart   the first cell on the itinerary that allows a player to turn is card up if the report is triggered
     * @param cardStatusIndex      the index of the papal cards that needs to be updated
     */
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
                            otherPlayer.getBoard().getItinerary().setCardStatus(CardStatus.FACE_UP, cardStatusIndex);
                        else
                            otherPlayer.getBoard().getItinerary().setCardStatus(CardStatus.DISCARDED, cardStatusIndex);
                    }
                }
            }
            if(player.getBoard().getItinerary().getBlackCrossPosition() != null) {
                int blackCrossPos = player.getBoard().getItinerary().getBlackCrossPosition();
                if(blackCrossPos >= vaticanReportTrigger) {
                    if(cardStatuses[cardStatusIndex] == CardStatus.FACE_DOWN) {
                        if(position >= vaticanReportStart)
                            player.getBoard().getItinerary().setCardStatus(CardStatus.FACE_UP, cardStatusIndex);
                        else
                            player.getBoard().getItinerary().setCardStatus(CardStatus.DISCARDED, cardStatusIndex);
                    }
                }
            }
        }
    }

    /**
     * Returns the amount of victory points given to a specified player for
     * his position on the itinerary.
     *
     * @param player the specified player
     * @return the amount of victory points given for the player's position on the itinerary
     */
    protected int addItineraryVP(Player player) {
        int result = 0;
        int[] itineraryVP = {1,2,4,6,9,12,16,20};
        for(int i = 3, j = 0; i <= 21; i = i + 3, j++) {
            if (player.getBoard().getItinerary().getPosition() >= i && player.getBoard().getItinerary().getPosition() < i + 3)
                result += itineraryVP[j];
        }
        if (player.getBoard().getItinerary().getPosition() == 24)
            result += itineraryVP[7];
        return result;
    }

    /**
     * Returns the amount of victory points given to a specified player by
     * his papal cards.
     *
     * @param player the specified player
     * @return the amount of victory points given by the player's papal cards
     */
    public int addPapalVP(Player player) {
        int result = 0;
        CardStatus[] papalCardStatus = player.getBoard().getItinerary().getCardStatus();
        for (int i = 0; i < 3; i++) {
            if (papalCardStatus[i] == CardStatus.FACE_UP) result += i + 2;
        }
        return result;
    }

    /**
     * Returns the amount of victory points given to a specified player by
     * the developments cards on his development space.
     *
     * @param player the specified player
     * @return the amount of victory points given by his development cards
     */
    public int addDevCardVP(Player player) {
        int result = 0;
        List<List<DevCard>> playerDevCards = player.getBoard().getDevSpace().getCards();
        for (List<DevCard> devSlotCard : playerDevCards) {
            for (DevCard devCard : devSlotCard) {
                result += devCard.getVictoryPoints();
            }
        }
        return result;
    }

    /**
     * Returns the amount of victory points given to a specified player by
     * his leader cards.
     *
     * @param player the specified player
     * @return the amount of victory points given by his leader cards
     */
    public int addLeaderVP(Player player) {
        int result = 0;
        for (LeaderCard leaderCard : player.getPlayedLeaderCards())
                result += leaderCard.getVictoryPoints();
        return result;
    }

    /**
     * Adds an observer to all the view elements that need to be observed.
     *
     * @param observer the observer to add
     */
    public void addObserver(Observer observer){
        game.addObserver(observer);
        for (DevDeck d : game.getDevDecks()){
            d.addObserver(observer);
        }
        game.getMarket().addObserver(observer);
        for (Player p : activePlayers){
            p.addObserver(observer);
            p.getBoard().getChest().addObserver(observer);
            p.getBoard().getDevSpace().addObserver(observer);
            p.getBoard().getWarehouse().addObserver(observer);
            p.getBoard().getItinerary().addObserver(observer);
        }
    }

    /**
     * Removes an observer from all the view elements that it was observing.
     *
     * @param observer the observer to remove
     */
    public void removeObserver (Observer observer){
        for (DevDeck d : game.getDevDecks()){
            d.removeObserver(observer);
        }
        game.getMarket().removeObserver(observer);
        for (Player p : activePlayers){
            p.removeObserver(observer);
            p.getBoard().getChest().removeObserver(observer);
            p.getBoard().getDevSpace().removeObserver(observer);
            p.getBoard().getWarehouse().removeObserver(observer);
            p.getBoard().getItinerary().removeObserver(observer);
        }
    }

    public abstract void checkEndGame();

    public abstract void setup();
    
    public abstract void endGame();

}
