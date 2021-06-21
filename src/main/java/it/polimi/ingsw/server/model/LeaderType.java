package it.polimi.ingsw.server.model;

/**
 * Enumeration LeaderType contains all the possible types of leader cards.
 *
 */
public enum LeaderType {
    DISCOUNT("Discount"), DEPOT("Depot"), MARBLE("Marble"), PRODUCT("Product");

    private final String name;

    /**
     * Default constructor.
     *
     * @param s the string representation of the leader type
     */
    LeaderType(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
