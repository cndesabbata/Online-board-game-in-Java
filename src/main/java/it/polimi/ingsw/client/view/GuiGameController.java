package it.polimi.ingsw.client.view;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class GuiGameController implements GuiController{

    @FXML public ImageView external;
    @FXML private ImageView market_00;
    @FXML private ImageView market_01;
    @FXML private ImageView market_02;
    @FXML private ImageView market_03;
    @FXML private ImageView market_10;
    @FXML private ImageView market_11;
    @FXML private ImageView market_12;
    @FXML private ImageView market_13;
    @FXML private ImageView market_20;
    @FXML private ImageView market_21;
    @FXML private ImageView market_22;
    @FXML private ImageView market_23;

    @FXML private ImageView first_green;
    @FXML private ImageView first_blue;
    @FXML private ImageView first_yellow;
    @FXML private ImageView first_purple;
    @FXML private ImageView second_green;
    @FXML private ImageView second_blue;
    @FXML private ImageView second_yellow;
    @FXML private ImageView second_purple;
    @FXML private ImageView third_green;
    @FXML private ImageView third_blue;
    @FXML private ImageView third_yellow;
    @FXML private ImageView third_purple;

    private List<ImageView> marbles = new ArrayList<>();
    private List<ImageView> devDecks = new ArrayList<>();

    private Gui gui;
    private ClientView view;

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
        view = gui.getView();
    }

    public void initializeGame(){
        initializeMarket();
    }

    private void initializeMarket() {
        marbles.add(market_00);
        marbles.add(market_01);
        marbles.add(market_02);
        marbles.add(market_03);
        marbles.add(market_10);
        marbles.add(market_11);
        marbles.add(market_12);
        marbles.add(market_13);
        marbles.add(market_20);
        marbles.add(market_21);
        marbles.add(market_22);
        marbles.add(market_23);
        updateMarket();
        devDecks.add(first_green);
        devDecks.add(first_blue);
        devDecks.add(first_yellow);
        devDecks.add(first_purple);
        devDecks.add(second_green);
        devDecks.add(second_blue);
        devDecks.add(second_yellow);
        devDecks.add(second_purple);
        devDecks.add(third_green);
        devDecks.add(third_blue);
        devDecks.add(third_yellow);
        devDecks.add(third_purple);
        updateDevDecks();
    }

    private void updateDevDecks() {
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 4; j++){
                String url = "/graphics/marbles/" + view.getMarket()[i][j] + ".png";
                Image m = new Image(getClass().getResourceAsStream(url));
                devDecks.get(i*4+j).setImage(m);
            }
        }
    }

    private void updateMarket() {
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 4; j++){
                String url = "/graphics/marbles/" + view.getMarket()[i][j] + ".png";
                Image m = new Image(getClass().getResourceAsStream(url));
                marbles.get(i*4+j).setImage(m);
            }
        }
        String url = "/graphics/marbles/" + view.getExternalMarble() + ".png";
        Image m = new Image(getClass().getResourceAsStream(url));
        external.setImage(m);
    }




}
