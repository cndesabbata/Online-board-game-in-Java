package it.polimi.ingsw.server.model;

public enum Resource {
    COIN("Coins"), STONE("Stones"), SERVANT("Servants"),
    SHIELD("Shields"), FAITHPOINT("Faith points"), EMPTY("");

    private final String name;

    Resource(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
