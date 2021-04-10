package it.polimi.ingsw.model;
import java.util.List;

public class DevCard extends Card{
    private final int level;
    private final Colour colour;
    private List<ResourceQuantity> productionInput;
    private List<ResourceQuantity> productionOutput;

    public DevCard(List<ResourceQuantity> resourceRequirements, int victoryPoints, int level, Colour colour,
                   List<ResourceQuantity> productionInput, List<ResourceQuantity> productionOutput) {
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

    public List<ResourceQuantity> getProductionInput() {
        return productionInput;
    }

    public List<ResourceQuantity> getProductionOutput() {
        return productionOutput;
    }
}
