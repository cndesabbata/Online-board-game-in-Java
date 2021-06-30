package it.polimi.ingsw.messages.clientMessages.internal;

/**
 * Class ChooseAction represents a {@link ViewMessage} that instructs the CLI and the GUI
 * to ask the player to choose an action.
 *
 */
public class ChooseAction extends ViewMessage{

    /**
     * Creates a new ChooseAction instance.
     *
     * @param message the message to show to the player
     */
    public ChooseAction(String message) {
        super(message);
    }
}
