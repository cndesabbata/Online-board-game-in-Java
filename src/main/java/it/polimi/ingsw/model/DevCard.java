package it.polimi.ingsw.model;
import java.util.List;

public class DevCard extends Card{
    private final int level;
    private final Colour colour;
    private List<ResourceReq> productionInput;
    private List<ResourceReq> productionOutput;

    public DevCard(List<ResourceReq> resourceRequirements, int victoryPoints, int level, Colour colour,
                   List<ResourceReq> productionInput, List<ResourceReq> productionOutput) {
        super(resourceRequirements, victoryPoints);
        this.level = level;
        this.colour = colour;
        this.productionInput = productionInput;
        this.productionOutput = productionOutput;
    }

    public DevCard(int level, Colour colour) {
        super(null, 0);
        this.level = level;
        this.colour = colour;
    }

    public int getLevel() {
        return level;
    }

    public Colour getColour() {
        return colour;
    }

    public List<ResourceReq> getProductionInput() {
        return productionInput;
    }

    public List<ResourceReq> getProductionOutput() {
        return productionOutput;
    }
}
