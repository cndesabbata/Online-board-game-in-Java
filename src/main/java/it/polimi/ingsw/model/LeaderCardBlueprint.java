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
        Resource resourceType;
        switch (resource){
            case "coin": resourceType = Resource.COIN; break;
            case "stone": resourceType = Resource.STONE; break;
            case "servant": resourceType = Resource.SERVANT; break;
            default /* shield */: resourceType = Resource.SHIELD; break;
        }
        if (hasResourceRequirements) BuildResources(resourceQuantities, resourceRequirements);
        else{
            AddRequiredDevCard(cardsReqs, firstCardColour, firstCardLevel);
            if (secondCardLevel != 0){
                AddRequiredDevCard(cardsReqs, secondCardColour, secondCardLevel);
            }
        }
        switch (type) {
            case "Discount": return new DiscountLeader(victoryPoints, false, cardsReqs, resourceType);
            case "Depot": return new DepotLeader(resourceQuantities, victoryPoints, false, resourceType);
            case "Marble": return new MarbleLeader(victoryPoints, false, cardsReqs, resourceType);
            default /* Product */: return new ProductLeader(victoryPoints, false, cardsReqs, resourceType);
        }
    }

    private void AddRequiredDevCard(List<DevCard> cardsReqs, String secondCardColour, int secondCardLevel) {
        Colour colour;
        switch (secondCardColour){
            case "yellow": colour = Colour.YELLOW; break;
            case "blue": colour = Colour.BLUE; break;
            case "green": colour = Colour.GREEN; break;
            case "purple": colour = Colour.PURPLE; break;
            default:
                throw new IllegalStateException("Unexpected value: " + firstCardColour);
        }
        cardsReqs.add(new DevCard(secondCardLevel, colour));
    }

    private void BuildResources(List<ResourceQuantity> resourceList, int[] array){
        if (array[0] != 0) resourceList.add(new ResourceQuantity(array[0], Resource.COIN));
        if (array[1] != 0) resourceList.add(new ResourceQuantity(array[1], Resource.STONE));
        if (array[2] != 0) resourceList.add(new ResourceQuantity(array[2], Resource.SERVANT));
        if (array[3] != 0) resourceList.add(new ResourceQuantity(array[3], Resource.SHIELD));
    }
}
