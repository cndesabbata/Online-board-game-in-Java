package it.polimi.ingsw.messages.clientMessages.internal;

/**
 * Class DisplayMessage represents a {@link ViewMessage} that instruct the CLI and the GUI
 * to display a string message to the player.
 *
 */
public class DisplayMessage extends ViewMessage {

    /**
     * Creates a new DisplayMessage instance.
     *
     * @param message the message to display
     */
    public DisplayMessage(String message) {
        super(message);
    }
}
