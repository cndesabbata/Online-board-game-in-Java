package it.polimi.ingsw.client.clientNetwork;

import it.polimi.ingsw.client.view.Cli;
import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.client.view.Gui;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.clientMessages.Reconnect;
import it.polimi.ingsw.messages.clientMessages.SetNickname;
import it.polimi.ingsw.messages.serverMessages.CloseMessage;
import it.polimi.ingsw.messages.serverMessages.ErrorMessage;
import it.polimi.ingsw.messages.serverMessages.ErrorType;
import it.polimi.ingsw.messages.serverMessages.SetupMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnectionSocket implements Runnable{
    private String serverIP;
    private int serverPort;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private MessageHandler messageHandler;
    private Socket socket;
    private Cli cli;
    private Gui gui;

    public ClientConnectionSocket(Cli cli, MessageHandler messageHandler){
        this.cli = cli;
        this.gui = null;
        this.messageHandler = messageHandler;
    }

    public ClientConnectionSocket(Gui gui, MessageHandler messageHandler){
        this.gui = gui;
        this.cli = null;
        this.messageHandler = messageHandler;
    }

    public boolean setupConnection() throws IOException {
        serverIP = Constants.getAddress();
        serverPort = Constants.getPort();
        try {
            socket = new Socket(serverIP, serverPort);
        } catch (IOException e) {
            return false;
        }
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
        return true;
    }

    public boolean setupNickname(String nickname, boolean newPlayer) {
        if (newPlayer) send(new SetNickname(nickname));
        else send(new Reconnect(nickname));
        Object inputObject;
        try {
             inputObject = input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            messageHandler.process(new ErrorMessage("Error in reading server answer", ErrorType.SOCKET_ERROR));
            return false;
        }
        Message message = (Message) inputObject;
        messageHandler.process(message);
        return (message instanceof SetupMessage);
    }

    public void send(Message message) {
        try {
            output.reset();
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            messageHandler.process(new ErrorMessage("Error in send process", ErrorType.SOCKET_ERROR));
        }
    }

    @Override
    public void run() {
        Message message = null;
        while (true){
            try {
                message = (Message) input.readObject();
                messageHandler.process(message);
            } catch (IOException | ClassNotFoundException e) {
                messageHandler.process(new ErrorMessage("Error in reading server message", ErrorType.SOCKET_ERROR));
                break;
            }
            if(message instanceof CloseMessage) break;
        }
        try {
            input.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
