package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;

import java.util.ArrayList;

public class MarbleEffect implements LeaderEffect {
    private int whiteMarbles;
    private Resource resource;
    //private List<Destinations> resourceDests;

    public MarbleEffect(int whiteMarbles, Resource resource) {
        this.whiteMarbles = whiteMarbles;
        this.resource = resource;
    }
    public void doLeaderEffect(Player player, Action action) throws ErrorException{
        if(!(action instanceof BuyResources))
            return;
        /*
        else{
            if(!(player.hasLeaderCard("marble", resource)))
                throw new ErrorException();
            else {
                BuyResources buyResources = (BuyResources) action;
                List<ResourceDest> addResourceDest = new ArrayList<>();
                List<ResourceDest> ResourceDest = buyResources.getResourceDest();
                for(ResourceDest resourceDest : buyResources.getResourceDest()) {

                }
            }
        }
        */
    }
}
