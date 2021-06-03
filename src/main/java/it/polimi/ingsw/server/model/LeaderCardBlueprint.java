package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;

public class LeaderCardBlueprint {
    private final String type;
    private final boolean hasResourceRequirements;
    private final int victoryPoints;
    private final int[] resourceRequirements;
    private final int firstCardLevel;
    private final int secondCardLevel;
    private final int thirdCardLevel;
    private final String firstCardColour;
    private final String secondCardColour;
    private final String thirdCardColour;
    private final String resource;
    private final String url;

    public LeaderCardBlueprint(String type, boolean hasResourceRequirements, int victoryPoints,
                               int[] resourceRequirements, int firstCardLevel, int secondCardLevel,
                               int thirdCardLevel, String firstCardColour, String secondCardColour,
                               String thirdCardColour, String resource, String url) {
        this.type = type;
        this.hasResourceRequirements = hasResourceRequirements;
        this.victoryPoints = victoryPoints;
        this.resourceRequirements = resourceRequirements;
        this.firstCardLevel = firstCardLevel;
        this.secondCardLevel = secondCardLevel;
        this.thirdCardLevel = thirdCardLevel;
        this.firstCardColour = firstCardColour;
        this.secondCardColour = secondCardColour;
        this.thirdCardColour = thirdCardColour;
        this.resource = resource;
        this.url = url;
    }

    public LeaderCard BuildCard (){
        List<ResourceQuantity> resQuantities = new ArrayList<>();
        List<DevCard> cardsReqs = new ArrayList<>();
        Resource resourceType = Resource.valueOf(resource.toUpperCase());
        if (hasResourceRequirements) BuildResources(resQuantities, resourceRequirements);
        else{
            cardsReqs.add(new DevCard(firstCardLevel, Colour.valueOf(firstCardColour.toUpperCase())));
            if (secondCardLevel != -1){
                cardsReqs.add(new DevCard(secondCardLevel, Colour.valueOf(secondCardColour.toUpperCase())));
            }
            if (thirdCardLevel != -1){
                cardsReqs.add(new DevCard(thirdCardLevel, Colour.valueOf(thirdCardColour.toUpperCase())));
            }
        }
        if (hasResourceRequirements)
            return new LeaderCard(resQuantities, victoryPoints, resourceType, LeaderType.valueOf(type.toUpperCase()), url);
        else
            return new LeaderCard(victoryPoints, cardsReqs, resourceType, LeaderType.valueOf(type.toUpperCase()), url);
    }

    private void BuildResources(List<ResourceQuantity> resourceList, int[] array){
        for (int i = 0; i < array.length; i++){
            if (array[i] != 0) resourceList.add(new ResourceQuantity(array[i], Resource.values()[i]));
        }
    }
}
