package it.polimi.ingsw.messages.newElement;

import it.polimi.ingsw.server.model.ResourceQuantity;

import java.util.ArrayList;
import java.util.List;

public class NewWarehouse implements ChangeMessage{
    private final List<ResourceQuantity> warehouse;
    private final int initialDim;
    private String owner;

    public NewWarehouse(List<ResourceQuantity> warehouse, int initialDim, String owner) {
        this.warehouse = warehouse;
        this.initialDim = initialDim;
        this.owner = owner;
    }

    public List<ResourceQuantity> getWarehouse() {
        return warehouse;
    }

    public int getInitialDim() {
        return initialDim;
    }

    public String getOwner() {
        return owner;
    }
}
