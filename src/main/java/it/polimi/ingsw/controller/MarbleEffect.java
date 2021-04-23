package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.model.*;

import java.util.List;

public class MarbleEffect implements LeaderEffect {
    private final int whiteMarbles;
    private final Resource resource;
    private final List<ResourcePosition> extraResources;

    public MarbleEffect(int whiteMarbles, Resource resource, List<ResourcePosition> extraResources) {
        this.whiteMarbles = whiteMarbles;
        this.resource = resource;
        this.extraResources = extraResources;
    }

    @Override
    public void doLeaderEffect(Player player, Action action) throws WrongActionException {
        if(action instanceof BuyResources) {
            if(!(player.hasPlayedLeaderCard(LeaderType.MARBLE, resource)))
                throw new WrongActionException("The player does not have the played leadCard");
            BuyResources buyResources = (BuyResources) action;
            if(extraResources.size() != whiteMarbles)
                throw new WrongActionException("Number of extra resources does not match the number of white marbles converted");
            for(ResourcePosition resourcePosition : extraResources) {
                if (resourcePosition.getResource() != resource)
                    throw new WrongActionException("Extra resources must match marble leaders' resource");
            }
            for(ResourcePosition extraRes : extraResources) {
                buyResources.setExtraRes(extraRes);
            }
            buyResources.setLeaderUsed(true);
        }
    }
}
