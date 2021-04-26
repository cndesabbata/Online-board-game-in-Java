package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Place;
import it.polimi.ingsw.model.gameboard.NumOfShelf;

public class ResourcePosition extends ResourceQuantity{
    private Place place;
    private NumOfShelf shelf;

    public ResourcePosition(int quantity, Resource resource, Place place, NumOfShelf shelf) {
        super(quantity, resource);
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
