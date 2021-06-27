package it.polimi.ingsw.server.model.gameboard;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.observer.Observable;

import java.util.List;

/**
 * Class GameBoard represents the game board of the player. It contains
 * the itinerary, the warehouse, the chest and the development space.
 *
 */
public class GameBoard extends Observable {
    private final Itinerary itinerary;
    private final Warehouse warehouse;
    private final Chest chest;
    private final DevSpace devSpace;

    /**
     * Default constructor.
     *
     * @param nickname the nickname of the player owning the gameboard
     */
    public GameBoard(String nickname){
        itinerary = new Itinerary(nickname);
        warehouse = new Warehouse(3, nickname);
        chest = new Chest(nickname);
        devSpace = new DevSpace(nickname);
    }

    /**
     * Returns the itinerary.
     *
     * @return the itinerary
     */
    public Itinerary getItinerary() {
        return itinerary;
    }

    /**
     * Returns the warehouse.
     *
     * @return the warehouse
     */
    public Warehouse getWarehouse() {
        return warehouse;
    }

    /**
     * Returns the chest.
     *
     * @return the chest
     */
    public Chest getChest() {
        return chest;
    }

    /**
     * Returns the development space.
     *
     * @return the development space
     */
    public DevSpace getDevSpace() {
        return devSpace;
    }

    /**
     * Decrements resources from the chest and the warehouse.
     *
     * @param resources the list of {@link ResourcePosition} objects that will be removed
     */
    public void expendResources(List<ResourcePosition> resources){
        chest.decrementResource(resources);
        warehouse.decrementResource(resources);
    }

    /**
     * Checks if a player has the required resources to play a leader card.
     *
     * @param resources the list of required resources
     * @param <Res>
     * @return {@code true} if the player has the required resources, {@code false} otherwise
     */
    public <Res extends ResourceQuantity> boolean checkResources(List<Res> resources){
        for(ResourceQuantity res : resources){
            int quantity = chest.getAvailability(res.getResource()) + warehouse.getAvailability(res.getResource());
            if (quantity < res.getQuantity()) return false;
        }
        return true;
    }

    /**
     * Returns the total amount of resources owned by the player.
     *
     * @return the total amount of resources owned by the player
     */
    public int getTotalResources (){
        int quantity = 0;
        for (int i = 0; i < Resource.values().length - 2; i++){
            quantity += chest.getAvailability(Resource.values()[i]) + warehouse.getAvailability(Resource.values()[i]);
        }
        return quantity;
    }

}
