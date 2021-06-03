package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.DevCard;
import it.polimi.ingsw.server.model.ResourceQuantity;

import java.util.List;

public class DevCardInfo {
    private final List<String> resourceRequirements;
    private final Integer victoryPoints;
    private final Integer level;
    private final String colour;
    private final List<String> productionInput;
    private final List<String> productionOutput;
    private final String url;

    public DevCardInfo(DevCard c){
        if (c.getResourceRequirements() == null){
            this.resourceRequirements = null;
            this.productionInput = null;
            this.productionOutput = null;
            this.victoryPoints = null;
            this.url = null;
        }
        else {
            this.resourceRequirements = ResourceQuantity.toStringList(ResourceQuantity.flatten(c.getResourceRequirements()));
            this.productionInput = ResourceQuantity.toStringList(ResourceQuantity.flatten(c.getProductionInput()));
            this.productionOutput = ResourceQuantity.toStringList(ResourceQuantity.flatten(c.getProductionOutput()));
            this.victoryPoints = c.getVictoryPoints();
            this.url = c.getUrl();
        }
        this.colour = c.getColour().toString();
        this.level = c.getLevel();
    }

    public String getUrl() {
        return url;
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
