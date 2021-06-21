package it.polimi.ingsw.server.model;

/**
 * Enumeration Colour contains all the possible colours for the development cards.
 *
 */
public enum Colour {
    GREEN("Green"), BLUE("Blue"), YELLOW("Yellow"), PURPLE("Purple");

    private final String name;

    /**
     * Default constructor.
     *
     * @param s the string representation of the colour
     */
    Colour(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
