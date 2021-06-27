package it.polimi.ingsw.server.model.gameboard;

import it.polimi.ingsw.messages.serverMessages.newElement.NewDevSpace;
import it.polimi.ingsw.messages.serverMessages.newElement.NewWarehouse;
import it.polimi.ingsw.server.controller.Place;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.ResourcePosition;
import it.polimi.ingsw.server.model.ResourceQuantity;
import it.polimi.ingsw.server.observer.Observable;

import java.util.*;

/**
 * Class Warehouse represents the warehouse on the game board. Its shelves
 * are represented as a list of {@link ResourceQuantity} objects.
 * Note that a depot leader is considered a shelf of the warehouse.
 *
 */
public class Warehouse extends Observable {
    private final List<ResourceQuantity> warehouse = new ArrayList<>();
    private final int initialDim;
    private final String owner;

    /**
     * Default constructor. Creates a warehouse object with a specified amount of
     * initial shelves.
     *
     * @param warehouseDim the initial number of shelves
     * @param nickname     the nickname of the player who owns this warehouse object
     */
    public Warehouse(int warehouseDim, String nickname){
        owner = nickname;
        for(int i = 0; i < warehouseDim; i++){
            warehouse.add(new ResourceQuantity(0, Resource.EMPTY));
        }
        initialDim = warehouseDim;
        notifyObservers(new NewWarehouse(warehouse, initialDim, owner));
    }

    /**
     * Notifies a player's virtual view with a {@link NewWarehouse} message. Used when a
     * player is reconnecting to a game.
     *
     * @param nickname the nickname of the player to notify
     */
    public void notifyNew(String nickname){
        notifySingleObserver(new NewWarehouse(warehouse, initialDim, owner), nickname);
    }

    /**
     * Returns a copy of the warehouse.
     *
     * @return the list of ResourceQuantity objects representing the shelves of the warehouse
     */
    public List<ResourceQuantity> getWarehouse(){
        return new ArrayList<>(warehouse);
    }

    /**
     * Returns the copy of a shelf of the warehouse, also considering depot leaders.
     *
     * @param numOfShelf the shelf to copy
     * @return the copy of the specified shelf
     */
    public ResourceQuantity getShelf(NumOfShelf numOfShelf) {
        ResourceQuantity shelf = warehouse.get(numOfShelf.ordinal());
        return new ResourceQuantity(shelf.getQuantity(), shelf.getResource());
    }

    /**
     * Adds resources in the shelves from a specified list of ResourcePosition objects.
     * Safety is guaranteed if this method is called after {@link #checkIncrement(List)},
     * if the latter does not throw an exception. It also notifies all the players'
     * virtual views with a {@link NewWarehouse} message.
     *
     * @param outputRes that list of resources that need to be added to the warehouse
     */
    public void incrementResource (List <ResourcePosition> outputRes) {
        List<ResourcePosition> storableRes = new ArrayList<>(outputRes);
        storableRes.removeIf(Rp -> Rp.getResource() == Resource.FAITHPOINT);                                            //faithpoints are not involved in this entire check
        storableRes.removeIf(Rp -> Rp.getPlace() == Place.TRASH_CAN);                                                   //Resources that should be discarded are not interested in this method.
        NumOfShelf numOfShelf;
        ResourceQuantity shelf;

        for(ResourcePosition Rp : storableRes){
            numOfShelf = Rp.getShelf();
            shelf = warehouse.get(numOfShelf.ordinal());
            if(shelf.getResource() == Resource.EMPTY) shelf.setResource(Rp.getResource());
            shelf.setQuantity(shelf.getQuantity() + Rp.getQuantity());                                                  //Rp.getQuantity() = 1
        }
        notifyObservers(new NewWarehouse(warehouse, initialDim, owner));
    }

    /**
     * Checks if the resources contained in the provided list can be added to the warehouse.
     *
     * @param outputRes the list of resources to add to the chest
     * @throws WrongActionException when one of the game rules of the warehouse is broken
     */
    public void checkIncrement(List <ResourcePosition> outputRes) throws WrongActionException {                         //used in the checkAction of BuyResources                                  //this method will be called only be the checkAction in BuyResources
        List<ResourcePosition> storableRes = new ArrayList<>(outputRes);
        storableRes.removeIf(Rp -> Rp.getResource() == Resource.FAITHPOINT);                                            //faithPoints are not interested by this entire check
        storableRes.removeIf(Rp -> Rp.getPlace() == Place.TRASH_CAN);                                                   //Resources that should be discarded are not interested in this method.
        //check on the storableRes itself
        for(int i = 0; i < storableRes.size(); i++){
            ResourcePosition Rp = storableRes.get(i);
            for(int j = i + 1; j < storableRes.size(); j++){
                if(storableRes.get(j).getResource() != Rp.getResource() && storableRes.get(j).getShelf() == Rp.getShelf())
                    throw new WrongActionException("Resources of different types cannot be stored in the same shelf. ");
                else if(storableRes.get(j).getResource() == Rp.getResource() && storableRes.get(j).getShelf() != Rp.getShelf()
                        && storableRes.get(j).getShelf().ordinal() < initialDim && Rp.getShelf().ordinal() < initialDim)//if the player wants to store 2 coins in the second shelf and 1 coin in the first one, that is an error. It is not an error if he wants to store 2 coins in the second shelf and 1 coin in the first depot
                    throw new WrongActionException("Resources of the same type cannot be stored in different shelves. ");
            }
        }
        NumOfShelf numOfShelf;
        ResourcePosition Rp;
        ResourceQuantity shelf;
        int dimShelf;
        //check on the disposition of the resources in the shelves
        for (int i = 0; i < storableRes.size(); i++) {
            Rp = storableRes.get(i);
            numOfShelf = Rp.getShelf();
            if (Rp.getPlace() == Place.CHEST)
                throw new WrongActionException("All the resources must be stored in the warehouse. ");
            else if (numOfShelf.ordinal() >= warehouse.size())
                throw new WrongActionException("Shelf " +numOfShelf+ " does not exist. ");
            else if (Rp.getResource() == Resource.EMPTY)
                throw new WrongActionException("The EMPTY resource is not storable. ");
            else {
                if (numOfShelf.ordinal() < initialDim) dimShelf = numOfShelf.ordinal() + 1;                             //dimension of a "normal" shelf
                else dimShelf = 2;                                                                                      //dimension of a "depot"
                shelf = warehouse.get(numOfShelf.ordinal());
                if (shelf.getResource() != Resource.EMPTY && shelf.getResource() != Rp.getResource())
                    throw new WrongActionException("Shelf " +numOfShelf+ " contains " +shelf.getResource()+ "s, not " +Rp.getResource()+ "s. ");
                else if (checkOtherShelves(Rp.getResource(), numOfShelf))                                              //example: if I want to add two coins in the third shelf, but there is already one coin in the second shelf, then I have to discard two coins.
                    throw new WrongActionException("There is already another shelf storing " +Rp.getResource().toString()+ "s and it is not shelf " +Rp.getShelf()+ ". ");
                else if (calculateQuantity(storableRes, Rp) > dimShelf - shelf.getQuantity())
                    throw new WrongActionException("Shelf " +numOfShelf+ " does not have enough space to store the indicated " +Rp.getResource()+ "s. ");
            }
        }
    }

    /**
     * Returns the number of resources contained in the provided list of the same
     * type as the ResourcePosition object provided that have the same NumOfShelf
     * attribute.
     *
     * @param inputRes the list of ResourcePosition objects
     * @param Rp       the ResourcePosition object the list will be compared to
     * @return the number of resources computed
     */
    private int calculateQuantity(List <ResourcePosition> inputRes, ResourcePosition Rp){                          //it computes the number of nodes in a given list of ResourcePosition that have the same Resource and numOfShelf
        int quantity = 0;
        for(ResourcePosition r : inputRes){
            if(r.getResource() == Rp.getResource() && r.getShelf() == Rp.getShelf()) quantity++;
        }
        return quantity;
    }

    /**
     * Removes resources from the shelves. Safety is guaranteed if this
     * method is called after {@link #checkDecrement(List)} if the
     * latter does not throw an exception. It also notifies all the
     * players' virtual views with a {@link NewWarehouse} message.
     *
     * @param inputRes the list of resources that need to be removed
     */
    public void decrementResource (List <ResourcePosition> inputRes) {
        decrementWithoutNotify(inputRes);
        notifyObservers(new NewWarehouse(warehouse, initialDim, owner));
    }

    /**
     * Checks if the resources contained in the provided list can be removed
     * from the warehouse.
     *
     * @param inputRes the list of resources to remove from the warehouse
     * @throws WrongActionException when one of the game rules of the warehouse is broken
     */
    public void checkDecrement(List<ResourcePosition> inputRes) throws WrongActionException {                                                   //used in checkAction of BuyDevCard, StartProduction.
        List <ResourcePosition> removableRes = new ArrayList<>(inputRes);
        removableRes.removeIf(Rp -> Rp.getPlace() != Place.WAREHOUSE);

        NumOfShelf numOfShelf;
        ResourcePosition Rp;
        ResourceQuantity shelf;

        for (int i = 0; i < removableRes.size(); i++) {
            numOfShelf = removableRes.get(i).getShelf();
            Rp = removableRes.get(i);
            if (numOfShelf.ordinal() >= warehouse.size())
                throw new WrongActionException("Shelf " +numOfShelf+ " does not exist. ");
            else if (Rp.getResource() == Resource.EMPTY)
                throw new WrongActionException("EMPTY resource is not removable. ");
            else {                                                                                                      //dimension of a "depot"
                shelf = warehouse.get(numOfShelf.ordinal());
                if (shelf.getResource() != Resource.EMPTY && shelf.getResource() != Rp.getResource())
                    throw new WrongActionException("There are not " +Rp.getResource()+ "s in shelf " +numOfShelf+ ". ");
                else if (calculateQuantity(removableRes, Rp) > shelf.getQuantity())
                    throw new WrongActionException("Shelf " +numOfShelf+ " does not have enough " +Rp.getResource()+ "s. ");
            }
        }
    }

    /**
     * Moves a certain number of resources from one shelf to another.
     *
     * @param srcShelf  the source shelf
     * @param destShelf the destination shelf
     * @param quantity  the amount of resources to move
     */
    public void moveResource (NumOfShelf srcShelf, NumOfShelf destShelf, int quantity){
        ResourceQuantity shelfSrc = warehouse.get(srcShelf.ordinal());
        List <ResourcePosition> inputRes = new ArrayList<>();
        List <ResourcePosition> outputRes = new ArrayList<>();
        for(int i = 0; i < quantity; i++) {
            inputRes.add(new ResourcePosition(shelfSrc.getResource(), Place.WAREHOUSE, srcShelf));
            outputRes.add(new ResourcePosition(shelfSrc.getResource(), Place.WAREHOUSE, destShelf));
        }
        decrementWithoutNotify(inputRes);
        incrementResource(outputRes);
    }

    /**
     * Removes resources from the shelves.
     *
     * @param inputRes the list of resources that need to be removed
     */
    private void decrementWithoutNotify(List <ResourcePosition> inputRes) {
        List<ResourcePosition> removableRes = new ArrayList<>(inputRes);
        removableRes.removeIf(Rp -> Rp.getPlace() != Place.WAREHOUSE);                                                 //Resources that should be discarded are not interested in this method.
        NumOfShelf numOfShelf;
        ResourceQuantity shelf;

        for (ResourcePosition Rp : removableRes) {
            numOfShelf = Rp.getShelf();
            shelf = warehouse.get(numOfShelf.ordinal());
            if (shelf.getQuantity() == 1 && numOfShelf.ordinal() < initialDim) shelf.setResource(Resource.EMPTY);
            shelf.setQuantity(shelf.getQuantity() - Rp.getQuantity());                                                  //Rp.getQuantity() = 1
        }
    }

    /**
     * Checks if a certain amount of resources can be moved from one shelf to another.
     *
     * @param srcShelf  the source shelf
     * @param destShelf the destination shelf
     * @param quantity  the amount of resources to move
     * @throws WrongActionException when one of the game rules of the warehouse is broken
     */
    public void checkMove (NumOfShelf srcShelf, NumOfShelf destShelf, int quantity) throws WrongActionException{
        if (srcShelf.ordinal() >= warehouse.size() || destShelf.ordinal() >= warehouse.size())
            throw new WrongActionException("One of the specified shelves does not exist.");
        else {
            ResourceQuantity shelfSrc = warehouse.get(srcShelf.ordinal());
            ResourceQuantity shelfDest = warehouse.get(destShelf.ordinal());
            int dimShelfDest;
            if (destShelf.ordinal() < initialDim)
                dimShelfDest = destShelf.ordinal() + 1;                                                                 //dimension of a "normal" shelf
            else dimShelfDest = 2;                                                                                      //dimension of a "depot"
            if (shelfSrc.getResource() == Resource.EMPTY)
                throw new WrongActionException(srcShelf+ " is empty. ");
            else if (shelfSrc.getQuantity() < quantity)
                throw new WrongActionException("The specified quantity is greater than the actual number of " +shelfSrc.getResource()+ "s in shelf " +srcShelf+ ". ");
            else if (srcShelf.ordinal() < initialDim && destShelf.ordinal() < initialDim && quantity != shelfSrc.getQuantity())
                throw new WrongActionException("When moving resources between the first " +initialDim+
                        " shelves, all the resources from the source shelf must be moved. ");
            else if (shelfDest.getResource() != Resource.EMPTY && shelfDest.getResource() != shelfSrc.getResource())                                                 //rule of same resource in the same shelf.
                throw new WrongActionException("Shelves " +srcShelf+ " and " +destShelf+ " store different resources. ");
            else if(srcShelf.ordinal() >= initialDim && checkMoveDepot(shelfSrc.getResource(), destShelf))
                throw new WrongActionException("There is already another shelf storing " +shelfSrc.getResource()+ "s. ");
            else if (dimShelfDest - shelfDest.getQuantity() < quantity)
                throw new WrongActionException("There is not enough space in shelf " +destShelf+ ". ");
        }
    }

    /**
     * Checks if there is another shelf storing a specified resource that is not a depot leader
     * and it's different from the shelf provided.
     *
     * @param resource the specified resource
     * @param shelf    the provided shelf
     * @return {@code true} if the condition is met, {@code false} otherwise
     */
    private boolean checkMoveDepot(Resource resource, NumOfShelf shelf){
        for(int i = 0; i < initialDim; i++){
            if(i != shelf.ordinal() && warehouse.get(i).getResource() == resource)
                return true;
        }
        return false;
    }

    /**
     * Checks if there is another shelf storing a specified resource that is not a depot leader
     * and it's different from the shelf provided.
     *
     * @param resource   the specified resource
     * @param numOfShelf the provided shelf
     * @return {@code true} if the condition is met, {@code false} otherwise
     */
    /*controls if there are some "normal" shelves storing resources of type resource, apart from numOfShelf*/
    private boolean checkOtherShelves(Resource resource, NumOfShelf numOfShelf){                                        //only used in addResource
        if(numOfShelf.ordinal() < initialDim){
            for(int i = 0; i < initialDim; i++) {
                if (i != numOfShelf.ordinal()) {
                    if (warehouse.get(i).getResource() == resource)
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds a shelf derived from the depot leader card. It also notifies all
     * the players' virtual views with a {@link NewWarehouse} message.
     *
     * @param resource the resource stored by the leader card
     */
    public void addDepot(Resource resource){
        warehouse.add(new ResourceQuantity(0, resource));
        notifyObservers(new NewWarehouse(warehouse, initialDim, owner));
    }

    /**
     * Checks if there is already a depot storing the same resource as the one provided.
     *
     * @param resource the provided resource
     * @return {@code true} if there is a depot storing the provided resource, {@code false} otherwise
     */
    public boolean checkDepot(Resource resource){
        boolean check = true;
        for(int i = initialDim; i < warehouse.size(); i++){
            if(warehouse.get(i).getResource() == resource){
                check = false;
                break;
            }
        }
        return check;
    }

    /**
     * Returns the amount of resources of the type provided stored in the warehouse.
     *
     * @param resource the type of resource
     * @return the amount of resources of the desired type stored in the warehouse
     */
    public int getAvailability(Resource resource){
        int supply = 0;
        for (ResourceQuantity shelf : warehouse){
            if (shelf.getResource() == resource) supply += shelf.getQuantity();
        }
        return supply;
    }

}
