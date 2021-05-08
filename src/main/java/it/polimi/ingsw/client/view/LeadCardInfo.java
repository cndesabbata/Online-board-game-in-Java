package it.polimi.ingsw.client.view;

import java.util.List;

public class LeadCardInfo {
    private final int victoryPoints;
    private final List<String> resourceRequirements;
    private final List<DevCardInfo> cardRequirements;
    private final String type;
    private final String resource;

    public LeadCardInfo(int victoryPoints, List<String> resourceRequirements, String type, String resource) {
        this.victoryPoints = victoryPoints;
        this.resourceRequirements = resourceRequirements;
        this.type = type;
        this.resource = resource;
        this.cardRequirements = null;
    }

    public LeadCardInfo(List<DevCardInfo> cardRequirements, int victoryPoints, String type, String resource) {
        this.victoryPoints = victoryPoints;
        this.cardRequirements = cardRequirements;
        this.type = type;
        this.resource = resource;
        this.resourceRequirements = null;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public List<String> getResourceRequirements() {
        return resourceRequirements;
    }

    public List<DevCardInfo> getCardRequirements() {
        return cardRequirements;
    }

    public String getType() {
        return type;
    }

    public String getResource() {
        return resource;
    }
}
