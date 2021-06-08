package it.polimi.ingsw.messages.serverMessages;

public class ErrorMessage extends CustomMessage {
    private final ErrorType errorType;

    public ErrorMessage(String message, ErrorType errorType){
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
