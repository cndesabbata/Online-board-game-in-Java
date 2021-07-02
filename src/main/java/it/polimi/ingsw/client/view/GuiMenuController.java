package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientNetwork.ClientConnectionSocket;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.messages.clientMessages.JoinLobby;
import it.polimi.ingsw.messages.clientMessages.SetPlayersNumber;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;

/**
 * Class GuiMenuController manages the menu in the GUI.
 *
 */
public class GuiMenuController implements GuiController{
    private Gui gui;
    @FXML private TextField serverIP;
    @FXML private TextField serverPort;
    @FXML private Label connectionMessage;
    @FXML private TextField nickname;
    @FXML private Label mainMessage;
    @FXML private ChoiceBox<String> playersNumber;
    @FXML private ChoiceBox<String> lobbies;
    @FXML private Label lobbyMessage;
    @FXML private Label waitingMessage;
    private final String[] numberOptions = {"1","2","3","4"};


    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    /**
     * Tries to set up the connection between client and server.
     *
     */
    public void connect(){
        if (serverIP.getText().equals("") || serverPort.getText().equals("")){
            connectionMessage.setText("Error: missing parameters.");
        }
        else {
            try {
                Constants.setAddress(serverIP.getText());
                Constants.setPort(Integer.parseInt(serverPort.getText()));
            } catch (NumberFormatException e) {
                connectionMessage.setText("Error: please insert correct values.");
                return;
            }
            try{
                if(!gui.getConnectionSocket().setupConnection()){
                    connectionMessage.setText("The entered IP/port doesn't match any active server. Please try again!");
                    return;
                }
            } catch (IOException e){
                System.err.println("Error during socket configuration! Application will now close.");
                System.exit(0);
            }
            gui.changeRoot("GuiMainMenu.fxml");
        }
    }

    /**
     * If the provided nickname is allowed, it sets it as the player nickname
     * and starts the thread listening on the socket.
     *
     */
    public void start() throws InterruptedException {
        if (nicknameCheck(true)) {
            gui.getView().setNickname(nickname.getText());
            gui.getView().getOwnGameBoard().setOwner(nickname.getText());
            Thread thread = new Thread(gui.getConnectionSocket());
            thread.start();
        }
    }

    /**
     * If the provided nickname is allowed, it sets it as the player nickname
     * and starts the thread listening on the socket.
     * 
     */
    public void resume(){
        if (nicknameCheck(false)){
            gui.getView().setNickname(nickname.getText());
            gui.getView().getOwnGameBoard().setOwner(nickname.getText());
            Thread thread = new Thread(gui.getConnectionSocket());
            thread.start();
        }
    }

    /**
     * Checks the nickname provided by the player and calls the
     * {@link ClientConnectionSocket#setupNickname(String, boolean)}.
     *
     * @param newPlayer {@code true} if the player is a new player, {@code false} otherwise
     * @return {@code true} if the player was registered in the server with the provided nickname,
     * {@code false} otherwise
     */
    private boolean nicknameCheck(boolean newPlayer) {
        if(nickname.getText().length()>16){
            mainMessage.setText("Error: the maximum length of nickname is 16 characters!");
            return false;
        }
        else if(nickname.getText().isEmpty()){
            mainMessage.setText("Error: missing parameters");
            return false;
        }
        return gui.getConnectionSocket().setupNickname(nickname.getText(), newPlayer);
    }

    /**
     * Tries to join a lobby sending the server a {@link JoinLobby} message
     * through the socket.
     *
     */
    public void join(){
        String lobbyJoined = lobbies.getValue();
        if (lobbyJoined == null || lobbyJoined.isEmpty()){
            lobbyMessage.setText("Error: missing parameters.");
            return;
        }
        gui.getConnectionSocket().send(new JoinLobby(lobbyJoined));
    }

    /**
     * Creates a new lobby, sending the server a {@link SetPlayersNumber} message
     * through the socket.
     *
     */
    public void newLobby(){
        int players;
        try{
            players = Integer.parseInt(playersNumber.getValue());
        } catch (InputMismatchException | NumberFormatException e){
            lobbyMessage.setText("Error: missing parameters.");
            return;
        }
        gui.getConnectionSocket().send(new SetPlayersNumber(players));
    }

    /**
     * Sets the main message.
     *
     * @param message the new main message
     */
    public void setMainMessage(String message) {
        mainMessage.setText(message);
    }

    /**
     * Sets the waiting message.
     *
     * @param message the new waiting message
     */
    public void setWaitingMessage(String message) {
        waitingMessage.setText(message);
    }

    /**
     * Initializes the lobbies the player can choose in the gui.
     *
     * @param lobbyList
     */
    public void initializeLobby(List<String> lobbyList){
        playersNumber.getItems().clear();
        playersNumber.getItems().addAll(numberOptions);
        lobbies.getItems().clear();
        lobbies.getItems().addAll(lobbyList);
    }
}
