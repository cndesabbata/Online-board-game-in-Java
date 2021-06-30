package it.polimi.ingsw.messages.clientMessages.internal;

/**
 * Class PrintChest represents a {@link ViewMessage} that instructs the CLI and the GUI
 * to show an updated chest.
 *
 */
public class PrintChest extends ViewMessage{

    /**
     * Creates e new PrintChest instance.
     *
     * @param owner the owner of the chest
     */
    public PrintChest(String owner) {
        super(owner);
    }
}
