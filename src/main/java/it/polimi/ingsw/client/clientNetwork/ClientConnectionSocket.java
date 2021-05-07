package it.polimi.ingsw.client.clientNetwork;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.client.view.MessageHandler;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.clientMessages.SetNickname;
import it.polimi.ingsw.messages.serverMessages.AnswerType;
import it.polimi.ingsw.messages.serverMessages.ErrorMessage;
import it.polimi.ingsw.messages.serverMessages.SetupMessage;
import it.polimi.ingsw.server.exceptions.InvalidNicknameException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnectionSocket {
    private final String serverIP;
    private final int serverPort;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private SocketListener listener;


    public ClientConnectionSocket(){
        serverIP = Constants.getAddress();
        serverPort = Constants.getPort();
    }

    public boolean setup(String nickname, ClientView view, MessageHandler messageHandler) throws InvalidNicknameException, IOException {
        Socket socket;
        try {
            socket = new Socket(serverIP, serverPort);
        } catch (IOException e) {
            return false;
        }
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
        try {
            if (nicknameChecker(input.readObject(), nickname)) {
                return true;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        listener = new SocketListener(socket, view, messageHandler, input);
        Thread thread = new Thread(listener);
        thread.start();
        return true;
    }

    private boolean nicknameChecker(Object inputObject, String nickname) throws InvalidNicknameException {
        send(new SetNickname(nickname));
        Message message = (Message) inputObject;
        if (message instanceof SetupMessage)
            return ((SetupMessage) message).getAnswerType() == AnswerType.CONFIRMATION;
        else if (message instanceof ErrorMessage){
            System.out.println(((ErrorMessage) message).getMessage());
            throw new InvalidNicknameException();
        }
        return false;
    }

    private void send(Message message) {
        try {
            output.reset();
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            System.err.println("Error during send process.");
            System.err.println(e.getMessage());
        }
    }
}
