package it.polimi.ingsw.client.clientNetwork;

import it.polimi.ingsw.client.view.Cli;
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

/**
 * Class ClientConnectionSocket manages the client socket. It handles
 * the connection between the client and the server.
 *
 */
public class ClientConnectionSocket implements Runnable{
    private String serverIP;
    private int serverPort;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private final MessageHandler messageHandler;
    private Socket socket;
    private Cli cli;
    private Gui gui;

    /**
     * Creates a new ClientConnectionSocket instance. Used by the CLI.
     *
     * @param cli            the cli object
     * @param messageHandler the MessageHandler object associated with this object
     */
    public ClientConnectionSocket(Cli cli, MessageHandler messageHandler){
        this.cli = cli;
        this.gui = null;
        this.messageHandler = messageHandler;
    }

    /**
     * Creates a new ClientConnectionSocket instance. Used by the GUI.
     *
     * @param gui            the gui object
     * @param messageHandler the MessageHandler object associated with this object
     */
    public ClientConnectionSocket(Gui gui, MessageHandler messageHandler){
        this.gui = gui;
        this.cli = null;
        this.messageHandler = messageHandler;
    }

    /**
     * Sets up the connection with the server.
     *
     * @return {@code true} if the connection is setup correctly, {@code false} otherwise
     * @throws IOException if an I/O exception occurs
     */
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

    /**
     * Tries to set the player's nickname with a {@link SetNickname} message,
     * or with a {@link Reconnect} message if the player is reconnecting.
     *
     * @param nickname  the nickname chosen by the player
     * @param newPlayer {@code true} if the player is a new player, {@code false} if he is reconnecting
     * @return {@code true} if the player is registered with the provided nickname, {@code false} otherwise
     */
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

    /**
     * Send a message to the server through the socket.
     *
     * @param message the message to send
     */
    public void send(Message message) {
        try {
            output.reset();
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            messageHandler.process(new ErrorMessage("Error in send process", ErrorType.SOCKET_ERROR));
        }
    }

    /**
     * Reads messages received from the server and calls the {@link MessageHandler#process(Message)}
     * method to process them. Closes the socket when a {@link CloseMessage} is received.
     *
     */
    @Override
    public void run() {
        Message message;
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