package it.polimi.ingsw.server.serverNetwork;

import it.polimi.ingsw.server.controller.GameController;

import java.util.ArrayList;
import java.util.List;

/**
 * Lobby class represents a lobby of players.
 *
 */
public class Lobby {
    private final int totalPlayers;
    private final List<ClientConnection> waitingList;
    private final String owner;
    private final GameController gameController;

    /**
     * Creates a new lobby instance.
     *
     * @param totalPlayers   the lobby size
     * @param connection     the ClientConnection object of the lobby owner
     * @param gameController the controller that will manage the game
     */
    public Lobby(int totalPlayers, ClientConnection connection, GameController gameController) {
        this.totalPlayers = totalPlayers;
        this.waitingList = new ArrayList<>();
        this.waitingList.add(connection);
        this.owner = connection.getPlayerNickname();
        this.gameController = gameController;
    }

    /**
     * Adds a ClientConnection object to the waiting list.
     *
     * @param connection the ClientConnection object to add
     */
    public synchronized void addToWaitingList (ClientConnection connection){
        waitingList.add(connection);
    }

    /**
     * Removes a ClientConnection object from the waiting list.
     *
     * @param connection the ClientConnection object to remove
     */
    public synchronized void removeFromWaitingList(ClientConnection connection){
        waitingList.remove(connection);
    }

    /**
     * Returns the lobby size.
     *
     * @return the lobby size
     */
    public synchronized int getTotalPlayers() {
        return totalPlayers;
    }

    /**
     * Returns the waiting list.
     *
     * @return the waiting list
     */
    public synchronized List<ClientConnection> getWaitingList() {
        return waitingList;
    }

    /**
     * Returns the nickname of the lobby owner.
     *
     * @return the nickname of the lobby owner
     */
    public synchronized String getOwner() {
        return owner;
    }

    /**
     * Returns the controller that will manage the game.
     *
     * @return the controller that will manage the game
     */
    public synchronized GameController getGameController(){
        return gameController;
    }

}
