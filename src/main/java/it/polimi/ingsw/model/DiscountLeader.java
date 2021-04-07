package it.polimi.ingsw.model;

import java.util.List;

public class DiscountLeader extends LeaderCard{
    private final Resource discount;
    private final LeaderType type = LeaderType.DISCOUNT;

    public DiscountLeader(int victoryPoints, boolean state, List<DevCard> cardRequirements, Resource discount) {
        super(victoryPoints, state, cardRequirements);
        this.discount = discount;
    }

    public LeaderType getType() {
        return type;
    }

    public Resource getDiscount() {
        return discount;
    }
}
