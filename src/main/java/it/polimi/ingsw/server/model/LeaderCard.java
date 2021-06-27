package it.polimi.ingsw.server.model;

import java.io.Serializable;
import java.util.List;

/**
 * LeaderCard class represents a leader card.
 *
 */
public class LeaderCard extends Card implements Serializable {
    private final List<DevCard> cardRequirements;
    private final Resource resource;
    private final LeaderType type;

    /**
     * Creates a leader card that has resource requirements.
     *
     * @param resourceRequirements the resource requirements list of the card
     * @param victoryPoints        the amount of victory points granted by the card
     * @param resource             the resource associated with the card
     * @param type                 the leader type of the card
     * @param url                  the url of the image corresponding to the card
     */
    public LeaderCard(List<ResourceQuantity> resourceRequirements, int victoryPoints,
                      Resource resource, LeaderType type, String url) {
        super(resourceRequirements, victoryPoints, url);
        this.cardRequirements = null;
        this.resource = resource;
        this.type = type;
    }

    /**
     * Creates a leader card that has card requirements.
     *
     * @param victoryPoints    the amount of victory points granted by the card
     * @param cardRequirements the card requirements list of the card
     * @param resource         the resource associated with the card
     * @param type             the leader type of the card
     * @param url              the url of the image corresponding to the card
     */
    public LeaderCard(int victoryPoints, List<DevCard> cardRequirements,
                      Resource resource, LeaderType type, String url) {
        super(null, victoryPoints, url);
        this.cardRequirements = cardRequirements;
        this.resource = resource;
        this.type = type;
    }

    /**
     * Returns the resource associated with the card.
     *
     * @return the resource associated with the card
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Returns the leader type of the card.
     *
     * @return the leader type of the card
     */
    public LeaderType getType() {
        return type;
    }

    /**
     * Returns the list of card requirements
     *
     * @return the list of card requirements
     */
    public List<DevCard> getCardRequirements() {
        return cardRequirements;
    }

}
