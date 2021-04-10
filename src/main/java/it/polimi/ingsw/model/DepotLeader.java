package it.polimi.ingsw.model;

import sun.jvm.hotspot.types.WrongTypeException;

import java.util.List;
import java.util.Arrays;

public class DepotLeader extends LeaderCard{
    private final List<Boolean> depot;
    private final LeaderType type = LeaderType.DEPOT;
    private final Resource resourceType;

    public DepotLeader(List<ResourceQuantity> resourceRequirements, int victoryPoints, boolean state, Resource resourceType) {
        super(resourceRequirements, victoryPoints, state);
        this.resourceType = resourceType;
        this.depot = Arrays.asList(false, false);
    }

    public List<Boolean> getDepot() {
        return depot;
    }

    public Resource getResourceType() {
        return resourceType;
    }

    public void storeInDepot(Resource type) throws DepotException, WrongTypeException {
        if (type!= resourceType) throw new WrongTypeException("The depot is meant for a different resource");
        if (!depot.contains(false)) throw new DepotException("The depot is already full");
        else if (depot.contains(true)) depot.set(1, true);
        else depot.set(0, true);
    }

    public Resource getFromDepot() throws DepotException{
        if(!depot.contains(true)) throw new DepotException("The depot is empty");
        else depot.set(depot.lastIndexOf(true), false);
        return resourceType;
    }

    private static class DepotException extends Exception {
        public DepotException(String message) {
            super(message);
        }
    }

    public LeaderType getType() {
        return type;
    }
}
