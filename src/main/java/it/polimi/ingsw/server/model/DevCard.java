package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;

public class DevCard extends Card {
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
        return new ArrayList<>(productionInput);
    }

    public List<ResourceQuantity> getProductionOutput() {
        return new ArrayList<>(productionOutput);
    }

    public boolean equals(Object o){
        if (!(o instanceof DevCard)) return false;
        DevCard devCard = (DevCard) o;
        if (devCard.getColour() != colour || devCard.getLevel() != level || devCard.getVictoryPoints() != getVictoryPoints())
            return false;
        return checkList(devCard.getResourceRequirements(), getResourceRequirements()) &&
                checkList(devCard.getProductionInput(), getProductionInput()) &&
                checkList(devCard.getProductionOutput(), getProductionOutput());
    }

    public boolean checkList(List<ResourceQuantity> thoseRes, List<ResourceQuantity> theseRes) {
        if (thoseRes.size() != theseRes.size())
            return false;
        for (int i = 0; i < theseRes.size(); i++) {
            if (thoseRes.get(i).getResource() != theseRes.get(i).getResource() ||
                    thoseRes.get(i).getQuantity() != theseRes.get(i).getQuantity())
                return false;
        }
        return true;
    }


}
