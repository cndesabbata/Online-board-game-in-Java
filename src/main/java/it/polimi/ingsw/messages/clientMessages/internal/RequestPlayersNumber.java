
package it.polimi.ingsw.messages.clientMessages.internal;

import java.util.List;

public class RequestPlayersNumber extends ViewMessage {
    List<String> infoLobbies;
    List<String> owners;

    public RequestPlayersNumber(String message, List<String> infoLobbies, List<String> owners) {
        super(message);
        this.infoLobbies = infoLobbies;
        this.owners = owners;
    }

    public List<String> getInfoLobbies() {
        return infoLobbies;
    }

    public List<String> getOwners() {
        return owners;
    }
}