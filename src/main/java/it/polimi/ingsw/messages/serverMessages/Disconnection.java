package it.polimi.ingsw.messages.serverMessages;

/**
 * Class Disconnection represents a {@link CustomMessage} used by the server used to communicate
 * that a player has disconnected from the game or has reconnected to the game.
 *
 */
public class Disconnection extends CustomMessage {
    private String nickname;

    /**
     * Creates a new Disconnection instance.
     *
     * @param message  the message to show
     * @param nickname the nickname of the player that is disconnecting or reconnecting
     */
    public Disconnection(String message, String nickname) {
        super(message);
        this.nickname = nickname;
    }

    /**
     * Returns the nickname of the player that is disconnecting or reconnecting.
     *
     * @return the nickname of the player that is disconnecting or reconnecting
     */
    public String getNickname() {
        return nickname;
    }
}
