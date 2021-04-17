package it.polimi.ingsw.model;
import java.util.ArrayList;
import java.util.List;

public abstract class Card {
    private final List<ResourceQuantity> resourceRequirements;
    private final int victoryPoints;

    public Card(ArrayList<ResourceQuantity> resourceRequirements, int victoryPoints) {
        this.resourceRequirements = resourceRequirements;
        this.victoryPoints = victoryPoints;
    }

    public ArrayList<ResourceQuantity> getRequirements() {
        if (resourceRequirements == null) return null;
        return new ArrayList<>(resourceRequirements);
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }
}
