package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.DevCard;
import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.ResourceQuantity;

import java.util.ArrayList;
import java.util.List;

public class LeadCardInfo {
    private final int victoryPoints;
    private final List<String> resourceRequirements;
    private final List<DevCardInfo> cardRequirements;
    private final String type;
    private final String resource;
    private final String url;


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
    public int getVictoryPoints() {
        return victoryPoints;
    }

    public List<String> getResourceRequirements() {
        return resourceRequirements;
    }

    public List<DevCardInfo> getCardRequirements() {
        return cardRequirements;
    }

    public String getType() {
        return type;
    }

    public String getResource() {
        return resource;
    }

    public String getUrl() {
        return url;
    }
}
