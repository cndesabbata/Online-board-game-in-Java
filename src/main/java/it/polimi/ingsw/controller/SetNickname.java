package it.polimi.ingsw.controller;

public class SetNickname implements ClientMessage {
    private final String nickname;

    public SetNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
