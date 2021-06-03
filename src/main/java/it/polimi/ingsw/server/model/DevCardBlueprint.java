package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;

public class DevCardBlueprint {
    private final int victoryPoints;
    private final int[] requiredResources;
    private final int[] inputResources;
    private final int[] outputResources;
    private final String url;

    public DevCardBlueprint(int victoryPoints, int[] requiredResources, int[] inputResources, int[] outputResources, String url) {
        this.victoryPoints = victoryPoints;
        this.requiredResources = requiredResources;
        this.inputResources = inputResources;
        this.outputResources = outputResources;
        this.url = url;
    }

    public DevCard BuildCard (int level, Colour colour){
        List<ResourceQuantity> requiredRes = new ArrayList<>();
        List<ResourceQuantity> inputRes = new ArrayList<>();
        List<ResourceQuantity> outputRes = new ArrayList<>();
        BuildResources(requiredRes, requiredResources);
        BuildResources(inputRes, inputResources);
        BuildResources(outputRes, outputResources);
        return new DevCard(requiredRes, victoryPoints, level, colour, inputRes, outputRes, url);
    }

    private void BuildResources(List<ResourceQuantity> resourceList, int[] array){
        for (int i = 0; i < array.length; i++){
            if (array[i] != 0) resourceList.add(new ResourceQuantity(array[i], Resource.values()[i]));
        }
    }
}
