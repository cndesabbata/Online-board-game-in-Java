package it.polimi.ingsw.server.model.gameboard;

/**
 * Enumeration CardStatus contains all the possible states for the papal cards.
 *
 */
public enum CardStatus {
    FACE_UP("Face up"), FACE_DOWN("Face down"), DISCARDED("Discarded");

    private final String name;

    /**
     * Default constructor.
     *
     * @param s the string representation of the state
     */
    CardStatus(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
