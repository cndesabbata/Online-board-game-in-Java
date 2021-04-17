package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Place;

import java.util.ArrayList;

public class Chest {
    private ArrayList<ResourceQuantity> chest;

    public Chest(){
        chest = new ArrayList<>();
        for(int i = 0; i < Resource.values().length - 2; i++){                                                          //faithpoint and empty are not storable in the Chest.
            chest.add(new ResourceQuantity(0, Resource.values()[i]));
        }
    }

    public ArrayList<ResourceQuantity> getChest() {
        return new ArrayList<ResourceQuantity>(chest);
    }

    public void decrementResource(ArrayList <ResourcePosition> inputRes) {
        ArrayList <ResourcePosition> removableRes = new ArrayList<>(inputRes);
        removableRes.removeIf(Rp -> Rp.getPlace() != Place.CHEST);
        for(ResourcePosition Rp : inputRes){
            int index = getIndexResource(Rp.getResource());
            ResourceQuantity Rq = chest.get(index);
            Rq.setQuantity(Rq.getQuantity() - Rp.getQuantity());
        }
    }

    public void checkDecrement(ArrayList <ResourcePosition> inputRes) throws WrongActionException{
        ArrayList <ResourcePosition> storableRes = new ArrayList<>(inputRes);
        storableRes.removeIf(Rp -> Rp.getPlace() != Place.CHEST);                                                       //resources that must be taken elsewhere are not involved in this method.
        ArrayList <ResourceQuantity> result = new ArrayList<>(chest);                                                   //shallow copy of chest
        for(ResourcePosition Rp : storableRes){
            if(Rp.getResource() == Resource.EMPTY) throw new WrongActionException("Empty resource is not removable");
            else {
                ResourceQuantity Rq = result.get(getIndexResource(Rp.getResource()));                                   //Rq is the node in result which has the same resource as the node of storableRes that is examined in this iteration of the loop.
                Rq.setQuantity(Rq.getQuantity() - Rp.getQuantity());
            }
        }
        boolean check = true;
        ArrayList <String> errors = new ArrayList<>();
        for(ResourceQuantity Rq : result){
            if(Rq.getQuantity() < 0){
                check = false;
                errors.add(new String("The number of" + Rq.getResource().toString() + "is not sufficient\n"));
            }
        }
        if(!check) {
            String error = errors.stream().reduce("", (S,S1) -> S + S1);                                         //error is the concatenation of the strings in errors
            throw new WrongActionException(error);
        }
    }

    public void incrementResource(ArrayList <ResourcePosition> inputRes) {
        for(ResourcePosition Rp : inputRes){
            int index = getIndexResource(Rp.getResource());
            ResourceQuantity Rq = chest.get(index);
            Rq.setQuantity(Rq.getQuantity() + Rp.getQuantity());
        }
    }

    public void checkIncrement(ArrayList <ResourcePosition> outputRes) throws WrongActionException{                     //it is used only by the checkAction in StartProduction
        for(ResourcePosition Rp : outputRes){
            if(Rp.getResource() == Resource.EMPTY) throw new WrongActionException("Empty resource cannot be stored");
            else if(Rp.getPlace() != Place.CHEST) throw new WrongActionException("All the resources must be stored in the chest");
        }
    }

    public boolean checkQuantity (Resource resource, int quantity){
        int index = this.getIndexResource(resource);
        return  chest.get(index).getQuantity() >= quantity;
    }

    private int getIndexResource(Resource resource){                                                                    //returns the index of the node of chest which store resource
        int index;
        for(index = 0; index < chest.size(); index++){
            if(chest.get(index).getResource() == resource)
                break;
        }
        return index;
    }
}

