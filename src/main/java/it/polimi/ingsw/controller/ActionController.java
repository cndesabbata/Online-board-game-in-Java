package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameBoard;

public class ActionController {
    private GameBoard board;
    private Action action;

    public void makeAction(Action action) {
        action.doAction();
    }
}
