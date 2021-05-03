package it.polimi.ingsw.messages.clientMessages;
import it.polimi.ingsw.messages.Message;

public class Reconnect implements Message {
    private String nickname;

    public Reconnect(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
