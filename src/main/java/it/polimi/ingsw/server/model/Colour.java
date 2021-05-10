package it.polimi.ingsw.server.model;

public enum Colour {
    GREEN("Green"), BLUE("Blue"), YELLOW("Yellow"), PURPLE("Purple");

    private final String name;

    Colour(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
