package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class DevCardBlueprint {
    private final int victoryPoints;
    private final int[] requiredResources;
    private final int[] inputResources;
    private final int[] outputResources;

    public DevCardBlueprint(int victoryPoints, int[] requiredResources, int[] inputResources, int[] outputResources) {
        this.victoryPoints = victoryPoints;
        this.requiredResources = requiredResources;
        this.inputResources = inputResources;
        this.outputResources = outputResources;
    }

    public DevCard BuildCard (int level, Colour colour){
        List<ResourceQuantity> requiredRes = new ArrayList<>();
        List<ResourceQuantity> inputRes = new ArrayList<>();
        List<ResourceQuantity> outputRes = new ArrayList<>();
        BuildResources(requiredRes, requiredResources);
        BuildResources(inputRes, inputResources);
        BuildResources(outputRes, outputResources);
        if (outputResources[4] != 0) outputRes.add(new ResourceQuantity(outputResources[4], Resource.FAITHPOINT));
        return new DevCard(requiredRes, victoryPoints, level, colour, inputRes, outputRes);
    }

    private void BuildResources(List<ResourceQuantity> resourceList, int[] array){
        for (int i = 0; i < array.length; i++){
            if (i != 0) resourceList.add(new ResourceQuantity(i, Resource.values()[i]));
        }
    }
}
