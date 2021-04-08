package it.polimi.ingsw.model;

import java.util.List;

public class ProductLeader extends LeaderCard{
    private final Resource product;
    private final LeaderType type = LeaderType.PRODUCT;

    public ProductLeader(int victoryPoints, boolean state, List<DevCard> cardRequirements, Resource product) {
        super(victoryPoints, state, cardRequirements);
        this.product = product;
    }

    public LeaderType getType() {
        return type;
    }

    public Resource getProduct() {
        return product;
    }
}
