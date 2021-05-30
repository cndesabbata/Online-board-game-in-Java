package it.polimi.ingsw.server.serverNetwork;

import it.polimi.ingsw.server.controller.GameController;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    private final int totalPlayers;
    private final List<ClientConnection> waitingList;
    private final String owner;
    private final GameController gameController;

    public Lobby(int totalPlayers, ClientConnection connection, GameController gameController) {
        this.totalPlayers = totalPlayers;
        this.waitingList = new ArrayList<>();
        this.waitingList.add(connection);
        this.owner = connection.getPlayerNickname();
        this.gameController = gameController;
    }

    public synchronized void addToWaitingList (ClientConnection connection){
        waitingList.add(connection);
    }

    public synchronized void removeFromWaitingList(ClientConnection connection){
        waitingList.remove(connection);
    }

    public synchronized int getTotalPlayers() {
        return totalPlayers;
    }

    public synchronized List<ClientConnection> getWaitingList() {
        return waitingList;
    }

    public synchronized String getOwner() {
        return owner;
    }

    public synchronized GameController getGameController(){
        return gameController;
    }

}
