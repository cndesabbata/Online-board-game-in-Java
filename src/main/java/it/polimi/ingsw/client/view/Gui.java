package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientNetwork.ClientConnectionSocket;
import it.polimi.ingsw.client.clientNetwork.MessageHandler;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.clientMessages.internal.DisplayMessage;
import it.polimi.ingsw.messages.clientMessages.internal.NewView;
import it.polimi.ingsw.messages.clientMessages.internal.RequestPlayersNumber;
import it.polimi.ingsw.server.observer.Observer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class Gui extends Application implements Observer {

    public static final String CONNECTION_MENU = "GuiConnectionMenu.fxml";
    public static final String MAIN_MENU = "GuiMainMenu.fxml";
    public static final String LOBBY_MENU = "GuiLobbyMenu.fxml";
    public static final String GUI_GAME = "GuiGame.fxml";
    public static final String WAIT_PLAYERS = "GuiWaitingPlayers.fxml";
    private Stage stage;
    private final HashMap<String, Scene> nameToScene = new HashMap<>();
    private final HashMap<String, GuiController> nameToController = new HashMap<>();
    private final ClientView view;
    private final MessageHandler messageHandler;
    private final ClientConnectionSocket connectionSocket;
    private boolean active;
    private Scene currentScene;

    public static void main(String[] args){
        launch(args);
    }

    public Gui() {
        this.view = new ClientView(this);
        this.messageHandler = new MessageHandler(view);
        this.active = true;
        this.connectionSocket = new ClientConnectionSocket(this, messageHandler);
    }

    public ClientView getView() {
        return view;
    }

    public ClientConnectionSocket getConnectionSocket() {
        return connectionSocket;
    }

    @Override
    public void start(Stage stage) throws Exception {
        setup();
        this.stage = stage;
        try{
            stage.setTitle("Master Of Renaissance");
            stage.setScene(currentScene);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/graphics/GiglioFirenze.png"))));
            stage.setResizable(false);
            stage.setMaximized(true);
            stage.setFullScreen(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.show();
        } catch (NullPointerException e){
            System.out.println("Null pointer exception");
            e.printStackTrace();
        }
    }

    private void setup() {
        List<String> fxmList = new ArrayList<>(Arrays.asList(CONNECTION_MENU, MAIN_MENU, LOBBY_MENU, GUI_GAME, WAIT_PLAYERS));
        try {
            for (String path : fxmList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + path));
                nameToScene.put(path, new Scene(loader.load()));
                GuiController controller = loader.getController();
                controller.setGui(this);
                nameToController.put(path, controller);
            }
        } catch (IOException e) {
            System.out.println("Error in Gui configuration.");
            e.printStackTrace();
        }
        currentScene = nameToScene.get(CONNECTION_MENU);
    }

    public void changeScene(String newScene) {
        currentScene = nameToScene.get(newScene);
        stage.setScene(currentScene);
        stage.setResizable(false);
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
    }

    public void initializeGame(){
        ((GuiGameController) nameToController.get(GUI_GAME)).initializeGame();
    }

    @Override
    public void update(Message message) {
        if (message instanceof DisplayMessage){
            DisplayMessage m = (DisplayMessage) message;
            if (currentScene.equals(nameToScene.get(MAIN_MENU))){
                Platform.runLater(() -> {
                    ((GuiMenuController) nameToController.get(MAIN_MENU)).setMainMessage(m.getMessage());
                });
            }
            else if (currentScene.equals(nameToScene.get(LOBBY_MENU))){
                Platform.runLater(() -> {
                    changeScene(WAIT_PLAYERS);
                });
            }
            else if (currentScene.equals(nameToScene.get(WAIT_PLAYERS)))
                Platform.runLater(() -> {
                    ((GuiMenuController) nameToController.get(WAIT_PLAYERS)).setWaitingMessage(m.getMessage());
                });
        }
        else if (message instanceof RequestPlayersNumber){
            RequestPlayersNumber r = (RequestPlayersNumber) message;
            Platform.runLater(() -> {
                ((GuiMenuController) nameToController.get(LOBBY_MENU)).initializeLobby(r.getOwners());
                changeScene(LOBBY_MENU);
            });
        }
        else if (message instanceof NewView){
            Platform.runLater(() -> {
                ((GuiGameController) nameToController.get(GUI_GAME)).initializeGame();
                changeScene(GUI_GAME);
            });
        }
    }
}
