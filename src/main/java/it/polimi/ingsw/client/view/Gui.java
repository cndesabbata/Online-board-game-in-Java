package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientNetwork.ClientConnectionSocket;
import it.polimi.ingsw.client.clientNetwork.MessageHandler;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.clientMessages.internal.*;
import it.polimi.ingsw.server.observer.Observer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

/**
 * Class Gui launches the graphical user interface. It observes the client view and calls
 * different methods to update the gui when it's notified with a {@link ViewMessage}.
 *
 */
public class Gui extends Application implements Observer {

    public static final String CONNECTION_MENU = "GuiConnectionMenu.fxml";
    public static final String MAIN_MENU = "GuiMainMenu.fxml";
    public static final String LOBBY_MENU = "GuiLobbyMenu.fxml";
    public static final String GUI_GAME = "GuiGame.fxml";
    public static final String WAIT_PLAYERS = "GuiWaitingPlayers.fxml";
    private Stage stage;
    private final HashMap<String, Parent> nameToRoot = new HashMap<>();
    private final HashMap<String, GuiController> nameToController = new HashMap<>();
    private final ClientView view;
    private final ClientConnectionSocket connectionSocket;
    private Scene currentScene;
    private String currentStatus;
    private MediaPlayer mediaPlayer;                                                                                    //this attribute is needed to keep the music going after the end of method start

    /**
     * Launches the application.
     *
     * @param args the main args
     */
    public static void main(String[] args){
        launch(args);
    }

    /**
     * Creates a new Gui instance.
     *
     */
    public Gui() {
        this.view = new ClientView(this);
        MessageHandler messageHandler = new MessageHandler(view);
        this.connectionSocket = new ClientConnectionSocket(this, messageHandler);
    }

    /**
     * Returns the client view associated with this Gui object.
     *
     * @return the associated client view
     */
    public ClientView getView() {
        return view;
    }

    /**
     * Returns the ClientConnectionSocket object associated with this Gui object.
     *
     * @return the ClientConnectionSocket object
     */
    public ClientConnectionSocket getConnectionSocket() {
        return connectionSocket;
    }

    /**
     * @see Application#start(Stage) 
     */
    @Override
    public void start(Stage stage) throws Exception {
        setup();
        this.stage = stage;
        try{
            stage.setTitle("Master Of Renaissance");
            stage.setScene(currentScene);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/graphics/IrisFlorence.png"))));
            stage.setResizable(false);
            stage.setMaximized(true);
            stage.setFullScreen(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            Media audio = new Media(Objects.requireNonNull(getClass().getClassLoader()
                    .getResource("audio/Intro.mp3")).toExternalForm());
            mediaPlayer = new MediaPlayer(audio);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.15);
            mediaPlayer.setAutoPlay(true);
            stage.show();
        } catch (NullPointerException e){
            System.out.println("Null pointer exception");
        }
    }

    /**
     * Builds hashmap with scenes for simpler transactions and sets current scene to the connection menu.
     */
    //setup method
    private void setup() {
        List<String> fxmList = new ArrayList<>(Arrays.asList(CONNECTION_MENU, MAIN_MENU, LOBBY_MENU, GUI_GAME, WAIT_PLAYERS));
        try {
            for (String path : fxmList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + path));
                nameToRoot.put(path, loader.load());
                GuiController controller = loader.getController();
                controller.setGui(this);
                nameToController.put(path, controller);
            }
        } catch (IOException e) {
            System.out.println("Error in Gui configuration.");
            e.printStackTrace();
        }
        currentScene = new Scene(nameToRoot.get(CONNECTION_MENU));
        currentStatus = CONNECTION_MENU;
    }

    /**
     * Changes root of the current scene.
     * @param newScene the name of the new root.
     */
    public void changeRoot(String newScene) {
       currentScene.setRoot(nameToRoot.get(newScene));
       currentStatus = newScene;
    }

    /**
     * Creates a new scene, sets it as the current scene, and maximizes it to full screen.
     * @param newScene the name of the new scene.
     */
    public void changeScene(String newScene) {
        currentScene = new Scene(nameToRoot.get(newScene));
        currentStatus = newScene;
        stage.setScene(currentScene);
        stage.setResizable(false);
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
    }

    /**
     * Changes root, scene, or elements of the scene according to view updates notified through observer pattern.
     * @param message message notified by the view.
     */
    @Override
    public void update(Message message) {
        if (message instanceof DisplayMessage){
            DisplayMessage m = (DisplayMessage) message;
            switch (currentStatus) {
                case MAIN_MENU -> Platform.runLater(() -> ((GuiMenuController) nameToController.get(MAIN_MENU)).setMainMessage(m.getMessage()));
                case LOBBY_MENU -> Platform.runLater(() -> changeRoot(WAIT_PLAYERS));
                case WAIT_PLAYERS -> Platform.runLater(() -> ((GuiMenuController) nameToController.get(WAIT_PLAYERS)).setWaitingMessage(m.getMessage()));
                default -> Platform.runLater(() -> ((GuiGameController) nameToController.get(GUI_GAME)).setMessage(m.getMessage()));
            }
        }
        else if (message instanceof RequestPlayersNumber){
            RequestPlayersNumber r = (RequestPlayersNumber) message;
            Platform.runLater(() -> {
                ((GuiMenuController) nameToController.get(LOBBY_MENU)).initializeLobby(r.getOwners());
                changeRoot(LOBBY_MENU);
            });
        }
        else if (message instanceof NewView){
            currentStatus = GUI_GAME;
            Platform.runLater(() -> {
                ((GuiGameController) nameToController.get(GUI_GAME)).initializeGame();
                changeScene(GUI_GAME);
            });
        }
        else if (message instanceof SetupDiscard){
            Platform.runLater(() -> ((GuiGameController) nameToController.get(GUI_GAME)).showSetupCards());
        }
        else if (message instanceof SetupResources){
            Platform.runLater(() -> ((GuiGameController) nameToController.get(GUI_GAME)).selectSetupResources());
        }
        else if (message instanceof PrintHandCards && currentStatus.equals(GUI_GAME)){
            Platform.runLater(() -> ((GuiGameController) nameToController.get(GUI_GAME)).updateHandCards());
        }
        else if (message instanceof PrintChest && currentStatus.equals(GUI_GAME)){
            Platform.runLater(() -> ((GuiGameController) nameToController.get(GUI_GAME)).updateChest(((PrintChest) message).getMessage()));
        }
        else if (message instanceof PrintDevDecks  && currentStatus.equals(GUI_GAME)){
            Platform.runLater(() -> ((GuiGameController) nameToController.get(GUI_GAME)).updateDevDecks());
        }
        else if (message instanceof PrintDevSpace  && currentStatus.equals(GUI_GAME)){
            Platform.runLater(() -> ((GuiGameController) nameToController.get(GUI_GAME)).updateDevSpace(((PrintDevSpace) message).getMessage()));
        }
        else if (message instanceof PrintItinerary && currentStatus.equals(GUI_GAME)){
            Platform.runLater(() -> ((GuiGameController) nameToController.get(GUI_GAME)).updateItinerary(((PrintItinerary) message).getMessage()));
        }
        else if (message instanceof PrintMarket && currentStatus.equals(GUI_GAME)){
            Platform.runLater(() -> ((GuiGameController) nameToController.get(GUI_GAME)).updateMarket());
        }
        else if (message instanceof PrintPlayedCards && currentStatus.equals(GUI_GAME)){
            Platform.runLater(() -> ((GuiGameController) nameToController.get(GUI_GAME)).updatePlayedCards(((PrintPlayedCards) message).getMessage()));
        }
        else if (message instanceof PrintWarehouse && currentStatus.equals(GUI_GAME)){
            Platform.runLater(() -> ((GuiGameController) nameToController.get(GUI_GAME)).updateWarehouse(((PrintWarehouse) message).getMessage()));
        }
        else if (message instanceof ChooseAction){
            Platform.runLater(() -> ((GuiGameController) nameToController.get(GUI_GAME)).enableAction(((ChooseAction) message).getMessage()));
        }
    }
}
