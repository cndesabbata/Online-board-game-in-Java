package it.polimi.ingsw.messages.serverMessages;

/**
 * Class SetupMessage represents a {@link CustomMessage} used by the server used to communicate
 * things to the player before the game starts.
 *
 */
public class SetupMessage extends CustomMessage {

    /**
     * Creates a new SetupMessage instance.
     *
     * @param message the message to show to the player
     */
    public SetupMessage(String message) {
        super(message);
    }

}
