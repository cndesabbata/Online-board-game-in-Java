package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.messages.actions.Action;
import it.polimi.ingsw.model.CardStatus;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Server;

import java.util.ArrayList;
import java.util.List;

public abstract class GameController {
    private final Game game;
    private final Server server;
    private Integer started;
    private final List<Player> activePlayers;
    private final List<ClientConnection> activeConnections;


    public GameController(Server server) {
        this.server = server;
        this.game = new Game();
        activePlayers = new ArrayList<>();
        activeConnections = new ArrayList<>();
        started = 0;
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


    public abstract void makeAction(Action action);

    public Integer isStarted() {
        return started;
    }

    public void setStarted(Integer started) {
        this.started = started;
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
                            otherPlayer.getBoard().getItinerary().setCardStatus(CardStatus.FACE_UP, 0);
                        else
                            otherPlayer.getBoard().getItinerary().setCardStatus(CardStatus.DISCARDED, 0);
                    }
                }
            }
        }
    }

    public abstract void checkEndGame();

    public void setup(){
    }

    /* computes victory points for every player and sets the game winner */
    public abstract void endGame();

}
