package it.polimi.ingsw.messages.serverMessages;

/**
 * Class CloseMessage represents a {@link CustomMessage} used by the server to communicate the end
 * of the communication. It's used when the game ends.
 *
 */
public class CloseMessage extends CustomMessage {

    /**
     * Creates a new CloseMessage instance.
     *
     * @param message the message to show to the player
     */
    public CloseMessage(String message) {
        super(message);
    }
}
