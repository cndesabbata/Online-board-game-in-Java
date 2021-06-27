package it.polimi.ingsw.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class Card represents a card of the game.
 *
 */
public abstract class Card implements Serializable {
    private final List<ResourceQuantity> resourceRequirements;
    private final int victoryPoints;
    private final String url;

    /**
     * Constructor used by classes that extend this abstract class.
     *
     * @param resourceRequirements list of resource requirements for the card
     * @param victoryPoints        the amount of victory point granted by the card
     * @param url                  the url of the image corresponding to the card
     */
    public Card(List<ResourceQuantity> resourceRequirements, int victoryPoints, String url) {
        this.resourceRequirements = resourceRequirements;
        this.victoryPoints = victoryPoints;
        this.url = url;
    }

    /**
     * Returns the resource requirements list of the card.
     *
     * @return the resource requirements list if the card has one,
     * returns {@code null} otherwise
     */
    public List<ResourceQuantity> getResourceRequirements() {
        if (resourceRequirements == null) return null;
        return new ArrayList<>(resourceRequirements);
    }

    /**
     * Returns the victory points granted by the card.
     *
     * @return the victory points granted by the card
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Returns the url of the image corresponding to the card.
     *
     * @return the url of the image corresponding to the card
     */
    public String getUrl() {
        return url;
    }
}