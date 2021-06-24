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

/**
 * Class Chest represents the player's chest on the game board.
 * Resource stored in the chest are represented as a list of
 * {@link ResourceQuantity} objects.
 *
 */
public class Chest extends Observable {
    private final List<ResourceQuantity> chest;
    private final String owner;

    /**
     * Default constructor.
     *
     * @param nickname the nickname of the player who owns this Chest object
     */
    public Chest(String nickname){
        owner = nickname;
        chest = new ArrayList<>();
        for(int i = 0; i < Resource.values().length - 2; i++){                                                          //faithpoint and empty are not storable in the Chest.
            chest.add(new ResourceQuantity(0, Resource.values()[i]));
        }
        notifyObservers(new NewChest(chest, owner));
    }

    /**
     * Notifies a player's virtual view with a {@link NewChest} message. Used when a
     * player is reconnecting to a game.
     *
     * @param nickname the nickname of the player to notify
     */
    public void notifyNew(String nickname){
        notifySingleObserver(new NewChest(chest, owner), nickname);
    }

    /**
     * Returns a copy of the chest.
     *
     * @return a copy of the chest
     */
    public List<ResourceQuantity> getChest() {
        return new ArrayList<>(chest);
    }

    /**
     * Removes resources from the chest. Safety is guaranteed if
     * this method is called after {@link #checkDecrement(List)}
     * if the latter does not throw an exception.
     *
     * @param inputRes the list of resources that need to be removed
     */
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

    /**
     * Checks if the resources contained in the provided list can be removed
     * from the chest.
     *
     * @param inputRes the list of resources to remove from the chest
     * @throws WrongActionException when the chest does not have enough resources to remove or when the provided list
     * contains an empty resource
     */
    public void checkDecrement(List <ResourcePosition> inputRes) throws WrongActionException {
        if(inputRes.stream().anyMatch(Rp -> Rp.getResource() == Resource.EMPTY))
            throw new WrongActionException("EMPTY resource is not removable. ");
        else {
            for (ResourcePosition Rp : inputRes) {
                if(Rp.getPlace() == Place.CHEST) {
                    if (inputRes.stream().filter(R -> R.getResource() == Rp.getResource() && R.getPlace() == Place.CHEST).   //if the required number of resources (of a certain type) is more then the actual value of that type of resources in chest
                        map(ResourcePosition::getQuantity).reduce(0, Integer::sum) > chest.get(getIndexResource(Rp.getResource())).getQuantity())
                        throw new WrongActionException("The resources in chest are not enough. ");
                }
            }
        }
    }

    /**
     * Adds resources to the chest. Safety is guaranteed if this
     * method is called after {@link #checkIncrement(List)} if
     * the latter does not throw an exception.
     *
     * @param inputRes the list of resources that need to be added
     */
    public void incrementResource(List <ResourcePosition> inputRes) {
        inputRes.removeIf(Rp -> Rp.getResource() == Resource.FAITHPOINT);
        for(ResourcePosition Rp : inputRes){
            int index = getIndexResource(Rp.getResource());
            ResourceQuantity Rq = chest.get(index);
            Rq.setQuantity(Rq.getQuantity() + Rp.getQuantity());
        }
        notifyObservers(new NewChest(chest, owner));
    }

    /**
     * Checks if the resources contained in the provided list can be added to the chest.
     *
     * @param outputRes the list of resources to add to the chest
     * @throws WrongActionException when the list of {@link ResourcePosition} contains an object which is not meant to
     * be stored in the chest or when trying to add an empty resource
     */
    public void checkIncrement(List <ResourcePosition> outputRes) throws WrongActionException{                          //it is used only by the checkAction in StartProduction
        for(ResourcePosition Rp : outputRes){
            if(Rp.getResource() == Resource.EMPTY) throw new WrongActionException("EMPTY resource cannot be stored. ");
            else if(Rp.getPlace() != Place.CHEST) throw new WrongActionException("All the resources must be stored in the CHEST. ");
        }
    }

    /*controls if there is a certain quantity (or more) of a certain resource
    public boolean checkQuantity (Resource resource, int quantity){
        int index = this.getIndexResource(resource);
        return  chest.get(index).getQuantity() >= quantity;
    }*/

    /**
     * Returns the amount of resources of the type provided stored in the chest.
     *
     * @param resource the type of resource
     * @return the amount of resources of the desired type stored in the chest
     */
    public int getAvailability(Resource resource){
        int supply = 0;
        for (ResourceQuantity res : chest){
            if (res.getResource() == resource) supply += res.getQuantity();
        }
        return supply;
    }

    /**
     * Returns the index of the node in the which stores a certain resource.
     *
     * @param resource the type of resource
     * @return the index of the node in the list
     */
    private int getIndexResource(Resource resource){
        int index;
        for(index = 0; index < chest.size(); index++){
            if(chest.get(index).getResource() == resource)
                break;
        }
        return index;
    }
}

