package it.polimi.ingsw.model;

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

    public void decrementResource(Resource resource, int quantity) {
        int index = this.getIndexResource(resource);
        ResourceQuantity Rq = chest.get(index);
        Rq.setQuantity(Rq.getQuantity() - quantity);
    }

    public void incrementResource(Resource resource, int quantity) {
        int index = this.getIndexResource(resource);
        ResourceQuantity Rq = chest.get(index);
        Rq.setQuantity(Rq.getQuantity() + quantity);
    }

    public boolean checkQuantity (Resource resource, int quantity){
        int index = this.getIndexResource(resource);
        return  chest.get(index).getQuantity() >= quantity;
    }

    private int getIndexResource(Resource resource){
        int index;
        for(index = 0; index < chest.size(); index++){
            if(chest.get(index).getResource() == resource)
                break;
        }
        return index;
    }
}

