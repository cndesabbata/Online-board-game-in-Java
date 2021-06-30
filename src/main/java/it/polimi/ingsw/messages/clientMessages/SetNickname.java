package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.messages.Message;

/**
 * Class SetNickname is a {@link Message} sent from the player to server. It contains
 * the nickname chosen by the player.
 *
 */
public class SetNickname implements Message {
    private final String nickname;

    /**
     * Creates a new SetNickname instance.
     *
     * @param nickname the nickname chosen by the player
     */
    public SetNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Returns the nickname chosen by the player.
     *
     * @return the nickname chosen by the player
     */
    public String getNickname() {
        return nickname;
    }
}
