package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientNetwork.ClientConnectionSocket;
import it.polimi.ingsw.messages.actions.BuyResources;
import it.polimi.ingsw.messages.clientMessages.LeaderCardSelection;
import it.polimi.ingsw.messages.clientMessages.ResourceSelection;
import it.polimi.ingsw.server.controller.GamePhase;
import it.polimi.ingsw.server.controller.Place;
import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.model.MarketSelection;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.ResourcePosition;
import it.polimi.ingsw.server.model.gameboard.NumOfShelf;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GuiGameController implements GuiController{


    //Market
    @FXML private Button market_c1;
    @FXML private Button market_c2;
    @FXML private Button market_c3;
    @FXML private Button market_c4;
    @FXML private Button market_r1;
    @FXML private Button market_r2;
    @FXML private Button market_r3;
    @FXML private ImageView external;
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
    private int position;
    private MarketSelection selection;
    private final List<ImageView> marbles = new ArrayList<>();
    private final List<Button> marketButtons = new ArrayList<>();

    //DevDecks
    @FXML private Button devspace_00;
    @FXML private Button devspace_01;
    @FXML private Button devspace_02;
    @FXML private Button devspace_10;
    @FXML private Button devspace_11;
    @FXML private Button devspace_12;
    @FXML private Button devspace_20;
    @FXML private Button devspace_21;
    @FXML private Button devspace_22;
    @FXML private Button devspace_30;
    @FXML private Button devspace_31;
    @FXML private Button devspace_32;
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
    private final List<ImageView> devDecks = new ArrayList<>();
    private final List<Button> devDeckButtons = new ArrayList<>();

    //PlayedCards
    @FXML private Button played_1;
    @FXML private Button played_2;
    @FXML private ImageView first_played;
    @FXML private ImageView second_played;
    private final List<Button> playedCardsButtons = new ArrayList<>();


    //HandCards
    @FXML private Button hand_1;
    @FXML private Button hand_2;
    @FXML private ImageView second_hand;
    @FXML private ImageView first_hand;
    private List<ImageView> hand;
    private final List<Button> handButtons = new ArrayList<>();


    //Itinerary
    @FXML private ImageView papal2;
    @FXML private ImageView papal1;
    @FXML private ImageView papal3;
    @FXML private ImageView blackcross;
    @FXML private ImageView cross;
    private List<Integer[]> coordinates;

    //Chest and Warehouse
    @FXML private Label chest_coin;
    @FXML private Label chest_stone;
    @FXML private Label chest_shield;
    @FXML private Label chest_servant;
    @FXML private Button chest;
    @FXML private Button shelf1;
    @FXML private Button shelf3;
    @FXML private Button shelf2;
    @FXML private ImageView first_shelf;
    @FXML private ImageView second_shelf_0;
    @FXML private ImageView second_shelf_1;
    @FXML private ImageView third_shelf_0;
    @FXML private ImageView third_shelf_1;
    @FXML private ImageView third_shelf_2;
    private final List<ImageView> second_shelf = new ArrayList<>();
    private final List<ImageView> third_shelf = new ArrayList<>();
    private int resourceToSelect;
    private String selectedResource;
    private final List<Button> warehouseButtons = new ArrayList<>();
    private final List<Button> chestButtons = new ArrayList<>();
    private List<ResourcePosition> resourcesForAction = new ArrayList<>();


    //DevSpace
    @FXML private Button slot_1;
    @FXML private Button slot_2;
    @FXML private Button slot_3;
    @FXML private Button board_production;
    @FXML private ImageView first_slot_0;
    @FXML private ImageView first_slot_1;
    @FXML private ImageView first_slot_2;
    @FXML private ImageView second_slot_0;
    @FXML private ImageView second_slot_1;
    @FXML private ImageView second_slot_2;
    @FXML private ImageView third_slot_0;
    @FXML private ImageView third_slot_1;
    @FXML private ImageView third_slot_2;
    private final List<ImageView> firstSlot = new ArrayList<>();
    private final List<ImageView> secondSlot = new ArrayList<>();
    private final List<ImageView> thirdSlot = new ArrayList<>();
    private final List<Button> devSpaceButtons = new ArrayList<>();                                                           //also contains board_production


    //Message & Board
    @FXML private Group group;
    @FXML private Label message;
    @FXML private Button back_button;
    @FXML private Button confirm_button;
    @FXML private Button end_turn;
    @FXML private Button right_gameboard;
    @FXML private Button left_gameboard;
    @FXML private Button coin_button;
    @FXML private Button stone_button;
    @FXML private Button servant_button;
    @FXML private Button shield_button;
    @FXML private ImageView message_coin;
    @FXML private ImageView message_stone;
    @FXML private ImageView message_servant;
    @FXML private ImageView message_shield;
    @FXML private Label message_coin_number;
    @FXML private Label message_stone_number;
    @FXML private Label message_servant_number;
    @FXML private Label message_shield_number;
    @FXML private ImageView inkwell;
    private final List<ImageView> messageResources = new ArrayList<>();
    private final List<Button> messageResourcesButtons = new ArrayList<>();
    private final List<Label> messageResourcesNumbers = new ArrayList<>();
    List <String> resourcesUrl = new ArrayList<>();

    //SetupDraw
    @FXML private ImageView setupdraw1;
    @FXML private ImageView setupdraw2;
    @FXML private ImageView setupdraw3;
    @FXML private ImageView setupdraw4;
    @FXML private Button setupdraw_button1;
    @FXML private Button setupdraw_button2;
    @FXML private Button setupdraw_button3;
    @FXML private Button setupdraw_button4;
    private final List<Button> setupDrawButtons = new ArrayList<>();

    private Gui gui;
    private ClientView view;
    private ClientConnectionSocket connectionSocket;
    private UserAction currentAction;
    private GameBoardInfo currentGameboard;

    private int[] leadCardIndexes = {-1,-1};

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
        this.view = gui.getView();
        this.connectionSocket = gui.getConnectionSocket();
        this.currentGameboard = view.getOwnGameBoard();
    }

    private void handleButtons(List<Button> buttons, boolean b){
        for(Button button : buttons){
            button.setDisable(b);
        }
    }

    public void initializeGame(){
        initializeMarket();
        initializeDevDecks();
        initializeDevSpaceSlots();
        initializeCoordinates();
        initializeWarehouse();
        initializeHandCards();
        initializePlayedCards();
        initializeMessagePanel();
    }

    public void initializeMessagePanel() {
        messageResources.add(message_coin);
        messageResources.add(message_stone);
        messageResources.add(message_servant);
        messageResources.add(message_shield);
        message_coin.imageProperty().set(null);
        message_stone.imageProperty().set(null);
        message_servant.imageProperty().set(null);
        message_shield.imageProperty().set(null);
        setupDrawButtons.add(setupdraw_button1);
        setupDrawButtons.add(setupdraw_button2);
        setupDrawButtons.add(setupdraw_button3);
        setupDrawButtons.add(setupdraw_button4);
        handleButtons(setupDrawButtons, false);
        messageResourcesButtons.add(coin_button);
        messageResourcesButtons.add(stone_button);
        messageResourcesButtons.add(servant_button);
        messageResourcesButtons.add(shield_button);
        handleButtons(messageResourcesButtons, true);
        messageResourcesNumbers.add(message_coin_number);
        messageResourcesNumbers.add(message_stone_number);
        messageResourcesNumbers.add(message_servant_number);
        messageResourcesNumbers.add(message_shield_number);
        resourcesUrl.add("/graphics/resources/coin.png");
        resourcesUrl.add("/graphics/resources/stone.png");
        resourcesUrl.add("/graphics/resources/servant.png");
        resourcesUrl.add("/graphics/resources/shield.png");
        confirm_button.setDisable(true);
        end_turn.setDisable(true);
        back_button.setDisable(true);
    }

    private void initializePlayedCards() {
        first_played.imageProperty().set(null);
        second_played.imageProperty().set(null);
        playedCardsButtons.add(played_1);
        playedCardsButtons.add(played_2);
        handleButtons(playedCardsButtons, true);
    }

    private void initializeHandCards() {
        first_hand.imageProperty().set(null);
        second_hand.imageProperty().set(null);
        handButtons.add(hand_1);
        handButtons.add(hand_2);
        handleButtons(handButtons, true);
    }

    private void initializeWarehouse() {
        second_shelf.add(second_shelf_0);
        second_shelf.add(second_shelf_1);
        third_shelf.add(third_shelf_0);
        third_shelf.add(third_shelf_1);
        third_shelf.add(third_shelf_2);
        warehouseButtons.add(shelf1);
        warehouseButtons.add(shelf2);
        warehouseButtons.add(shelf3);
    }

    private void disableButtons(){
        back_button.setDisable(true);
        confirm_button.setDisable(true);
        end_turn.setDisable(true);
        right_gameboard.setDisable(true);
        left_gameboard.setDisable(true);
        market_c1.setDisable(true);
        market_c2.setDisable(true);
        market_c3.setDisable(true);
        market_c4.setDisable(true);
        market_r1.setDisable(true);
        market_r2.setDisable(true);
        market_r3.setDisable(true);
        devspace_00.setDisable(true);
        devspace_01.setDisable(true);
        devspace_02.setDisable(true);
        devspace_10.setDisable(true);
        devspace_11.setDisable(true);
        devspace_12.setDisable(true);
        devspace_20.setDisable(true);
        devspace_21.setDisable(true);
        devspace_22.setDisable(true);
        devspace_30.setDisable(true);
        devspace_31.setDisable(true);
        devspace_32.setDisable(true);
        played_1.setDisable(true);
        played_2.setDisable(true);
        hand_1.setDisable(true);
        hand_2.setDisable(true);
        slot_1.setDisable(true);
        slot_2.setDisable(true);
        slot_3.setDisable(true);
        board_production.setDisable(true);
        chest.setDisable(true);
        shelf1.setDisable(true);
        shelf2.setDisable(true);
        shelf3.setDisable(true);
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
        marketButtons.add(market_c1);
        marketButtons.add(market_c2);
        marketButtons.add(market_c3);
        marketButtons.add(market_c4);
        marketButtons.add(market_r1);
        marketButtons.add(market_r2);
        marketButtons.add(market_r3);
        updateMarket();
    }

    private void initializeDevDecks(){
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
        devDeckButtons.add(devspace_00);
        devDeckButtons.add(devspace_01);
        devDeckButtons.add(devspace_02);
        devDeckButtons.add(devspace_10);
        devDeckButtons.add(devspace_11);
        devDeckButtons.add(devspace_12);
        devDeckButtons.add(devspace_20);
        devDeckButtons.add(devspace_21);
        devDeckButtons.add(devspace_22);
        devDeckButtons.add(devspace_30);
        devDeckButtons.add(devspace_31);
        devDeckButtons.add(devspace_32);
        updateDevDecks();
    }

    private void initializeDevSpaceSlots(){
        firstSlot.add(first_slot_0);
        firstSlot.add(first_slot_1);
        firstSlot.add(first_slot_2);
        secondSlot.add(second_slot_0);
        secondSlot.add(second_slot_1);
        secondSlot.add(second_slot_2);
        thirdSlot.add(third_slot_0);
        thirdSlot.add(third_slot_1);
        thirdSlot.add(third_slot_2);
        devSpaceButtons.add(slot_1);
        devSpaceButtons.add(slot_2);
        devSpaceButtons.add(slot_3);
        devSpaceButtons.add(board_production);
    }

    private void initializeCoordinates(){
        this.coordinates = new ArrayList<>();
        coordinates.add( new Integer[]{33, 153});
        coordinates.add( new Integer[]{65, 153});
        coordinates.add( new Integer[]{97, 153});
        coordinates.add( new Integer[]{97, 121});
        coordinates.add( new Integer[]{97, 89});
        coordinates.add( new Integer[]{129, 89});
        coordinates.add( new Integer[]{161, 89});
        coordinates.add( new Integer[]{193, 89});
        coordinates.add( new Integer[]{225, 89});
        coordinates.add( new Integer[]{257, 89});
        coordinates.add( new Integer[]{257, 121});
        coordinates.add( new Integer[]{257, 153});
        coordinates.add( new Integer[]{289, 153});
        coordinates.add( new Integer[]{321, 153});
        coordinates.add( new Integer[]{353, 153});
        coordinates.add( new Integer[]{385, 153});
        coordinates.add( new Integer[]{417, 153});
        coordinates.add( new Integer[]{417, 121});
        coordinates.add( new Integer[]{417, 89});
        coordinates.add( new Integer[]{449, 89});
        coordinates.add( new Integer[]{481, 89});
        coordinates.add( new Integer[]{513, 89});
        coordinates.add( new Integer[]{545, 89});
        coordinates.add( new Integer[]{577, 89});
        coordinates.add( new Integer[]{601, 89});
    }



    public void updateDevDecks() {
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 4; j++){
                String url = "/graphics/devcards/" + view.getDevDecks()[i][j].getUrl();
                Image m = new Image(getClass().getResourceAsStream(url));
                devDecks.get(i*4+j).setImage(m);
            }
        }
    }

    public void updateMarket() {
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

    public void showSetupCards() {
        currentAction = UserAction.SELECT_LEADCARD;
        message.setText("These are your new hand cards.\nPlease select the two you want to discard.");
        String url;
        Image m;
        url = "/graphics/leadcards/" + view.getHand().get(0).getUrl();
        m = new Image(getClass().getResourceAsStream(url));
        setupdraw1.setImage(m);
        url = "/graphics/leadcards/" + view.getHand().get(1).getUrl();
        m = new Image(getClass().getResourceAsStream(url));
        setupdraw2.setImage(m);
        url = "/graphics/leadcards/" + view.getHand().get(2).getUrl();
        m = new Image(getClass().getResourceAsStream(url));
        setupdraw3.setImage(m);
        url = "/graphics/leadcards/" + view.getHand().get(3).getUrl();
        m = new Image(getClass().getResourceAsStream(url));
        setupdraw4.setImage(m);
        setupdraw_button1.getStyleClass().add("clickable");
        setupdraw_button2.getStyleClass().add("clickable");
        setupdraw_button3.getStyleClass().add("clickable");
        setupdraw_button4.getStyleClass().add("clickable");
    }

    private void select_sd(int i) {
        if (leadCardIndexes[0] == -1){
            leadCardIndexes[0] = i;
            message.setText("Please choose another card:");
        }
        else{
            leadCardIndexes[1] = i;
            handleButtons(setupDrawButtons, true);
            connectionSocket.send(new LeaderCardSelection(leadCardIndexes));
            message.setText("");
            group.getChildren().remove(setupdraw1);
            group.getChildren().remove(setupdraw2);
            group.getChildren().remove(setupdraw3);
            group.getChildren().remove(setupdraw4);
            group.getChildren().remove(setupdraw_button1);
            group.getChildren().remove(setupdraw_button2);
            group.getChildren().remove(setupdraw_button3);
            group.getChildren().remove(setupdraw_button4);
            back_button.setDisable(false);
        }
    }

    @FXML
    public void select_sd1(ActionEvent actionEvent) {
        select_sd(1);
        setupdraw_button1.setDisable(true);
    }

    @FXML
    public void select_sd2(ActionEvent actionEvent) {
        select_sd(2);
        setupdraw_button2.setDisable(true);
    }
    @FXML
    public void select_sd3(ActionEvent actionEvent) {
        select_sd(3);
        setupdraw_button3.setDisable(true);
    }
    @FXML
    public void select_sd4(ActionEvent actionEvent) {
        select_sd(4);
        setupdraw_button4.setDisable(true);
    }

    public void updateHandCards() {
        if (view.getGamePhase() != GamePhase.SETUP || (view.getGamePhase() == GamePhase.SETUP && view.getHand().size() <= 2)
                && currentGameboard.getOwner().equalsIgnoreCase(view.getNickname())){
            String url;
            Image m;
            if (view.getHand().size() > 0){
                url = "/graphics/leadcards/" + view.getHand().get(0).getUrl();
                m = new Image(getClass().getResourceAsStream(url));
                first_hand.setImage(m);
                hand_1.setDisable(false);
            }
            else first_hand.imageProperty().set(null);
            if (view.getHand().size() > 1){
                url = "/graphics/leadcards/" + view.getHand().get(1).getUrl();
                m = new Image(getClass().getResourceAsStream(url));
                second_hand.setImage(m);
                hand_2.setDisable(false);
            }
            else second_hand.imageProperty().set(null);
        }
    }

    public void updateChest(String owner) {
        if (currentGameboard.getOwner().equalsIgnoreCase(owner)){
            Integer quantity;
            quantity = currentGameboard.getChest().get("Coins");
            chest_coin.setText(quantity.toString());
            quantity = currentGameboard.getChest().get("Stones");
            chest_stone.setText(quantity.toString());
            quantity = currentGameboard.getChest().get("Servants");
            chest_servant.setText(quantity.toString());
            quantity = currentGameboard.getChest().get("Shields");
            chest_shield.setText(quantity.toString());
        }
    }

    public void updateDevSpace(String owner) {
        if (currentGameboard.getOwner().equalsIgnoreCase(owner)){
            List<DevCardInfo> slot1 = currentGameboard.getDevSpace().get(1);
            for (int i = 0; i < 3; i++){
                if (slot1.size() <= i) firstSlot.get(i).imageProperty().set(null);
                else {
                    String url = "/graphics/devcards/" + slot1.get(i).getUrl();
                    Image m = new Image(getClass().getResourceAsStream(url));
                    firstSlot.get(i).setImage(m);
                }
            }
            List<DevCardInfo> slot2 = currentGameboard.getDevSpace().get(2);
            for (int i = 0; i < 3; i++){
                if (slot2.size() <= i) secondSlot.get(i).imageProperty().set(null);
                else {
                    String url = "/graphics/devcards/" + slot2.get(i).getUrl();
                    Image m = new Image(getClass().getResourceAsStream(url));
                    secondSlot.get(i).setImage(m);
                }
            }
            List<DevCardInfo> slot3 = currentGameboard.getDevSpace().get(3);
            for (int i = 0; i < 3; i++){
                if (slot3.size() <= i) thirdSlot.get(i).imageProperty().set(null);
                else {
                    String url = "/graphics/devcards/" + slot3.get(i).getUrl();
                    Image m = new Image(getClass().getResourceAsStream(url));
                    thirdSlot.get(i).setImage(m);
                }
            }
        }
    }

    public void updateItinerary(String owner) {
        if (currentGameboard.getOwner().equalsIgnoreCase(owner)){
            if(currentGameboard.getBlackCrossPosition()!= null){
                if (blackcross.getImage() == null){
                    Image m = new Image(getClass().getResourceAsStream("/graphics/itinerary/black_cross.png"));
                    blackcross.setImage(m);
                }
                Integer[] loc = coordinates.get(currentGameboard.getBlackCrossPosition());
                blackcross.setX(loc[0]+7);
                blackcross.setY(loc[1]+7);
            }
            cross.setX(coordinates.get(currentGameboard.getPosition())[0]);
            cross.setY(coordinates.get(currentGameboard.getPosition())[1]);
        }
    }

    public void updatePlayedCards(String owner) {
        if (currentGameboard.getOwner().equalsIgnoreCase(owner)){
            String url;
            Image m;
            if (currentGameboard.getPlayedCards().size()>0){
                url = "/graphics/leadcards/" + currentGameboard.getPlayedCards().get(0).getUrl();
                m = new Image(getClass().getResourceAsStream(url));
                first_played.setImage(m);
            }
            else first_played.imageProperty().set(null);
            if (currentGameboard.getPlayedCards().size()>1){
                url = "/graphics/leadcards/" + currentGameboard.getPlayedCards().get(1).getUrl();
                m = new Image(getClass().getResourceAsStream(url));
                second_played.setImage(m);
            }
            else second_played.imageProperty().set(null);
        }
    }

    public void updateWarehouse(String owner) {
        if (currentGameboard.getOwner().equalsIgnoreCase(owner)){
            String url;
            Image m;
            if (!currentGameboard.getWarehouse().get(0).isEmpty()
                    && !currentGameboard.getWarehouse().get(0).get(0).isEmpty()){
                url = "/graphics/resources/" + currentGameboard.getWarehouse().get(0).get(0).toLowerCase() + ".png";
                m = new Image(Objects.requireNonNull(getClass().getResourceAsStream(url)));
                first_shelf.setImage(m);
            }
            else first_shelf.imageProperty().set(null);
            for (int i = 0; i < 2; i++){
                if (currentGameboard.getWarehouse().get(1).size() > i
                        && !currentGameboard.getWarehouse().get(1).get(i).isEmpty()){
                    url = "/graphics/resources/" + currentGameboard.getWarehouse().get(1).get(i).toLowerCase() + ".png";
                    m = new Image(Objects.requireNonNull(getClass().getResourceAsStream(url)));
                    second_shelf.get(i).setImage(m);
                }
                else second_shelf.get(i).imageProperty().set(null);
            }
            for (int i = 0; i < 3; i++){
                if (currentGameboard.getWarehouse().get(2).size() > i
                        && !currentGameboard.getWarehouse().get(2).get(i).isEmpty()){
                    url = "/graphics/resources/" + currentGameboard.getWarehouse().get(2).get(i).toLowerCase() + ".png";
                    m = new Image(Objects.requireNonNull(getClass().getResourceAsStream(url)));
                    third_shelf.get(i).setImage(m);
                }
                else third_shelf.get(i).imageProperty().set(null);
            }
        }
    }

    public void setMessage(String s) {
        message.setText(s);
    }

    public void enableAction(String s) {
        message.setText(s);
        handleButtons(marketButtons, false);
        handleButtons(devSpaceButtons, false);
        confirm_button.setDisable(false);
        for(int i = 0; i < 3; i++){
            devSpaceButtons.get(i).setDisable(view.getOwnGameBoard().getDevSpace().get(i).isEmpty());
            for (int j = 0; j < 4; j++){
                devDeckButtons.get(i + j*3).setDisable(view.getDevDecks()[i][j] == null);
            }
        }
        handleButtons(handButtons, false);
        handleButtons(playedCardsButtons, false);
        for(int i = 0; i < 2; i++){
            handButtons.get(i).setDisable(view.getHand().size() <= i);
            playedCardsButtons.get(i).setDisable(view.getOwnGameBoard().getPlayedCards().size() <= i);
        }
        checkWarehouseButtons(false);
        handleButtons(chestButtons, true);
        currentAction = null;
    }

    public void selectSetupResources() {
        currentAction = UserAction.RESOURCE_SELECTION;
        handleButtons(messageResourcesButtons, false);
        int i = 0;
        for (String s : resourcesUrl) {
            Image m = new Image(Objects.requireNonNull(getClass().getResourceAsStream(s)));
            messageResources.get(i).setImage(m);
            i++;
        }
        Integer index = view.getPlayerIndex();
        if(index > 0) {
            String s;
            s = "You are player number " +(index+1)+ ",\nthis gives you access to";
            switch (index){
                case 1 -> {s = s + "an additional resource,\nplease select one:";
                    resourceToSelect = 1;}
                case 2 -> {s = s +"one faithpoint and an additional resource,\nplease select one:";
                    resourceToSelect = 1;}
                case 3 -> {s = s +"one faithpoint and two additional resources,\nplease select them:";
                    resourceToSelect = 2;}
                default -> s = s + "";
            }
            message.setText(s);
        }
    }

    private void checkWarehouseButtons(boolean toStore){
        shelf1.setDisable((toStore && first_shelf.getImage() != null)
                || (!toStore && first_shelf.getImage() == null));
        shelf2.setDisable((toStore && second_shelf.get(1).getImage() != null)
                || (!toStore && second_shelf.get(0).getImage() == null));
        shelf3.setDisable((toStore && third_shelf.get(2).getImage() != null)
                || (!toStore && third_shelf.get(0).getImage() == null));
    }

    /*used when some resources are needed*/
    private void selectResourcesPosition(boolean toStore, boolean alsoChest){
        checkWarehouseButtons(toStore);
        handleButtons(chestButtons, !alsoChest);
        handleButtons(devSpaceButtons, true);
        handleButtons(devDeckButtons, true);
        handleButtons(marketButtons, true);
        handleButtons(handButtons, true);
        handleButtons(playedCardsButtons, true);
        right_gameboard.setDisable(true);
        left_gameboard.setDisable(true);
    }

    public void select_coin(ActionEvent actionEvent) {
        selectedResource = "Coin";
        handleButtons(messageResourcesButtons, true);
        selectResourcesPosition(true, false);
        message.setText("Now click on one of the warehouse shelves");
    }

    public void select_stone(ActionEvent actionEvent) {
        selectedResource = "Stone";
        selectResourcesPosition(true,false);
        handleButtons(messageResourcesButtons, true);
        message.setText("Now click on one of the warehouse shelves");
    }

    public void select_servant(ActionEvent actionEvent) {
        selectedResource = "Servant";
        selectResourcesPosition(true, false);
        handleButtons(messageResourcesButtons, true);
        message.setText("Now click on one of the warehouse shelves");
    }

    public void select_shield(ActionEvent actionEvent) {
        selectedResource = "Shield";
        selectResourcesPosition(true, false);
        handleButtons(messageResourcesButtons, true);
        message.setText("Now click on one of the warehouse shelves");
    }

    private void selectWarehouse(String shelf){
        if(view.getGamePhase() == GamePhase.SETUP){
            updateWarehouseInAction(shelf);
            if(resourceToSelect == 0) {
                connectionSocket.send(new ResourceSelection(resourcesForAction));
                for (int i = 0; i < messageResourcesButtons.size(); i++) {
                    messageResourcesButtons.get(i).setDisable(true);
                    messageResources.get(i).imageProperty().set(null);
                    message.setText("");
                }
            }
            else
                handleButtons(messageResourcesButtons, false);
        }
        else if(currentAction == UserAction.BUY_RESOURCES){
            updateWarehouseInAction(shelf);
            int index = Arrays.asList(Resource.values()).indexOf(Resource.valueOf(selectedResource.toUpperCase()));
            messageResourcesNumbers.get(index).setText
                    (String.valueOf(Integer.parseInt(messageResourcesNumbers.get(index).getText()) - 1));
            if(messageResourcesNumbers.get(index).getText().equalsIgnoreCase("0")) {
                messageResourcesNumbers.get(index).setText("");
                messageResources.get(index).setImage(null);
            }
            if(resourceToSelect == 0) {
                confirm_button.setDisable(false);
                message.setText("Click on the check button below to confirm your choice");
            }
        }
        else if(currentAction == UserAction.START_PRODUCTION){

        }
        else if(currentAction == UserAction.BUY_DEVCARD){

        }
        else{
            currentAction = UserAction.MOVE_RESOURCES;
        }
    }

    private void updateWarehouseInAction(String shelf) {
        resourcesForAction.add(new ResourcePosition(Resource.valueOf(selectedResource.toUpperCase()),
                Place.WAREHOUSE, NumOfShelf.valueOf(shelf)));
        switch (shelf){
            case "ONE" -> {
                Image image = new Image("/graphics/resources/" +selectedResource.toLowerCase()+ ".png");
                first_shelf.setImage(image);}
            case "TWO" -> {
                for (ImageView i : second_shelf) {
                    if (i.getImage() == null) {
                        Image image = new Image("/graphics/resources/" + selectedResource.toLowerCase() + ".png");
                        i.setImage(image);
                        break;
                    }
                }
            }
            case "THREE" -> {
                for(ImageView i : third_shelf){
                    if(i.getImage() == null){
                        Image image = new Image("/graphics/resources/" +selectedResource.toLowerCase()+ ".png");
                        i.setImage(image);
                        break;
                    }
                }
            }
        }
        resourceToSelect--;
    }

    public void selectFirstShelf(ActionEvent actionEvent) {
        selectWarehouse("ONE");
    }

    public void selectSecondShelf(ActionEvent actionEvent) {
        selectWarehouse("TWO");
    }

    public void selectThirdShelf(ActionEvent actionEvent) {
        selectWarehouse("THREE");
    }

    private void selectMarket(boolean isRow, int n){
        resourceToSelect = 0;
        currentAction = UserAction.BUY_RESOURCES;
        handleButtons(marketButtons, true);
        handleButtons(devSpaceButtons, true);
        handleButtons(devDeckButtons, true);
        handleButtons(handButtons, true);
        handleButtons(playedCardsButtons, true);
        handleButtons(chestButtons, true);
        handleButtons(warehouseButtons, true);
        back_button.setDisable(false);
        List <String> colours;
        List <String> resToReceive = new ArrayList<>();
        if(isRow)
            colours = Arrays.stream(view.getMarket()[n])
                    .filter(s -> !s.equalsIgnoreCase("WHITE")).collect(Collectors.toList());
        else
            colours = Arrays.stream(getColumn(view.getMarket(), n))
                    .filter(s -> !s.equalsIgnoreCase("WHITE")).collect(Collectors.toList());
        for (String s : colours) {
            switch (s.toUpperCase()) {
                case "GREY" -> resToReceive.add("STONE");
                case "YELLOW" -> resToReceive.add("COIN");
                case "BLUE" -> resToReceive.add("SHIELD");
                case "PURPLE" -> resToReceive.add("SERVANT");
                case "RED" -> resToReceive.add("FAITHPOINT");
            }
        }
        for (int i = 0; i < Resource.values().length; i++) {
            Resource r = Resource.values()[i];
            int quantity = (int) resToReceive.stream().filter(s -> s.equalsIgnoreCase(r.toString())).count();
            if(quantity > 0){
                resourceToSelect += quantity;
                messageResourcesNumbers.get(i).setText(String.valueOf(quantity));
                messageResources.get(i).setImage(new Image(resourcesUrl.get(i)));
                messageResourcesButtons.get(i).setDisable(false);
            }
        }
    }

    public String[] getColumn(String[][] array, int index) {
        String[] column = new String[3];
        for (int i = 0; i < column.length; i++) {
            column[i] = array[i][index];
        }
        return column;
    }

    public void selectMarketC1(ActionEvent actionEvent) {
        selectMarket(false, 1);
        position = 1;
        selection = MarketSelection.COLUMN;
    }

    public void selectMarketC2(ActionEvent actionEvent) {
        selectMarket(false, 2);
        position = 2;
        selection = MarketSelection.COLUMN;
    }

    public void selectMarketC3(ActionEvent actionEvent) {
        selectMarket(false, 3);
        position = 3;
        selection = MarketSelection.COLUMN;
    }

    public void selectMarketC4(ActionEvent actionEvent) {
        selectMarket(false, 4);
        position = 4;
        selection = MarketSelection.COLUMN;
    }

    public void selectMarketR1(ActionEvent actionEvent) {
        selectMarket(true, 1);
        position = 1;
        selection = MarketSelection.ROW;
    }

    public void selectMarketR2(ActionEvent actionEvent) {
        selectMarket(true, 2);
        position = 2;
        selection = MarketSelection.ROW;
    }

    public void selectMarketR3(ActionEvent actionEvent) {
        selectMarket(true, 3);
        position = 3;
        selection = MarketSelection.ROW;
    }

    public void selectConfirmButton(ActionEvent actionEvent){
        if(currentAction == UserAction.BUY_RESOURCES){
            connectionSocket.send(new BuyResources(null, position, selection, resourcesForAction));
        }
    }

}
