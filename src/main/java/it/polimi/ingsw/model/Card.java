package it.polimi.ingsw.model;
import java.util.ArrayList;
import java.util.List;

public abstract class Card {
    private final List<ResourceReq> resourceRequirements;
    private final int victoryPoints;

    public Card(List<ResourceReq> resourceRequirements, int victoryPoints) {
        this.resourceRequirements = resourceRequirements;
        this.victoryPoints = victoryPoints;
    }

    public List<ResourceReq> getRequirements() {
        if (resourceRequirements == null) return null;
        return new ArrayList<>(resourceRequirements);
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }
}
