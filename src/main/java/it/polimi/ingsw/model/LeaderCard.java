package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class LeaderCard extends Card{
    private boolean isPlayed;
    private final List<DevCard> cardRequirements;
    private final Resource resource;
    private final LeaderType type;

    public LeaderCard(ArrayList<ResourceQuantity> resourceRequirements, int victoryPoints,
                      Resource resource, LeaderType type) {
        super(resourceRequirements, victoryPoints);
        this.cardRequirements = null;
        this.resource = resource;
        this.type = type;
        this.isPlayed = false;
    }

    public LeaderCard(int victoryPoints, List<DevCard> cardRequirements,
                      Resource resource, LeaderType type) {
        super(null, victoryPoints);
        this.isPlayed = false;
        this.cardRequirements = cardRequirements;
        this.resource = resource;
        this.type = type;
    }

    public Resource getResource() {
        return resource;
    }

    public LeaderType getType() {
        return type;
    }

    public List<DevCard> getCardRequirements() {
        return cardRequirements;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public void setPlayed(boolean played) {
        this.isPlayed = played;
    }

}
