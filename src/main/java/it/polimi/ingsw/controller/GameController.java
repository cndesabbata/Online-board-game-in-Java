package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.CardStatus;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.Server;

import java.util.List;

public class GameController {
    private Game game;
    private Player currentPlayer;
    private Server server;
    private boolean actionDone;
    private boolean started;
    private List<Player> activePlayers;
    private int currentPlayerIndex;


    public GameController(Server server) {
        this.server = server;
        this.game = new Game();
    }

    public void setUpPlayer(String nickname, Integer clientID){
        Player newPlayer = new Player(nickname, clientID, game);
        activePlayers.add(newPlayer);
        game.addPlayer(newPlayer);
    }

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

    public boolean isStarted() {
        return started;
    }

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
    private void checkPapalReport(int papalReportTrigger, int papalReportStart, int cardStatusIndex) {
        List<Player> players = game.getPlayers();

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
                currentPlayer.getBoard().getDevSpace().countCards() == 7) {
            game.setFinalTurn(true);
        }
    }

    public void setup(){

    }

    public void endGame() {
        
    }
}