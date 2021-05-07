package it.polimi.ingsw.client.clientNetwork;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Message;

public class MessageHandler {
    private final ClientView view;

    public MessageHandler(ClientView view) {
        this.view = view;
    }

    public void process(Message message){

    }
}
