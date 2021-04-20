package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.CardStatus;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.List;

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
                currentPlayer.getBoard().getDevSpace().countCards() == 7) {

            game.setFinalTurn(true);
        }
    }

    public void endGame() {
        
    }
}