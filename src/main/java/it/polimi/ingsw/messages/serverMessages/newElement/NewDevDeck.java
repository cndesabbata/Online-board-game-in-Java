package it.polimi.ingsw.messages.serverMessages.newElement;

import it.polimi.ingsw.server.model.Colour;
import it.polimi.ingsw.server.model.DevCard;

public class NewDevDeck implements ChangeMessage {
    private final Colour colour;
    private final int level;
    private final DevCard deck;

    public NewDevDeck(Colour colour, int level, DevCard deck) {
        this.colour = colour;
        this.level = level;
        this.deck = deck;
    }

    public Colour getColour() {
        return colour;
    }

    public int getLevel() {
        return level;
    }

    public DevCard getDeck() {
        return deck;
    }
}