package it.polimi.ingsw.server.model;

public enum Marble {
    WHITE("White"), GREY("Grey"), YELLOW("Yellow"),
    BLUE("Blue"), PURPLE("Purple"), RED("Red");

    private final String name;

    Marble(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
