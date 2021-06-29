package it.polimi.ingsw.messages.serverMessages.newElement;

import it.polimi.ingsw.server.model.ResourceQuantity;
import java.util.List;

/**
 * Class NewChest is a {@link ChangeMessage} that contains an updated copy of
 * a player's chest.
 *
 */
public class NewChest implements ChangeMessage {
    private final List<ResourceQuantity> chest;
    private final String owner;

    /**
     * Creates a NewChest instance.
     *
     * @param chest the new chest
     * @param owner the nickname of the player who owns the chest
     */
    public NewChest(List<ResourceQuantity> chest, String owner) {
        this.chest = chest;
        this.owner = owner;
    }

    /**
     * Returns the new chest.
     *
     * @return the new chest
     */
    public List<ResourceQuantity> getChest() {
        return chest;
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
