package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.gameboard.CardStatus;
import it.polimi.ingsw.server.observer.Observer;
import it.polimi.ingsw.server.serverNetwork.ClientConnection;
import it.polimi.ingsw.server.serverNetwork.Server;

import java.util.ArrayList;
import java.util.List;

public abstract class GameController {
    private final Game game;
    private final Server server;
    private GamePhase phase;
    private final List<Player> activePlayers;
    private final List<ClientConnection> activeConnections;


    public GameController(Server server) {
        this.server = server;
        this.game = new Game();
        activePlayers = new ArrayList<>();
        activeConnections = new ArrayList<>();
        phase = GamePhase.NOT_STARTED;
    }

    public void reloadView(String nickname){
        game.getMarket().notifyNew(nickname);
        for (DevDeck d : game.getDevDecks()){
            d.notifyNew(nickname);
        }
        for (Player p : activePlayers){
            p.notifyNew(nickname);
            p.getBoard().getChest().notifyNew(nickname);
            p.getBoard().getDevSpace().notifyNew(nickname);
            p.getBoard().getWarehouse().notifyNew(nickname);
            p.getBoard().getItinerary().notifyNew(nickname);
        }
    }

    public void setUpPlayer(ClientConnection connection){
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

    public void addActiveConnection(ClientConnection connection){
        activeConnections.add(connection);
    }

    public void addActivePlayer(Player player){
        activePlayers.add(player);
    }

    public abstract void sendReloadedView(String nickname);

    public abstract void makeAction(Action action);

    public GamePhase getPhase() {
        return phase;
    }

    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }

    /* checks if any papalReport needs to be triggered */
    public void checkAllPapalReports() {
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
                            otherPlayer.getBoard().getItinerary().setCardStatus(CardStatus.FACE_UP, cardStatusIndex);
                        else
                            otherPlayer.getBoard().getItinerary().setCardStatus(CardStatus.DISCARDED, cardStatusIndex);
                    }
                }
            }
        }
    }

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

    public int addPapalVP(Player player) {
        int result = 0;
        CardStatus[] papalCardStatus = player.getBoard().getItinerary().getCardStatus();
        for (int i = 0; i < 3; i++) {
            if (papalCardStatus[i] == CardStatus.FACE_UP) result += i + 2;
        }
        return result;
    }

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

    public int addLeaderVP(Player player) {
        int result = 0;
        for (LeaderCard leaderCard : player.getPlayedLeaderCards())
                result += leaderCard.getVictoryPoints();
        return result;
    }

    public void addObserver(Observer observer){
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

    /* computes victory points for every player and sets the game winner */
    public abstract void endGame();

}
