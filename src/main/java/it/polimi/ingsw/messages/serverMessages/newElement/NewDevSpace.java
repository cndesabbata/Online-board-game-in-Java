package it.polimi.ingsw.messages.serverMessages.newElement;

import it.polimi.ingsw.server.model.DevCard;

import java.util.List;

/**
 * Class NewDevSpace is a {@link ChangeMessage} that contains an updated copy of
 * a player's development space.
 *
 */
public class NewDevSpace implements ChangeMessage{
    private final List<List<DevCard>> devSpace;
    private final String owner;

    /**
     * Creates a NewDevSpace instance.
     *
     * @param devSpace the new development space
     * @param owner    the nickname of the player who owns the development space
     */
    public NewDevSpace(List<List<DevCard>> devSpace, String owner) {
        this.devSpace = devSpace;
        this.owner = owner;
    }

    /**
     * Returns the new development space.
     *
     * @return the new development space
     */
    public List<List<DevCard>> getDevSpace() {
        return devSpace;
    }

    /**
     * Returns the owner of the chest.
     *
     * @return the owner of the chest
     */
    public String getOwner() {
        return owner;
    }
}
