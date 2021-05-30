package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.messages.Message;

public class JoinLobby implements Message {
    private final String lobbyHost;


    public JoinLobby(String lobbyHost) {
        this.lobbyHost = lobbyHost;
    }

    public String getLobbyHost() {
        return lobbyHost;
    }

}
