package it.polimi.ingsw.client.view;


import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.observer.Observer;
import javafx.application.Application;
import javafx.stage.Stage;

public class GUI extends Application implements Observer {

    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        setup();
        this.stage = stage;
    }

    private void setup() {
    }

    @Override
    public void update(Message message) {

    }
}
