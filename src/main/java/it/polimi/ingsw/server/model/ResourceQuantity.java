package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;

public class ResourceQuantity {
    private int quantity;
    private  Resource resource;

    public ResourceQuantity(int quantity, Resource resource) {
        this.quantity = quantity;
        this.resource = resource;
    }

    public int getQuantity() {
        return quantity;
    }

    public Resource getResource() {
        return resource;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    // flatten extends l to a list of single resources
    public static List<Resource> flatten(List<ResourceQuantity> l){
        List<Resource> resourceList = new ArrayList<>();
        for (ResourceQuantity res : l){
            for (int i = 0; i < res.getQuantity(); i++){
                resourceList.add(res.getResource());
            }
        }
        return resourceList;
    }

    // flatten extends r to a list of single resources
    public static List<Resource> flatten(ResourceQuantity r){
        List<Resource> resourceList = new ArrayList<>();
        for (int i = 0; i < r.getQuantity(); i++){
            resourceList.add(r.getResource());
        }
        return resourceList;
    }

    // toStringList turns l into a list of strings
    public static List<String> toStringList(List<Resource> l){
        List<String> list = new ArrayList<>();
        for (Resource r : l){
            list.add(r.toString());
        }
        return list;
    }
}
