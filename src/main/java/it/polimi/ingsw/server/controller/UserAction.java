package it.polimi.ingsw.server.controller;

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
    RESOURCE_SELECTION(""),
    DISCARD_DEV_CARD("has discarded development cards from the market."),
    UPDATE_AND_SHUFFLE("has advanced on the itinerary."),
    UPDATE_ITINERARY("has advanced on the itinerary.");

    private final String name;

    UserAction(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
