package it.polimi.ingsw.client.view;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.messages.clientMessages.JoinLobby;
import it.polimi.ingsw.messages.clientMessages.SetPlayersNumber;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

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
            gui.changeStage("GuiMainMenu.fxml");
        }
    }


    public void start() throws InterruptedException {
        if (nicknameCheck(true)) {
            gui.getView().setNickname(nickname.getText());
            gui.getView().getOwnGameBoard().setOwner(nickname.getText());
            Thread thread = new Thread(gui.getConnectionSocket());
            thread.start();
        }
    }


    public void resume() throws InterruptedException {
        if (nicknameCheck(false)){
            gui.getView().setNickname(nickname.getText());
            gui.getView().getOwnGameBoard().setOwner(nickname.getText());
            Thread thread = new Thread(gui.getConnectionSocket());
            thread.start();
        }
    }

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

    public void join(){
        String lobbyJoined = lobbies.getValue();
        if (lobbyJoined == null || lobbyJoined.isEmpty()){
            lobbyMessage.setText("Error: missing parameters.");
            return;
        }
        gui.getConnectionSocket().send(new JoinLobby(lobbyJoined));
    }

    public void newLobby(){
        int players;
        try{
            players = Integer.parseInt(playersNumber.getValue());
        } catch (InputMismatchException e){
            lobbyMessage.setText("Error: missing parameters.");
            return;
        }
        gui.getConnectionSocket().send(new SetPlayersNumber(players));
    }

    public void setLobbyMessage(String message){
        waitingMessage.setText(message);
    }

    public void setMainMessage(String message) {
        mainMessage.setText(message);
    }

    public void initializeLobby(List<String> lobbyList){
        playersNumber.getItems().addAll(numberOptions);
        lobbies.getItems().addAll(lobbyList);
    }
}
