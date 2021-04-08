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
        List<ResourceReq> requiredRes = new ArrayList<>();
        List<ResourceReq> inputRes = new ArrayList<>();
        List<ResourceReq> outputRes = new ArrayList<>();
        BuildResources(requiredRes, requiredResources);
        BuildResources(inputRes, inputResources);
        BuildResources(outputRes, outputResources);
        if (outputResources[4] != 0) outputRes.add(new ResourceReq(outputResources[4], Resource.FAITHPOINT));
        return new DevCard(requiredRes, victoryPoints, level, colour, inputRes, outputRes);
    }

    private void BuildResources(List<ResourceReq> resourceList, int[] array){
        if (array[0] != 0) resourceList.add(new ResourceReq(array[0], Resource.COIN));
        if (array[1] != 0) resourceList.add(new ResourceReq(array[1], Resource.STONE));
        if (array[2] != 0) resourceList.add(new ResourceReq(array[2], Resource.SERVANT));
        if (array[3] != 0) resourceList.add(new ResourceReq(array[3], Resource.SHIELD));
    }
}
