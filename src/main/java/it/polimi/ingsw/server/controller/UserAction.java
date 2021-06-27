package it.polimi.ingsw.server.controller;

/**
 * Enumeration UserAction contains all the possible actions a user can perform.
 *
 */
public enum UserAction {
    BUY_RESOURCES ("has bought resources from the market."),
    BUY_DEVCARD ("has bought a development card."),
    START_PRODUCTION("has started production."),
    DISCARD_LEADCARD("has discarded a leader card."),
    PLAY_LEADCARD("has played a leader card."),
    MOVE_RESOURCES("has moved resources in his warehouse."),
    SETUP_DRAW(""),
    SELECT_LEADCARD(""),
    INITIAL_DISPOSITION(""),
    RECONNECT_DISPOSITION(""),
    LAST_ACTION(""),
    RESOURCE_SELECTION("has chosen his initial resources."),
    DISCARD_DEV_CARD("has discarded development cards."),
    UPDATE_AND_SHUFFLE("has advanced on the itinerary."),
    UPDATE_ITINERARY("has advanced on the itinerary.");

    private final String name;

    /**
     * Default constructor.
     *
     * @param s the string representation of the action
     */
    UserAction(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
