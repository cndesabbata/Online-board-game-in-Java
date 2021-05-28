package it.polimi.ingsw.server.model;

import java.io.Serializable;
import java.util.List;

public class LeaderCard extends Card implements Serializable {
    private final List<DevCard> cardRequirements;
    private final Resource resource;
    private final LeaderType type;

    public LeaderCard(List<ResourceQuantity> resourceRequirements, int victoryPoints,
                      Resource resource, LeaderType type) {
        super(resourceRequirements, victoryPoints);
        this.cardRequirements = null;
        this.resource = resource;
        this.type = type;
    }

    public LeaderCard(int victoryPoints, List<DevCard> cardRequirements,
                      Resource resource, LeaderType type) {
        super(null, victoryPoints);
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

}
