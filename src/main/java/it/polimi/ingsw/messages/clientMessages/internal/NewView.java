package it.polimi.ingsw.messages.clientMessages.internal;

/**
 * Class NewView represents a {@link ViewMessage} that instructs the CLI and the GUI
 * to display the view elements when a game starts or when the player is reconnecting.
 *
 */
public class NewView extends ViewMessage{

    /**
     * Creates a new NewView instance.
     *
     * @param message the message to show
     */
    public NewView(String message) {
        super(message);
    }
}
