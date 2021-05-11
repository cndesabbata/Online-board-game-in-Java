package it.polimi.ingsw.messages.newElement;

import it.polimi.ingsw.server.model.ResourceQuantity;
import java.util.List;

public class NewChest implements ChangeMessage {
    private final List<ResourceQuantity> chest;
    private final String owner;

    public NewChest(List<ResourceQuantity> chest, String owner) {
        this.chest = chest;
        this.owner = owner;
    }

    public List<ResourceQuantity> getChest() {
        return chest;
    }

    public String getOwner() {
        return owner;
    }
}
