package it.polimi.ingsw.messages.serverMessages;

import java.io.Serializable;

public class Reconnect implements Serializable {
    private String nickname;

    public Reconnect(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
