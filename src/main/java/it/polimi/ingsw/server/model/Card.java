package it.polimi.ingsw.server.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Card implements Serializable {
    private final List<ResourceQuantity> resourceRequirements;
    private final int victoryPoints;

    public Card(List<ResourceQuantity> resourceRequirements, int victoryPoints) {
        this.resourceRequirements = resourceRequirements;
        this.victoryPoints = victoryPoints;
    }

    public List<ResourceQuantity> getResourceRequirements() {
        if (resourceRequirements == null) return null;
        return new ArrayList<>(resourceRequirements);
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }
}
