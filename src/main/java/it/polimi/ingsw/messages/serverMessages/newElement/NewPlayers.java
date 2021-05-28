package it.polimi.ingsw.messages.serverMessages.newElement;

import java.util.List;

public class NewPlayers implements ChangeMessage{
    private List<String> players;

    public NewPlayers(List<String> players) {
        this.players = players;
    }

    public List<String> getPlayers() {
        return players;
    }
}
