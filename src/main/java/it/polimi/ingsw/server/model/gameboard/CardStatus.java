package it.polimi.ingsw.server.model.gameboard;

public enum CardStatus {
    FACE_UP("Face up"), FACE_DOWN("Face down"), DISCARDED("Discarded");

    private final String name;

    CardStatus(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
