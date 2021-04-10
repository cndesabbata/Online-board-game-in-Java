package it.polimi.ingsw.model;

import java.util.List;

public class LeaderCard extends Card{
    private boolean state;
    private final List<DevCard> cardRequirements;

    public LeaderCard(List<ResourceQuantity> resourceRequirements, int victoryPoints, boolean state) {
        super(resourceRequirements, victoryPoints);
        this.state = state;
        this.cardRequirements = null;
    }

    public LeaderCard(int victoryPoints, boolean state, List<DevCard> cardRequirements) {
        super(null, victoryPoints);
        this.state = state;
        this.cardRequirements = cardRequirements;
    }

    public List<DevCard> getCardRequirements() {
        return cardRequirements;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
