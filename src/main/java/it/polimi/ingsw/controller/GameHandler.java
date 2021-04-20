package it.polimi.ingsw.controller;

import java.util.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.Server;

public class GameHandler {
    private Game model;
    private GameController gameController;
    private Server server;
    private int started;
    private int numOfPlayers;
    private InitialLeaderDraw initialLeaderDraw;
    private Player currentPlayer;

    public void setup(int numOfPlayers, List<String> nickNames) {

    }

    public void endGame() {

    }

    private void setupPlayer(String nickname) {

    }

    public void executeAction(Action action) {

    }

    public Game getModel() {
        return model;
    }

}