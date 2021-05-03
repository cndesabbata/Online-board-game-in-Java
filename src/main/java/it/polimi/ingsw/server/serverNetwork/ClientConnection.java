package it.polimi.ingsw.server.serverNetwork;

import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.messages.clientMessages.SetNickname;
import it.polimi.ingsw.messages.clientMessages.SetPlayersNumber;
import it.polimi.ingsw.messages.serverMessages.ErrorMessage;
import it.polimi.ingsw.messages.serverMessages.PlayersNumberMessage;
import it.polimi.ingsw.messages.clientMessages.Reconnect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection implements Runnable {
    private final Server server;
    private Socket socket;
    private boolean active;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String playerNickname;
    private GameController gameController;

    public ClientConnection(Socket socket, Server server ) {
        this.server = server;
        this.socket = socket;
        active = true;
        try {
            input = new ObjectInputStream(this.socket.getInputStream());
            output = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Error during initialization of the client!");
        }
    }

    public void close(){
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void readInput() throws IOException, ClassNotFoundException {
        Message inputClientMessage = (Message) input.readObject();
        if (inputClientMessage != null) messageHandler(inputClientMessage);
    }

    @Override
    public void run(){
        try {
            while (active) {
                readInput();
            }
            server.removeClient(this);
        } catch (IOException e) {
            if (gameController.isStarted() == 1) server.unregisterClient(this);
            else server.removeClient(this);
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e){
            System.err.println(e.getMessage());
        }
    }

    public void messageHandler(Message clientMessage){
        if (clientMessage instanceof SetNickname){
            if (gameController == null && playerNickname == null){
                try {
                    server.(((SetNickname) clientMessage).getNickname(), this);
                    playerNickname = ((SetNickname) clientMessage).getNickname();
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
            else sendSocketMessage(new ErrorMessage("Invalid action: you already have a nickname"));
        }
        else if (clientMessage instanceof Reconnect){
            if (gameController == null && playerNickname == null){
                try {
                    server.reconnectClient(((Reconnect) clientMessage).getNickname(), this);
                    playerNickname = ((SetNickname) clientMessage).getNickname();
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
            else sendSocketMessage(new ErrorMessage("Invalid action: you are already connected"));
        }
        else if (clientMessage instanceof SetPlayersNumber){
            if (((SetPlayersNumber) clientMessage).getNumOfPlayers() < 1
                    || ((SetPlayersNumber) clientMessage).getNumOfPlayers() > 4){
                sendSocketMessage(new ErrorMessage("Not a valid input, please provide a number between 1 and 4"));
                sendSocketMessage(new PlayersNumberMessage("Choose the number of players: [1...4]"));
            }
            else server.setTotalPlayers(((SetPlayersNumber) clientMessage).getNumOfPlayers(), this);
        }
    }

    public void sendSocketMessage(Message message){
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            close();
        }
    }

    public String getPlayerNickname() {
        return playerNickname;
    }
}
