package it.polimi.ingsw.server.model;

/**
 * Enumeration Resource contains all the possible types of resources.
 *
 */
public enum Resource {
    COIN("Coin"), STONE("Stone"), SERVANT("Servant"),
    SHIELD("Shield"), FAITHPOINT("Faithpoint"), EMPTY("");

    private final String name;

    /**
     * Default constructor.
     *
     * @param s the string representation of the resource
     */
    Resource(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
