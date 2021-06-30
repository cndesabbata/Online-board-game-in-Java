package it.polimi.ingsw.messages.clientMessages.internal;

/**
 * Class SetupResources represents a {@link ViewMessage} that instructs the CLI and the GUI
 * to ask the player where he wants to put his initial resources.
 *
 */
public class SetupResources extends ViewMessage{

    /**
     * Creates a new SetupResources instance.
     *
     * @param message the message to show to the player
     */
    public SetupResources(String message) {
        super(message);
    }
}
