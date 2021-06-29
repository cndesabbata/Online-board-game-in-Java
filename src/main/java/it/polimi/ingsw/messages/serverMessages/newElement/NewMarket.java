package it.polimi.ingsw.messages.serverMessages.newElement;

import it.polimi.ingsw.server.model.Marble;

/**
 * Class NewMarket is a {@link ChangeMessage} that contains the updated copy of the market.
 *
 */
public class NewMarket implements ChangeMessage{
    private final Marble[][] disposition;
    private final Marble external;

    /**
     * Creates a NewMarket instance.
     *
     * @param disposition the new disposition of the marbles
     * @param external    the new external marble
     */
    public NewMarket(Marble[][] disposition, Marble external) {
        this.disposition = disposition;
        this.external = external;
    }

    /**
     * Returns the new marble disposition.
     *
     * @return the new marble disposition
     */
    public Marble[][] getDisposition() {
        return disposition;
    }

    /**
     * Returns the new external marble.
     *
     * @return the new external marble
     */
    public Marble getExternal() {
        return external;
    }
}
