package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class LeaderCardBlueprint {
    private final String type;
    private final boolean hasResourceRequirements;
    private final int victoryPoints;
    private final int[] resourceRequirements;
    private final int firstCardLevel;
    private final int secondCardLevel;
    private final String firstCardColour;
    private final String secondCardColour;
    private final String resource;

    public LeaderCardBlueprint(String type, boolean hasResourceRequirements, int victoryPoints,
                               int[] resourceRequirements, int firstCardLevel, int secondCardLevel,
                               String firstCardColour, String secondCardColour, String resource) {
        this.type = type;
        this.hasResourceRequirements = hasResourceRequirements;
        this.victoryPoints = victoryPoints;
        this.resourceRequirements = resourceRequirements;
        this.firstCardLevel = firstCardLevel;
        this.secondCardLevel = secondCardLevel;
        this.firstCardColour = firstCardColour;
        this.secondCardColour = secondCardColour;
        this.resource = resource;
    }

    public LeaderCard BuildCard (){
        List<ResourceQuantity> resourceQuantities = new ArrayList<>();
        List<DevCard> cardsReqs = new ArrayList<>();
        Resource resourceType = Resource.valueOf(resource);
        if (hasResourceRequirements) BuildResources(resourceQuantities, resourceRequirements);
        else{
            cardsReqs.add(new DevCard(firstCardLevel, Colour.valueOf(firstCardColour)));
            if (secondCardLevel != 0){
                cardsReqs.add(new DevCard(secondCardLevel, Colour.valueOf(secondCardColour)));
            }
        }
        switch (type) {
            case "Discount": return new DiscountLeader(victoryPoints, false, cardsReqs, resourceType);
            case "Depot": return new DepotLeader(resourceQuantities, victoryPoints, false, resourceType);
            case "Marble": return new MarbleLeader(victoryPoints, false, cardsReqs, resourceType);
            default /* Product */: return new ProductLeader(victoryPoints, false, cardsReqs, resourceType);
        }
    }

    private void BuildResources(List<ResourceQuantity> resourceList, int[] array){
        if (array[0] != 0) resourceList.add(new ResourceQuantity(array[0], Resource.COIN));
        if (array[1] != 0) resourceList.add(new ResourceQuantity(array[1], Resource.STONE));
        if (array[2] != 0) resourceList.add(new ResourceQuantity(array[2], Resource.SERVANT));
        if (array[3] != 0) resourceList.add(new ResourceQuantity(array[3], Resource.SHIELD));
    }
}
