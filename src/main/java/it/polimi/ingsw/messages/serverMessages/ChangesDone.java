package it.polimi.ingsw.messages.serverMessages;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.serverMessages.newElement.ChangeMessage;
import it.polimi.ingsw.server.controller.UserAction;

import java.util.ArrayList;
import java.util.List;

public class ChangesDone implements Message {
    private final String nickname;
    private final UserAction type;
    private List<ChangeMessage> newElements;

    public ChangesDone(String nickname, UserAction userAction) {
        this.nickname = nickname;
        this.type = userAction;
        newElements = new ArrayList<>();
    }

    public List<ChangeMessage> getNewElements() {
        return newElements;
    }

    public String getNickname() {
        return nickname;
    }

    public UserAction getType() {
        return type;
    }

    public void setNewElements(List<ChangeMessage> newElements) {
        this.newElements = newElements;
    }
}
