package it.polimi.ingsw.messages.clientMessages.internal;

/**
 * Class PrintItinerary represents a {@link ViewMessage} that instructs the CLI and the GUI
 * to show an updated itinerary.
 *
 */
public class PrintItinerary extends ViewMessage{

    /**
     * Creates a new PrintItinerary instance.
     *
     * @param owner the owner of the itinerary
     */
    public PrintItinerary(String owner) {
        super(owner);
    }
}
