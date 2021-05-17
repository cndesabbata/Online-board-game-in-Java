package it.polimi.ingsw.client.clientNetwork;

import it.polimi.ingsw.client.view.CLI;
import it.polimi.ingsw.client.view.ClientView;
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
    private CLI cli;

    public ClientConnectionSocket(CLI cli, MessageHandler messageHandler){
        this.cli = cli;
        this.messageHandler = messageHandler;
    }

    public boolean setupConnection(ClientView view) throws IOException {
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
        if (message instanceof SetupMessage){
            System.out.println(((SetupMessage) message).getMessage());
            return true;
        }
        else if (message instanceof ErrorMessage){
            System.out.println(((ErrorMessage) message).getMessage());
        }
        return false;
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
        while (cli.isActive()){
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
