package it.polimi.ingsw.model;

import java.util.List;

public class MarbleLeader extends LeaderCard{
    private final Resource additionalMarble;
    private final LeaderType type = LeaderType.MARBLE;

    public MarbleLeader(int victoryPoints, boolean state, List<DevCard> cardRequirements, Resource additionalMarble) {
        super(victoryPoints, state, cardRequirements);
        this.additionalMarble = additionalMarble;
    }

    public LeaderType getType() {
        return type;
    }

    public Resource getAdditionalMarble() {
        return additionalMarble;
    }
}
