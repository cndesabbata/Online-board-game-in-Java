package it.polimi.ingsw.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DevCard class represents a development card.
 *
 */
public class DevCard extends Card implements Serializable {
    private final int level;
    private final Colour colour;
    private List<ResourceQuantity> productionInput;
    private List<ResourceQuantity> productionOutput;

    /**
     * Default constructor.
     *
     * @param resourceRequirements the resource requirements list of the card
     * @param victoryPoints        the amount of victory points granted by the card
     * @param level                the level of the card
     * @param colour               the colour of the card
     * @param productionInput      the list of resources the card takes as input
     * @param productionOutput     the list of resources the card gives as output
     * @param url                  the url of the image corresponding to the card
     */
    public DevCard(List<ResourceQuantity> resourceRequirements, int victoryPoints, int level, Colour colour,
                   List<ResourceQuantity> productionInput, List<ResourceQuantity> productionOutput, String url) {
        super(resourceRequirements, victoryPoints, url);
        this.level = level;
        this.colour = colour;
        this.productionInput = productionInput;
        this.productionOutput = productionOutput;
    }

    /**
     * Reduced constructor. It is used to create DevCard objects used as requirements for
     * leader card to avoid adding unnecessary information.
     *
     * @param level  the level of the card
     * @param colour the colour of the card
     */
    public DevCard(int level, Colour colour) {
        super(null, 0, null);
        this.level = level;
        this.colour = colour;
    }

    /**
     * Returns the level of the card.
     *
     * @return the level of the card
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the colour of the card.
     *
     * @return the colour of the card
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Returns the production input list of the card.
     *
     * @return the production input list of the card
     */
    public List<ResourceQuantity> getProductionInput() {
        return new ArrayList<>(productionInput);
    }

    /**
     * Returns the production output list of the card.
     *
     * @return the production output list of the card.
     */
    public List<ResourceQuantity> getProductionOutput() {
        return new ArrayList<>(productionOutput);
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof DevCard)) return false;
        DevCard devCard = (DevCard) o;
        if (devCard.getColour() != colour || devCard.getLevel() != level || devCard.getVictoryPoints() != getVictoryPoints())
            return false;
        return checkList(devCard.getResourceRequirements(), getResourceRequirements()) &&
                checkList(devCard.getProductionInput(), getProductionInput()) &&
                checkList(devCard.getProductionOutput(), getProductionOutput());
    }

    /**
     * Checks if two lists of resources are equivalent. Used in the equals method
     * of this class.
     *
     * @param thoseRes the list of resources of the card that is compared to this card object
     * @param theseRes the list of resources of this card
     * @return {@code true} if the lists are equivalent, {@code false} otherwise
     */
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
