package it.polimi.ingsw.messages.clientMessages.internal;

/**
 * Class PrintPlayedCards represents a {@link ViewMessage} that instructs the CLI and the GUI
 * to show the played leader cards of a specified player.
 *
 */
public class PrintPlayedCards extends ViewMessage{

    /**
     * Creates a new PrintPlayedCards instance.
     *
     * @param owner the owner of the played leader cards
     */
    public PrintPlayedCards(String owner) {
        super(owner);
    }
}
