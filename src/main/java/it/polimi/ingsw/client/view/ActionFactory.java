package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientNetwork.ClientConnectionSocket;

public class ActionFactory {
    private final CLI cli;
    private final ClientConnectionSocket connectionSocket;

    public ActionFactory(CLI cli, ClientConnectionSocket connectionSocket) {
        this.cli = cli;
        this.connectionSocket = connectionSocket;
    }
}
