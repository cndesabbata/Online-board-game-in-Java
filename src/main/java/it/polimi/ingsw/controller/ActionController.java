package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;

public class ActionController {
    private GameBoard board;
    private Action action;                                                                                              //serve?
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
        actionDone = action.doAction(currentPlayer);
    }

    public void startTurn() {

    }

    public void endTurn() {

    }
}