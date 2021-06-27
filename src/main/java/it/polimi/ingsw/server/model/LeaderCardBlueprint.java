package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class LeaderCardBlueprint is an auxiliary class used to build a leader
 * card from the json files.
 *
 */
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

    /**
     * Default constructor. Parameters are retrieved from the json file.
     *
     * @param type                    the leader type of the card
     * @param hasResourceRequirements true if the card has resource requirements, false otherwise
     * @param victoryPoints           the amount of victory points granted by the card
     * @param resourceRequirements    the array of resource requirements of the card
     * @param firstCardLevel          the level of the first required card
     * @param secondCardLevel         the level of the second required card
     * @param thirdCardLevel          the level of the third required card
     * @param firstCardColour         the colour of the first required card
     * @param secondCardColour        the colour of the second required card
     * @param thirdCardColour         the colour of the third required card
     * @param resource                the resource associated with the card
     * @param url                     the url of the image corresponding to the card
     */
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

    /**
     * Creates a new leader card object. Note that if the card does not have
     * card requirements, the level of these required cards is set to -1 in
     * the json file. If the card has card requirements but the level is not
     * specified, the level of these required cards is set to 0 in the json
     * file.
     *
     * @return the created leader card
     */
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

    /**
     * Fills the list of resources using the information contained in the arrays
     * retrieved from the json file.
     *
     * @param resourceList the empty list of resource to fill
     * @param array        the array that contains information about the resources
     */
    private void BuildResources(List<ResourceQuantity> resourceList, int[] array){
        for (int i = 0; i < array.length; i++){
            if (array[i] != 0) resourceList.add(new ResourceQuantity(array[i], Resource.values()[i]));
        }
    }
}
