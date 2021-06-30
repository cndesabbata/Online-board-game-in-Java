package it.polimi.ingsw.messages.clientMessages.internal;

/**
 * Class SetupDiscard represents a {@link ViewMessage} that instructs the CLI and the GUI
 * to ask the player which initial leader cards he wants to discard.
 *
 */
public class SetupDiscard extends ViewMessage{

    /**
     * Creates a new SetupDiscard instance.
     *
     * @param message the message to show to the player
     */
    public SetupDiscard(String message) {
        super(message);
    }
}
