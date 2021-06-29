package it.polimi.ingsw.messages.serverMessages.newElement;

import it.polimi.ingsw.server.model.Colour;
import it.polimi.ingsw.server.model.DevCard;

/**
 * Class NewDevDeck is a {@link ChangeMessage} that contains the updated copy of the first card
 * of a development cards deck.
 *
 */
public class NewDevDeck implements ChangeMessage {
    private final Colour colour;
    private final int level;
    private final DevCard deck;

    /**
     * Creates a NewDevDeck instance.
     *
     * @param colour the colour of the deck
     * @param level  the level of the deck
     * @param deck   the new first card of the deck
     */
    public NewDevDeck(Colour colour, int level, DevCard deck) {
        this.colour = colour;
        this.level = level;
        this.deck = deck;
    }

    /**
     * Returns the colour of the deck.
     *
     * @return the colour of the deck
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Returns the level of the deck.
     *
     * @return the level of the deck
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the new first card of the deck.
     *
     * @return the new first card of the deck
     */
    public DevCard getDeck() {
        return deck;
    }
}