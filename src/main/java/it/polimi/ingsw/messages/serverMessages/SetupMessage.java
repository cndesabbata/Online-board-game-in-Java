package it.polimi.ingsw.messages.serverMessages;

public class SetupMessage extends CustomMessage {
    private final AnswerType answerType;

    public SetupMessage(String message, AnswerType answerType) {
        super(message);
        this.answerType = answerType;
    }

    public AnswerType getAnswerType() {
        return answerType;
    }
}
