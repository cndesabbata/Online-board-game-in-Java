package it.polimi.ingsw.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class ResourceQuantity represents an amount of resources of a certain type.
 *
 */
public class ResourceQuantity implements Serializable {
    private int quantity;
    private Resource resource;

    /**
     * Default constructor.
     *
     * @param quantity the amount of resources
     * @param resource the type of the resources
     */
    public ResourceQuantity(int quantity, Resource resource) {
        this.quantity = quantity;
        this.resource = resource;
    }

    /**
     * Returns the amount of resources.
     *
     * @return the amount of resources
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns the type of the resources.
     *
     * @return the type of the resources
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Sets the amount of resources of this object.
     *
     * @param quantity the new amount of resources
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Sets the type of the resources of this object.
     *
     * @param resource the type of the resources
     */
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    /**
     * Flattens a list of ResourceQuantity objects to a list of single resources.
     *
     * @param l the list that will be flattened
     * @return the flattened list
     */
    public static List<Resource> flatten(List<ResourceQuantity> l){
        List<Resource> resourceList = new ArrayList<>();
        for (ResourceQuantity res : l){
            for (int i = 0; i < res.getQuantity(); i++){
                resourceList.add(res.getResource());
            }
        }
        return resourceList;
    }

    /**
     * Flattens a ResourceQuantity object to a list of single resources.
     *
     * @param r the ResourceQuantity object that will be flattened
     * @return the flattened list
     */
    public static List<Resource> flatten(ResourceQuantity r){
        List<Resource> resourceList = new ArrayList<>();
        for (int i = 0; i < r.getQuantity(); i++){
            resourceList.add(r.getResource());
        }
        return resourceList;
    }

    /**
     * Turns a list of resources into a list of strings.
     *
     * @param l the list of resources
     * @return the list of string representing the resources as text
     */
    public static List<String> toStringList(List<Resource> l){
        List<String> list = new ArrayList<>();
        for (Resource r : l){
            list.add(r.toString());
        }
        return list;
    }
}
