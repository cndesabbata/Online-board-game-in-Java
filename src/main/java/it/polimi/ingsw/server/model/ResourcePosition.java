package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.controller.Place;
import it.polimi.ingsw.server.model.gameboard.NumOfShelf;

import java.io.Serializable;

/**
 * Class Resource Position represents an amount of resources of a certain
 * type that are stored in a certain location.
 *
 */
public class ResourcePosition extends ResourceQuantity implements Serializable {
    private Place place;
    private NumOfShelf shelf;

    /**
     * Default constructor. Creates objects of the desired resource with a
     * quantity set to 1.
     *
     * @param resource the type of the resource
     * @param place    the place where the resource is stored
     * @param shelf    the shelf of the warehouse if the resource is stored in the warehouse, {@code null} otherwise
     */
    public ResourcePosition(Resource resource, Place place, NumOfShelf shelf) {
        super(1, resource);
        this.place = place;
        this.shelf = shelf;
    }

    /**
     * Reduced constructor used to create objects with a desired amount
     * of resources. Used in unit tests only.
     *
     * @param quantity the amount of resources
     * @param resource the type of the resources
     * @param place    the place where the resources are stored
     */
    public ResourcePosition(int quantity, Resource resource, Place place) {
        super(quantity, resource);
        this.place = place;
    }

    /**
     * Reduced constructor used in the peepRequirements method of the DevDeck class.
     *
     * @param quantity the amount of resources
     * @param resource the type of the resources
     */
    public ResourcePosition(int quantity, Resource resource) {
        super(quantity, resource);
    }

    /**
     * Returns the place where the resource is stored.
     *
     * @return the place where the resource is stored
     */
    public Place getPlace() {
        return place;
    }

    /**
     * The warehouse shelf where the resource is stored.
     *
     * @return the warehouse shelf where the resource is stored
     */
    public NumOfShelf getShelf() {
        return shelf;
    }
}
