package it.polimi.ingsw.messages.serverMessages;

import it.polimi.ingsw.server.serverNetwork.Lobby;

import java.util.ArrayList;
import java.util.List;

/**
 * Class PlayersNumberMessage represents a {@link CustomMessage} used by the server used to inform
 * the player of the available lobbies.
 *
 */
public class PlayersNumberMessage extends CustomMessage {
    private final List<String> infoLobbies;
    private final List<String> owners;

    /**
     * Creates a new PlayersNumberMessage instance. Creates a string for each lobby
     * that shows the lobby owner, the lobby size, and how many players have already
     * joined that lobby.
     *
     * @param message the message to show
     * @param lobbies the available lobbies
     */
    public PlayersNumberMessage(String message, List<Lobby> lobbies) {
        super(message);
        infoLobbies = new ArrayList<>();
        owners = new ArrayList<>();
        for(Lobby l : lobbies){
            owners.add(l.getOwner());
            infoLobbies.add(l.getOwner().toUpperCase() + " :  [ "
                    + l.getWaitingList().size() + " / " + l.getTotalPlayers() + " ]");
        }
    }

    /**
     * Returns the list of strings that contains information on the available lobbies.
     *
     * @return the list of strings that contains information on the available lobbies
     */
    public List<String> getInfoLobbies() {
        return infoLobbies;
    }

    /**
     * Returns the list of nicknames of lobby owners.
     *
     * @return the list of nicknames of lobby owners
     */
    public List<String> getOwners() {
        return owners;
    }
}