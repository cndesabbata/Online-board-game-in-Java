package it.polimi.ingsw.model;

public class ResourceReq {
    private final int quantity;
    private final Resource resource;

    public ResourceReq(int quantity, Resource resource) {
        this.quantity = quantity;
        this.resource = resource;
    }

    public int getQuantity() {
        return quantity;
    }

    public Resource getResource() {
        return resource;
    }
}
