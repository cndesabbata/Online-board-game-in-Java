package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class DevCardBlueprint is an auxiliary class used to build development
 * card from the json files.
 *
 */
public class DevCardBlueprint {
    private final int victoryPoints;
    private final int[] requiredResources;
    private final int[] inputResources;
    private final int[] outputResources;
    private final String url;

    /**
     * Default constructor. Parameters are retrieved from the json files.
     *
     * @param victoryPoints     the amount of victory points granted by the card
     * @param requiredResources the array of required resources of the card
     * @param inputResources    the array of input resources of the card
     * @param outputResources   the array of output resources of the card
     * @param url
     */
    public DevCardBlueprint(int victoryPoints, int[] requiredResources, int[] inputResources, int[] outputResources, String url) {
        this.victoryPoints = victoryPoints;
        this.requiredResources = requiredResources;
        this.inputResources = inputResources;
        this.outputResources = outputResources;
        this.url = url;
    }

    /**
     * Creates a new DevCard object of the level and colour specified.
     *
     * @param level  the level of the card
     * @param colour the colour of the card
     * @return the created development card
     */
    public DevCard BuildCard (int level, Colour colour){
        List<ResourceQuantity> requiredRes = new ArrayList<>();
        List<ResourceQuantity> inputRes = new ArrayList<>();
        List<ResourceQuantity> outputRes = new ArrayList<>();
        BuildResources(requiredRes, requiredResources);
        BuildResources(inputRes, inputResources);
        BuildResources(outputRes, outputResources);
        return new DevCard(requiredRes, victoryPoints, level, colour, inputRes, outputRes, url);
    }

    /**
     * Fills the list of resources using the information contained in the arrays
     * retrieved from the json files.
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
