package it.polimi.ingsw.messages.serverMessages;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.newElement.ChangeMessage;
import it.polimi.ingsw.server.controller.UserAction;

import java.util.ArrayList;
import java.util.List;

public class ActionDone implements Message {
    private final String nickname;
    private final UserAction userAction;
    private List<ChangeMessage> newElements;

    public ActionDone(String nickname, UserAction userAction) {
        this.nickname = nickname;
        this.userAction = userAction;
        newElements = new ArrayList<>();
    }

    public List<ChangeMessage> getNewElements() {
        return newElements;
    }

    public String getNickname() {
        return nickname;
    }

    public UserAction getUserAction() {
        return userAction;
    }

    public void setNewElements(List<ChangeMessage> newElements) {
        this.newElements = newElements;
    }
}
