package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientNetwork.MessageHandler;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.observer.Observer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Gui extends Application implements Observer {

    public static final String CONNECTION_MENU = "GuiConnectionMenu.fxml";
    public static final String MAIN_MENU = "GuiMainMenu.fxml";
    public static final String WAIT_MENU = "GuiWaitMenu.fxml";
    public static final String GUI_GAME = "GuiGame.fxml";
    private Stage stage;
    private final HashMap<String, Scene> nameToScene = new HashMap<>();
    private final HashMap<String, GuiController> nameMapController = new HashMap<>();
    private final ClientView view;
    private final MessageHandler messageHandler;
    private boolean active;
    private Scene currentScene;

    public static void main(String[] args) {
        launch(args);
    }

    public Gui() {
        this.view = new ClientView(this);
        messageHandler = new MessageHandler(view);
        active = true;
    }

    public ClientView getView() {
        return view;
    }

    @Override
    public void start(Stage stage) throws Exception {
        setup();
        this.stage = stage;
        stage.setTitle("MasterOfRenaissance");
        stage.setScene(currentScene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("graphics/icons/MasterOfReinassance.jpg")));
        stage.show();
    }

    private void setup() {
        List<String> fxmList = new ArrayList<>(Arrays.asList(CONNECTION_MENU, MAIN_MENU, WAIT_MENU, GUI_GAME));
        try {
            for (String path : fxmList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + path));
                nameToScene.put(path, new Scene(loader.load()));
                GuiController controller = loader.getController();
                controller.setGui(this);
                nameMapController.put(path, controller);
            }
        } catch (IOException e) {
            System.out.println("Error in Gui configuration.");
        }
        currentScene = nameToScene.get(CONNECTION_MENU);
    }

    @Override
    public void update(Message message) {

    }
}
