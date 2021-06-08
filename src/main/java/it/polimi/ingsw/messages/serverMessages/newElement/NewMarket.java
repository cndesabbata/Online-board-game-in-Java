package it.polimi.ingsw.messages.serverMessages.newElement;

import it.polimi.ingsw.server.model.Marble;

public class NewMarket implements ChangeMessage{
    private final Marble[][] disposition;
    private final Marble external;

    public NewMarket(Marble[][] disposition, Marble external) {
        this.disposition = disposition;
        this.external = external;
    }

    public Marble[][] getDisposition() {
        return disposition;
    }

    public Marble getExternal() {
        return external;
    }
}
