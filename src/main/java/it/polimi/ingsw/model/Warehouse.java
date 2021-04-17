package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Place;

import java.util.*;

public class Warehouse {
    private  ArrayList<ResourceQuantity> warehouse = new ArrayList<ResourceQuantity>();
    private int initialDim;

    public Warehouse(int warehouseDim){
        for(int i = 0; i < warehouseDim; i++){
            warehouse.add(new ResourceQuantity(0, Resource.EMPTY));
        }
        initialDim = warehouseDim;
    }

    public ArrayList<ResourceQuantity> getWarehouse(){
        return new ArrayList<ResourceQuantity>(warehouse);
    }

    public ResourceQuantity getShelf(NumOfShelf numOfShelf) {                                                           //returns a shelf of the warehouse (considering also depots)
        ResourceQuantity shelf = warehouse.get(numOfShelf.ordinal());
        return new ResourceQuantity(shelf.getQuantity(), shelf.getResource());
    }

    public void incrementResource (ArrayList <ResourcePosition> outputRes) {
        ArrayList<ResourcePosition> storableRes = new ArrayList<>(outputRes);
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
    }

    public void checkIncrement(ArrayList <ResourcePosition> outputRes) throws WrongActionException {                    //used in the checkAction of BuyResources                                  //this method will be called only be the checkAction in BuyResources
        ArrayList<ResourcePosition> storableRes = new ArrayList<>(outputRes);
        storableRes.removeIf(Rp -> Rp.getResource() == Resource.FAITHPOINT);                                            //faithpoint are not interested by this entire check
        storableRes.removeIf(Rp -> Rp.getPlace() == Place.TRASH_CAN);                                                   //Resources that should be discarded are not interested in this method.
        //check on the storableRes itself
        for(int i = 0; i < storableRes.size(); i++){
            ResourcePosition Rp = storableRes.get(i);
            for(int j = i + 1; j < storableRes.size(); j++){
                if(storableRes.get(j).getResource() != Rp.getResource() && storableRes.get(j).getShelf() == Rp.getShelf())
                    throw new WrongActionException("The resources number " +i+ " and " +j+ " cannot be stored because they are different, but the player wanted to store them in the same shelf.");
                else if(storableRes.get(j).getResource() == Rp.getResource() && storableRes.get(j).getShelf() != Rp.getShelf())
                    throw new WrongActionException("The resources number " +i+ " and " +j+ " cannot be stored because they are of the same type, but the player wanted to store them in two different shelves.");
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
                throw new WrongActionException("All the resources must be stored in the warehouse.");
            else if (numOfShelf.ordinal() >= warehouse.size())
                throw new WrongActionException("The resource number " + i + " cannot be stored because the indicated shelf does not exist.");
            else if (Rp.getResource() == Resource.EMPTY)
                throw new WrongActionException("The empty resource is not storable.");
            else {
                if (numOfShelf.ordinal() < initialDim) dimShelf = numOfShelf.ordinal() + 1;                             //dimension of a "normal" shelf
                else dimShelf = 2;                                                                                      //dimension of a "depot"
                shelf = warehouse.get(numOfShelf.ordinal());
                if (shelf.getResource() != Resource.EMPTY && shelf.getResource() != Rp.getResource())
                    throw new WrongActionException("The resource number " + i + " cannot be stored because there is already another type of resource in the selected shelf.");
                else if (!checkOtherShelves(Rp.getResource(), numOfShelf))                                              //example: if I want to add two coins in the third shelf, but there is already one coin in the second shelf, then I have to discard two coins.
                    throw new WrongActionException("The resource number " + i + " cannot be stored because there is already another shelf storing the same type of resource.");
                else if (calculateQuantity(storableRes, Rp) > dimShelf - shelf.getQuantity())
                    throw new WrongActionException("The shelf number " + numOfShelf.toString() + " does not have enough space to store the indicated resources.");
            }
        }
    }

    private int calculateQuantity(ArrayList <ResourcePosition> inputRes, ResourcePosition Rp){                          //it computes the number of nodes in a given list of ResourcePosition that have the same Resource and numOfShelf
        int quantity = 0;
        for(ResourcePosition r : inputRes){
            if(r.getResource() == Rp.getResource() && r.getShelf() == Rp.getShelf()) quantity++;
        }
        return quantity;
    }

    public void decrementResource (ArrayList <ResourcePosition> inputRes) {
        ArrayList<ResourcePosition> removableRes = new ArrayList<>(inputRes);
        removableRes.removeIf(Rp -> Rp.getPlace() != Place.WHAREHOUSE);                                                 //Resources that should be discarded are not interested in this method.
        NumOfShelf numOfShelf;
        ResourceQuantity shelf;

        for(ResourcePosition Rp : removableRes){
            numOfShelf = Rp.getShelf();
            shelf = warehouse.get(numOfShelf.ordinal());
            if(shelf.getQuantity() == 1) shelf.setResource(Resource.EMPTY);
            shelf.setQuantity(shelf.getQuantity() - Rp.getQuantity());                                                  //Rp.getQuantity() = 1
        }
    }

    public void checkDecrement(ArrayList<ResourcePosition> inputRes) throws WrongActionException {                                                   //used in checkAction of BuyDevCard, StartProduction.
        ArrayList <ResourcePosition> removableRes = new ArrayList<>(inputRes);
        removableRes.removeIf(Rp -> Rp.getPlace() != Place.WHAREHOUSE);

        NumOfShelf numOfShelf;
        ResourcePosition Rp;
        ResourceQuantity shelf;
        int dimShelf;

        for (int i = 0; i < removableRes.size(); i++) {
            numOfShelf = removableRes.get(i).getShelf();
            Rp = removableRes.get(i);

            if (numOfShelf.ordinal() >= warehouse.size())
                throw new WrongActionException("The resource number " + i + " cannot be removed because the indicated shelf does not exist.");
            else if (Rp.getResource() == Resource.EMPTY)
                throw new WrongActionException("The resource number " + i + " cannot be removed because the empty resource is not storable.");
            else {
                if (numOfShelf.ordinal() < initialDim) dimShelf = numOfShelf.ordinal() + 1;                             //dimension of a "normal" shelf
                else dimShelf = 2;                                                                                      //dimension of a "depot"
                shelf = warehouse.get(numOfShelf.ordinal());
                if (shelf.getResource() != Rp.getResource())
                    throw new WrongActionException("The resource number " + i + " cannot be removed because the resource is not in the indicated shelf.");
                else if (calculateQuantity(removableRes, Rp) >= shelf.getQuantity())
                    throw new WrongActionException("The shelf number " +numOfShelf.toString()+ " does not have enough " +Rp.getResource().toString()+ "S.");
            }
        }
    }

    public void moveResource (NumOfShelf srcShelf, NumOfShelf destShelf){
        ResourceQuantity shelfSrc = warehouse.get(srcShelf.ordinal());
        ResourceQuantity shelfDest = warehouse.get(destShelf.ordinal());
        ArrayList <ResourcePosition> inputRes = new ArrayList<>();
        ArrayList <ResourcePosition> outputRes = new ArrayList<>();
        for(int i = 0; i < shelfSrc.getQuantity(); i++) {
            inputRes.add(new ResourcePosition(1, shelfSrc.getResource(), Place.WHAREHOUSE, srcShelf));
            outputRes.add(new ResourcePosition(1,shelfSrc.getResource(), Place.WHAREHOUSE, destShelf));
        }
        decrementResource(inputRes);
        incrementResource(outputRes);
    }

    public void checkMove (NumOfShelf srcShelf, NumOfShelf destShelf) throws WrongActionException{
        ResourceQuantity shelfSrc = warehouse.get(srcShelf.ordinal());
        ResourceQuantity shelfDest = warehouse.get(destShelf.ordinal());
        int dimShelfDest;
        if (destShelf.ordinal() < initialDim) dimShelfDest = destShelf.ordinal() + 1;                                   //dimension of a "normal" shelf
        else dimShelfDest = 2;
        if(shelfSrc.getResource() == Resource.EMPTY)
            throw new WrongActionException("Move not possible because the shelf indicated as source is empty");
        else if(shelfDest.getResource() != Resource.EMPTY && shelfDest.getResource() != shelfSrc.getResource())         //rule of same resource in the same shelf.
            throw new WrongActionException("Move not possible because it would store incompatible resource in the same shelf");
        else if (dimShelfDest - shelfDest.getQuantity() < shelfSrc.getQuantity())                                       //dimShelfDest - shelfDest.getQuantity() is used in case the destShelf is a "depot".
            throw new WrongActionException("Move not possible because there is not enough space in the shelf indicated as destination");
    }

    public ArrayList<NumOfShelf> findShelves(Resource resource){
        ArrayList <NumOfShelf> shelves = new ArrayList<>();
        int i;
        for(i = 0; i < warehouse.size(); i++){
            if(warehouse.get(i).getResource() == resource)
                shelves.add(NumOfShelf.values()[i]);
        }
        return shelves;
    }

    private boolean checkOtherShelves(Resource resource, NumOfShelf numOfShelf){                                        //only used in addResource
        boolean check = true;
        for(int i = 0; i < initialDim; i++){
            if(i != numOfShelf.ordinal()){
                if(warehouse.get(i).getResource() == resource) {
                    check = false;
                    break;
                }
            }
        }
        return check;
    }

    public void addDepot(Resource resource){
        warehouse.add(new ResourceQuantity(0, resource));
    }

}
