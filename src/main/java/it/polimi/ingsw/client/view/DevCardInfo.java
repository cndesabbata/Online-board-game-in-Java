package it.polimi.ingsw.client.view;

import java.util.List;

public class DevCardInfo {
    private final List<String> resourceRequirements;
    private final Integer victoryPoints;
    private final Integer level;
    private final String colour;
    private final List<String> productionInput;
    private final List<String> productionOutput;

    public DevCardInfo(List<String> resourceRequirements, int victoryPoints,
                       int level, String colour, List<String> productionInput, List<String> getProductionOutput) {
        this.resourceRequirements = resourceRequirements;
        this.victoryPoints = victoryPoints;
        this.level = level;
        this.colour = colour;
        this.productionInput = productionInput;
        this.productionOutput = getProductionOutput;
    }

    public DevCardInfo(int level, String colour) {
        this.level = level;
        this.colour = colour;
        this.resourceRequirements = null;
        this.victoryPoints = null;
        this.productionInput = null;
        this.productionOutput = null;
    }

    public List<String> getResourceRequirements() {
        return resourceRequirements;
    }

    public Integer getVictoryPoints() {
        return victoryPoints;
    }

    public Integer getLevel() {
        return level;
    }

    public String getColour() {
        return colour;
    }

    public List<String> getProductionInput() {
        return productionInput;
    }

    public List<String> getProductionOutput() {
        return productionOutput;
    }
}
