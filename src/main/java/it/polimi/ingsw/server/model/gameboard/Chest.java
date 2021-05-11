package it.polimi.ingsw.server.model.gameboard;

import it.polimi.ingsw.messages.serverMessages.newElement.NewChest;
import it.polimi.ingsw.server.controller.Place;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.ResourcePosition;
import it.polimi.ingsw.server.model.ResourceQuantity;
import it.polimi.ingsw.server.observer.Observable;

import java.util.ArrayList;
import java.util.List;

public class Chest extends Observable {
    private final List<ResourceQuantity> chest;
    private String owner;

    public Chest(String nickname){
        owner = nickname;
        chest = new ArrayList<>();
        for(int i = 0; i < Resource.values().length - 2; i++){                                                          //faithpoint and empty are not storable in the Chest.
            chest.add(new ResourceQuantity(0, Resource.values()[i]));
        }
        notifyObservers(new NewChest(chest, owner));
    }

    /*returns a copy of the chest*/
    public List<ResourceQuantity> getChest() {
        return new ArrayList<>(chest);
    }

    /*removes resources from the chest*/
    public void decrementResource(List <ResourcePosition> inputRes) {
        List <ResourcePosition> removableRes = new ArrayList<>(inputRes);
        removableRes.removeIf(Rp -> Rp.getPlace() != Place.CHEST);
        for(ResourcePosition Rp : removableRes){
            int index = getIndexResource(Rp.getResource());
            ResourceQuantity Rq = chest.get(index);
            Rq.setQuantity(Rq.getQuantity() - Rp.getQuantity());
        }
        notifyObservers(new NewChest(chest, owner));
    }

    /*controls if the resources can be removed*/
    public void checkDecrement(List <ResourcePosition> inputRes) throws WrongActionException {
        if(inputRes.stream().anyMatch(Rp -> Rp.getResource() == Resource.EMPTY))
            throw new WrongActionException("EMPTY resource is not removable.");
        else {
            for (ResourcePosition Rp : inputRes) {
                if(Rp.getPlace() == Place.CHEST) {
                    if (inputRes.stream().filter(R -> R.getResource() == Rp.getResource() && R.getPlace() == Place.CHEST).   //if the required number of resources (of a certain type) is more then the actual value of that type of resources in chest
                        map(ResourcePosition::getQuantity).reduce(0, Integer::sum) > chest.get(getIndexResource(Rp.getResource())).getQuantity())
                        throw new WrongActionException("The resources in chest are not enough.");
                }
            }
        }
    }

    /*add resources from the chest*/
    public void incrementResource(List <ResourcePosition> inputRes) {
        for(ResourcePosition Rp : inputRes){
            int index = getIndexResource(Rp.getResource());
            ResourceQuantity Rq = chest.get(index);
            Rq.setQuantity(Rq.getQuantity() + Rp.getQuantity());
        }
        notifyObservers(new NewChest(chest, owner));
    }

    /*controls if the resources can be stored*/
    public void checkIncrement(List <ResourcePosition> outputRes) throws WrongActionException{                          //it is used only by the checkAction in StartProduction
        for(ResourcePosition Rp : outputRes){
            if(Rp.getResource() == Resource.EMPTY) throw new WrongActionException("EMPTY resource cannot be stored.");
            else if(Rp.getPlace() != Place.CHEST) throw new WrongActionException("All the resources must be stored in the CHEST.");
        }
    }

    /*controls if there is a certain quantity (or more) of a certain resource
    public boolean checkQuantity (Resource resource, int quantity){
        int index = this.getIndexResource(resource);
        return  chest.get(index).getQuantity() >= quantity;
    }*/

    /*returns the number of resources of the same type of resource*/
    public int getAvailability(Resource resource){
        int supply = 0;
        for (ResourceQuantity res : chest){
            if (res.getResource() == resource) supply += res.getQuantity();
        }
        return supply;
    }

    /*returns the index of the node of chest which stores resource*/
    private int getIndexResource(Resource resource){
        int index;
        for(index = 0; index < chest.size(); index++){
            if(chest.get(index).getResource() == resource)
                break;
        }
        return index;
    }
}

