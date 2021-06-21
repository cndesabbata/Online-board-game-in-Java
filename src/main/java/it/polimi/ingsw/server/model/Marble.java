package it.polimi.ingsw.server.model;

/**
 * Enumeration Marble contains all the possible types of marbles.
 *
 */
public enum Marble {
    WHITE("White"), GREY("Grey"), YELLOW("Yellow"),
    BLUE("Blue"), PURPLE("Purple"), RED("Red");

    private final String name;

    /**
     * Default constructor.
     *
     * @param s the string representation of the marble
     */
    Marble(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
