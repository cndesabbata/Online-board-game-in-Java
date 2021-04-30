package it.polimi.ingsw.server.model.gameboard;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.observer.Observable;

import java.util.List;

public class GameBoard extends Observable {
    private final Itinerary itinerary;
    private final Warehouse warehouse;
    private final Chest chest;
    private final DevSpace devSpace;

    public GameBoard(String nickname){
        itinerary = new Itinerary(nickname);
        warehouse = new Warehouse(3, nickname);
        chest = new Chest(nickname);
        devSpace = new DevSpace(nickname);
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
