package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.DevCard;
import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.ResourceQuantity;

import java.util.ArrayList;
import java.util.List;

/**
 * Class LeadCardInfo is a simplified representation of a leader card.
 *
 */
public class LeadCardInfo {
    private final int victoryPoints;
    private final List<String> resourceRequirements;
    private final List<DevCardInfo> cardRequirements;
    private final String type;
    private final String resource;
    private final String url;

    /**
     * Creates a new LeadCardInfo instance.
     *
     * @param l the leader card the new instance will represent
     */
    public LeadCardInfo(LeaderCard l){
        this.victoryPoints = l.getVictoryPoints();
        this.type = l.getType().toString();
        this.resource = l.getResource().toString();
        this.url = l.getUrl();
        if (l.getCardRequirements() == null){
            this.resourceRequirements = ResourceQuantity.toStringList(ResourceQuantity.flatten(l.getResourceRequirements()));
            this.cardRequirements = null;
        }
        else{
            this.cardRequirements = new ArrayList<>();
            for (DevCard c : l.getCardRequirements())
                this.cardRequirements.add(new DevCardInfo(c));
            this.resourceRequirements = null;
        }
    }

    /**
     * Returns the amount of victory points granted by this card.
     *
     * @return the amount of victory points granted by this card
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Returns the list of resources required to play the card.
     *
     * @return the list of resources required to play the card
     */
    public List<String> getResourceRequirements() {
        return resourceRequirements;
    }

    /**
     * Returns the list of development cards required to play the card.
     *
     * @return the list of development cards required to play the card
     */
    public List<DevCardInfo> getCardRequirements() {
        return cardRequirements;
    }

    /**
     * Returns the type of the leader card.
     *
     * @return the type of the leader card
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the type of the resource associated with this card.
     *
     * @return the type of the resource associated with this card
     */
    public String getResource() {
        return resource;
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
