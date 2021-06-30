package it.polimi.ingsw.messages.clientMessages.internal;

import java.util.List;

/**
 * Class PrintChest represents a {@link ViewMessage} that instructs the CLI and the GUI
 * to show the player the available lobbies he can join.
 *
 */
public class RequestPlayersNumber extends ViewMessage {
    List<String> infoLobbies;
    List<String> owners;

    /**
     * Creates a new RequestPlayersNumber instance.
     *
     * @param message     a string message that can be shown to the player
     * @param infoLobbies the list that describes the state of each lobby
     * @param owners      the list of lobby owners
     */
    public RequestPlayersNumber(String message, List<String> infoLobbies, List<String> owners) {
        super(message);
        this.infoLobbies = infoLobbies;
        this.owners = owners;
    }

    /**
     * Returns the list that describes the state of each lobby.
     *
     * @return the list that describes the state of each lobby
     */
    public List<String> getInfoLobbies() {
        return infoLobbies;
    }

    /**
     * Returns the list of lobby owners.
     *
     * @return the list of hobby owners
     */
    public List<String> getOwners() {
        return owners;
    }
}