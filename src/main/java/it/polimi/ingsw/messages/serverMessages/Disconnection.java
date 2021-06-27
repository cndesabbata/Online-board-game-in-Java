package it.polimi.ingsw.messages.serverMessages;

public class Disconnection extends CustomMessage {
    private String nickname;

    public Disconnection(String message, String nickname) {
        super(message);
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
