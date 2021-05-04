package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.Place;
import it.polimi.ingsw.server.model.gameboard.NumOfShelf;

public class ResourcePosition extends ResourceQuantity{
    private Place place;
    private NumOfShelf shelf;

    public ResourcePosition(Resource resource, Place place, NumOfShelf shelf) {
        super(1, resource);
        this.place = place;
        this.shelf = shelf;
    }

    public ResourcePosition(int quantity, Resource resource, Place place) {
        super(quantity, resource);
        this.place = place;
    }

    public ResourcePosition(int quantity, Resource resource) {
        super(quantity, resource);
    }

    public Place getPlace() {
        return place;
    }

    public NumOfShelf getShelf() {
        return shelf;
    }
}
