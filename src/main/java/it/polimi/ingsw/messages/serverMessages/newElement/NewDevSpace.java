package it.polimi.ingsw.messages.serverMessages.newElement;

import it.polimi.ingsw.server.model.DevCard;

import java.util.List;

public class NewDevSpace implements ChangeMessage{
    private final List<List<DevCard>> devSpace;
    private final String owner;

    public NewDevSpace(List<List<DevCard>> devSpace, String owner) {
        this.devSpace = devSpace;
        this.owner = owner;
    }

    public List<List<DevCard>> getDevSpace() {
        return devSpace;
    }

    public String getOwner() {
        return owner;
    }
}
