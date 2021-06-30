package it.polimi.ingsw.messages.clientMessages.internal;

/**
 * Class PrintWarehouse represents a {@link ViewMessage} that instructs the CLI and the GUI
 * to show an updated warehouse.
 *
 */
public class PrintWarehouse extends ViewMessage{

    /**
     * Creates a new PrintWarehouse instance.
     *
     * @param owner the owner of the warehouse
     */
    public PrintWarehouse(String owner) {
        super(owner);
    }
}
