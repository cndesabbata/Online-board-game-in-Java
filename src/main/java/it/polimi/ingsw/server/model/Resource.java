package it.polimi.ingsw.server.model;

public enum Resource {
    COIN("Coin"), STONE("Stone"), SERVANT("Servant"),
    SHIELD("Shield"), FAITHPOINT("Faithpoint"), EMPTY("");

    private final String name;

    Resource(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
