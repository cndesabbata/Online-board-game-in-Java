package it.polimi.ingsw.controller.messages;

import it.polimi.ingsw.controller.messages.ClientMessage;

public class SetNickname implements ClientMessage {
    private final String nickname;

    public SetNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
