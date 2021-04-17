package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Place;

public class ResourcePosition extends ResourceQuantity{
    private Place place;
    private NumOfShelf shelf;

    public ResourcePosition(int quantity, Resource resource, Place place, NumOfShelf shelf) {
        super(quantity, resource);
        this.place = place;
        this.shelf = shelf;
    }

    public Place getPlace() {
        return place;
    }

    public NumOfShelf getShelf() {
        return shelf;
    }
}
