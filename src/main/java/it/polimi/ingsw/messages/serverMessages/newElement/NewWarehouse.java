package it.polimi.ingsw.messages.serverMessages.newElement;

import it.polimi.ingsw.server.model.ResourceQuantity;

import java.util.List;

/**
 * Class NewWarehouse is a {@link ChangeMessage} that contains an updated copy of
 * a player's warehouse.
 *
 */
public class NewWarehouse implements ChangeMessage{
    private final List<ResourceQuantity> warehouse;
    private final int initialDim;
    private final String owner;

    /**
     * Creates a NewWarehouse instance.
     *
     * @param warehouse  the new warehouse
     * @param initialDim the initialDim attribute of the warehouse
     * @param owner      the nickname of the player who owns the warehouse
     */
    public NewWarehouse(List<ResourceQuantity> warehouse, int initialDim, String owner) {
        this.warehouse = warehouse;
        this.initialDim = initialDim;
        this.owner = owner;
    }

    /**
     * Returns the new warehouse.
     *
     * @return the new warehouse
     */
    public List<ResourceQuantity> getWarehouse() {
        return warehouse;
    }

    public int getInitialDim() {
        return initialDim;
    }

    /**
     * Returns the owner of the warehouse.
     *
     * @return the owner of the warehouse
     */
    public String getOwner() {
        return owner;
    }
}
