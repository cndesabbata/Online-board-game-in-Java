package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.messages.Message;

/**
 * Class JoinLobby is a {@link Message} sent from the player to server. It contains
 * the nickname of the host of the lobby the player wants to join.
 *
 */
public class JoinLobby implements Message {
    private final String lobbyHost;

    /**
     * Creates a new JoinLobby instance.
     *
     * @param lobbyHost the nickname of the lobby host
     */
    public JoinLobby(String lobbyHost) {
        this.lobbyHost = lobbyHost;
    }

    /**
     * Returns the nickname of the lobby host.
     *
     * @return the nickname of the lobby host
     */
    public String getLobbyHost() {
        return lobbyHost;
    }

}
