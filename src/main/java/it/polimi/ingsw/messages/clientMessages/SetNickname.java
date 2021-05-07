package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.messages.Message;

public class SetNickname implements Message {
    private final String nickname;

    public SetNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
