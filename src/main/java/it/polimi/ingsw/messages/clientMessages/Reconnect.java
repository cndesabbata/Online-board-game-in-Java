package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.messages.Message;

/**
 * Class Reconnect is a {@link Message} sent from the player to server when
 * he wants to reconnect to a game.
 *
 */
public class Reconnect implements Message {
    private final String nickname;

    /**
     * Creates a new Reconnect instance.
     *
     * @param nickname the nickname of the player
     */
    public Reconnect(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Returns the player's nickname.
     *
     * @return the player's nickname
     */
    public String getNickname() {
        return nickname;
    }
}
