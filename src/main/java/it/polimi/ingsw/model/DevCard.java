package it.polimi.ingsw.model;
import java.util.ArrayList;
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
        return new ArrayList<>(productionInput);
    }

    public List<ResourceQuantity> getProductionOutput() {
        return new ArrayList<>(productionOutput);
    }

    public boolean equals(Object o){
        if (!(o instanceof DevCard)) return false;
        DevCard devCard= (DevCard) o;
        if(devCard.getColour() != colour || devCard.getLevel() != level)
            return false;
        else {
            boolean check = true;
            for (int i = 0; i < getResourceRequirements().size(); i++) {
                if (getResourceRequirements().get(i).getQuantity() != devCard.getResourceRequirements().get(i).getQuantity() ||
                        getResourceRequirements().get(i).getResource() != devCard.getResourceRequirements().get(i).getResource()) {
                    check = false;
                    break;
                }
            }
            if (!check) return check;
            else {
                for (int i = 0; i < productionInput.size(); i++) {
                    if (productionInput.get(i).getQuantity() != devCard.getProductionInput().get(i).getQuantity() ||
                            productionInput.get(i).getResource() != devCard.getProductionInput().get(i).getResource()) {
                        check = false;
                        break;
                    }
                }
                if (!check) return check;
                else {
                    for (int i = 0; i < productionOutput.size(); i++) {
                        if (productionOutput.get(i).getQuantity() != devCard.getProductionOutput().get(i).getQuantity() ||
                                productionOutput.get(i).getResource() != devCard.getProductionOutput().get(i).getResource()) {
                            check = false;
                            break;
                        }
                    }
                    return check;
                }
            }
        }
    }

}
