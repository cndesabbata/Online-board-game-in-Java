package it.polimi.ingsw.model;

public class GameBoard {
    private Itinerary itinerary;
    private Warehouse warehouse;
    private Chest chest;
    private DevSpace devSpace;

    public GameBoard(){
        itinerary = new Itinerary();
        warehouse = new Warehouse();
        chest = new Chest();
        devSpace = new DevSpace();
    }

    public Itinerary getItinerary() {
        return itinerary;
    }

    public Warehouse getWharehouse() {
        return warehouse;
    }

    public Chest getChest() {
        return chest;
    }

    public DevSpace getDevSpace() {
        return devSpace;
    }

}
