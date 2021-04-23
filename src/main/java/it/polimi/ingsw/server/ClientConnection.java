package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.ServerMessage;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.ClientMessage;
import it.polimi.ingsw.controller.SetNickname;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection implements Runnable {
    private Server server;
    private Socket socket;
    private boolean active;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String playerNickname;
    private Integer clientID;
    private GameController gameController;

    public ClientConnection(Socket socket, Server server ) {
        this.server = server;
        this.socket = socket;
        clientID = -1;
        active = true;
        try {
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Error during initialization of the client!");
        }
    }

    public void getFromStream(){

    }

    public void close(){

    }

    public void readInput() throws IOException, ClassNotFoundException {
        ClientMessage inputClientMessage = (ClientMessage) input.readObject();
        if (inputClientMessage != null) messageHandler(inputClientMessage);
    }

    @Override
    public void run(){
        try {
            while (active) {
                readInput();
            }
        } catch (IOException e) {
            if (gameController.isStarted()) server.unregisterClient(clientID);
            else server.removeClient(clientID);
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e){
            System.err.println(e.getMessage());
        }
    }

    public void messageHandler(ClientMessage clientMessage){
        if (clientMessage instanceof SetNickname){
            try {
                clientID = server.registerClient(((SetNickname) clientMessage).getNickname(), this);
                if (clientID == null) {
                    active = false;
                    return;
                }
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public void checkConnection(){

    }

    public void sendSocketMessage(ServerMessage serverMessage){

    }

}
