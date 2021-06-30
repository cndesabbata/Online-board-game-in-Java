package it.polimi.ingsw.messages.clientMessages.internal;

/**
 * Class PrintDevSpace represents a {@link ViewMessage} that instructs the CLI and the GUI
 * to show an updated development space.
 *
 */
public class PrintDevSpace extends ViewMessage{

    /**
     * Creates a new PrintDevSpace instance.
     *
     * @param owner the owner of the development space
     */
    public PrintDevSpace(String owner) {
        super(owner);
    }
}
