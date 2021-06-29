package it.polimi.ingsw.messages.serverMessages.newElement;

import java.util.List;

/**
 * Class NewPlayers is a {@link ChangeMessage} that contains an the nicknames
 * of all players in the game
 *
 */
public class NewPlayers implements ChangeMessage{
    private final List<String> players;

    /**
     * Creates a NewPlayers instance.
     *
     * @param players the list of nicknames
     */
    public NewPlayers(List<String> players) {
        this.players = players;
    }

    /**
     * Returns the list of nicknames.
     *
     * @return the list of nicknames
     */
    public List<String> getPlayers() {
        return players;
    }
}
