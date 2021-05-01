package it.polimi.ingsw.messages.newElement;

import it.polimi.ingsw.server.model.Marble;

public class NewMarket implements ChangeMessage{
    private  Marble[][] disposition;
    private Marble external;

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
