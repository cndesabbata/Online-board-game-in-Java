package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.DevCard;
import it.polimi.ingsw.server.model.ResourceQuantity;

import java.util.List;

/**
 * Class DevCardInfo is a simplified representation of a development card.
 *
 */
public class DevCardInfo {
    private final List<String> resourceRequirements;
    private final Integer victoryPoints;
    private final Integer level;
    private final String colour;
    private final List<String> productionInput;
    private final List<String> productionOutput;
    private final String url;

    /**
     * Creates a new DevCardInfo instance.
     *
     * @param c the development card the new instance will represent
     */
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

    /**
     * Returns the url of the image corresponding to the card.
     *
     * @return the url of the image corresponding to the card
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the list of resources required to buy the card.
     *
     * @return the list of resources required to buy the card
     */
    public List<String> getResourceRequirements() {
        return resourceRequirements;
    }

    /**
     * Returns the amount of victory points granted by this card.
     *
     * @return the amount of victory points granted by this card
     */
    public Integer getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Returns the level of the card.
     *
     * @return the level of the card
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * Returns the colour of the card.
     *
     * @return the colour of the card
     */
    public String getColour() {
        return colour;
    }

    /**
     * Returns the list of input resources for the card.
     *
     * @return the list of input resources for the card
     */
    public List<String> getProductionInput() {
        return productionInput;
    }

    /**
     * Returns the list of resources produced by the card.
     *
     * @return the list of resources produced by the card
     */
    public List<String> getProductionOutput() {
        return productionOutput;
    }

}
