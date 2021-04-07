package it.polimi.ingsw.model;

public class GameBoard {
    private Itinerary itinerary;
    private Wharehouse wharehouse;
    private Chest chest;
    private DevSpace devSpace;

    public GameBoard(){
        itinerary = new Itinerary();
        wharehouse = new Wharehouse();
        chest = new Chest();
        devSpace = new DevSpace();
    }

    public Itinerary getItinerary() {
        return itinerary;
    }

    public Wharehouse getWharehouse() {
        return wharehouse;
    }

    public Chest getChest() {
        return chest;
    }

    public DevSpace getDevSpace() {
        return devSpace;
    }

}
