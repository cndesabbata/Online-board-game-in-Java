package it.polimi.ingsw.server.model;

public enum LeaderType {
    DISCOUNT("Discount"), DEPOT("Depot"), MARBLE("Marble"), PRODUCT("Product");

    private final String name;

    LeaderType(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
