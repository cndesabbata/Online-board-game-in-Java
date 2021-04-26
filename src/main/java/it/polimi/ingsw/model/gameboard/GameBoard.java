package it.polimi.ingsw.model.gameboard;

import it.polimi.ingsw.model.*;

import java.util.List;

public class GameBoard {
    private final Itinerary itinerary;
    private final Warehouse warehouse;
    private final Chest chest;
    private final DevSpace devSpace;

    public GameBoard(){
        itinerary = new Itinerary();
        warehouse = new Warehouse(3);
        chest = new Chest();
        devSpace = new DevSpace();
    }

    public Itinerary getItinerary() {
        return itinerary;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public Chest getChest() {
        return chest;
    }

    public DevSpace getDevSpace() {
        return devSpace;
    }

    public void expendResources(List<ResourcePosition> resources){
        chest.decrementResource(resources);
        warehouse.decrementResource(resources);
    }

    public <Res extends ResourceQuantity> boolean checkResources(List<Res> resources){
        for(ResourceQuantity res : resources){
            int quantity = chest.getAvailability(res.getResource()) + warehouse.getAvailability(res.getResource());
            if (quantity < res.getQuantity()) return false;
        }
        return true;
    }

    public int getTotalResources (){
        int quantity = 0;
        for (int i = 0; i < Resource.values().length - 2; i++){
            quantity += chest.getAvailability(Resource.values()[i]) + warehouse.getAvailability(Resource.values()[i]);
        }
        return quantity;
    }

}
