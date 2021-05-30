package it.polimi.ingsw.client.view;

import it.polimi.ingsw.constants.Constants;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class GuiMenuController implements GuiController{
    private Gui gui;
    @FXML private TextField serverIP;
    @FXML private TextField serverPort;
    @FXML private Label connectionMessage;
    @FXML private TextField nickname;
    @FXML private Label mainMessage;


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

    public void start(){
        if (nicknameCheck(true)) {
            gui.getView().setNickname(nickname.getText());
            gui.getView().getOwnGameBoard().setOwner(nickname.getText());
            Thread thread = new Thread(gui.getConnectionSocket());
            thread.start();
            gui.changeStage("GuiLobbyMenu.fxml");
        }
    }

    public void resume(){
        if (nicknameCheck(false)){
            gui.getView().setNickname(nickname.getText());
            gui.getView().getOwnGameBoard().setOwner(nickname.getText());
            Thread thread = new Thread(gui.getConnectionSocket());
            thread.start();
            gui.changeStage("GuiGame.fxml");
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

    }

    public void newLobby(){

    }

    public void setMainMessage(String message) {
        mainMessage.setText(message);
    }
}
