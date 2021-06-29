package it.polimi.ingsw.messages.serverMessages;

/**
 * Class ErrorMessage represents an error {@link CustomMessage} that needs to be
 * showed to the player.
 *
 */
public class ErrorMessage extends CustomMessage {
    private final ErrorType errorType;

    /**
     * Creates a new ErrorMessage instance.
     *
     * @param message   the message to show to the player
     * @param errorType the type of the error
     */
    public ErrorMessage(String message, ErrorType errorType){
        super(message);
        this.errorType = errorType;
    }

    /**
     * Returns the type of the error.
     *
     * @return the type of the error
     */
    public ErrorType getErrorType() {
        return errorType;
    }
}
