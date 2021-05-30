package it.polimi.ingsw.messages.serverMessages;

import it.polimi.ingsw.server.serverNetwork.Lobby;

import java.util.ArrayList;
import java.util.List;

public class PlayersNumberMessage extends CustomMessage {
    private final List<String> infoLobbies;
    private final List<String> owners;

    public PlayersNumberMessage(String message, List<Lobby> lobbies) {
        super(message);
        infoLobbies = new ArrayList<>();
        owners = new ArrayList<>();
        for(Lobby l : lobbies){
            owners.add(l.getOwner());
            infoLobbies.add(l.getOwner().toUpperCase() + " :  [ "
                    + l.getWaitingList().size() + " / " + l.getTotalPlayers() + " ]");
        }
    }

    public List<String> getInfoLobbies() {
        return infoLobbies;
    }

    public List<String> getOwners() {
        return owners;
    }
}