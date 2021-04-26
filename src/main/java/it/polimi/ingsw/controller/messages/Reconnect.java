package it.polimi.ingsw.controller.messages;

public class Reconnect implements ClientMessage{
    private String nickname;

    public Reconnect(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
