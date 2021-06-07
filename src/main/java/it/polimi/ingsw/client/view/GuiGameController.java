package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientNetwork.ClientConnectionSocket;
import it.polimi.ingsw.messages.actions.BuyDevCard;
import it.polimi.ingsw.messages.actions.BuyResources;
import it.polimi.ingsw.messages.actions.MoveResources;
import it.polimi.ingsw.messages.clientMessages.EndTurn;
import it.polimi.ingsw.messages.clientMessages.LeaderCardSelection;
import it.polimi.ingsw.messages.clientMessages.ResourceSelection;
import it.polimi.ingsw.server.controller.GamePhase;
import it.polimi.ingsw.server.controller.Place;
import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.model.Colour;
import it.polimi.ingsw.server.model.MarketSelection;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.ResourcePosition;
import it.polimi.ingsw.server.model.gameboard.DevSpaceSlot;
import it.polimi.ingsw.server.model.gameboard.NumOfShelf;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.*;
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
    private int position = -1;
    private MarketSelection selection;
    private final List<ImageView> marbles = new ArrayList<>();
    private final List<Button> marketButtons = new ArrayList<>();

    //DevDecks
    @FXML private Button devdeck_00;
    @FXML private Button devdeck_01;
    @FXML private Button devdeck_02;
    @FXML private Button devdeck_10;
    @FXML private Button devdeck_11;
    @FXML private Button devdeck_12;
    @FXML private Button devdeck_20;
    @FXML private Button devdeck_21;
    @FXML private Button devdeck_22;
    @FXML private Button devdeck_30;
    @FXML private Button devdeck_31;
    @FXML private Button devdeck_32;
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
    private int row;
    private int column;
    private final List<String> resourceRequirements = new ArrayList<>();
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
    @FXML private ImageView trashcan;
    @FXML private Button trashcan_button;
    private final List<ImageView> second_shelf = new ArrayList<>();
    private final List<ImageView> third_shelf = new ArrayList<>();
    private final List<List<ImageView>> warehouse = new ArrayList<>();
    private int resourceToSelect;
    private String selectedResource;
    private int sourceShelf;
    private int destinationShelf;
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
    private final List<List<ImageView>> devSpace = new ArrayList<>();
    private final List<Button> devSpaceButtons = new ArrayList<>();                                                           //also contains board_production
    private int selectedSlot;

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
        disableButtons();
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
        List<ImageView> first = new ArrayList<>();
        first.add(first_shelf);
        warehouse.add(first);
        warehouse.add(second_shelf);
        warehouse.add(third_shelf);
        warehouseButtons.add(shelf1);
        warehouseButtons.add(shelf2);
        warehouseButtons.add(shelf3);
        sourceShelf = -1;
        destinationShelf = 1;
        trashcan.setImage(null);
        trashcan_button.setDisable(true);
        chestButtons.add(chest);
    }

    private void disableButtons(){
        back_button.setDisable(true);
        confirm_button.setDisable(true);
        end_turn.setDisable(true);
        right_gameboard.setDisable(true);
        left_gameboard.setDisable(true);
        handleButtons(messageResourcesButtons, true);
        handleButtons(marketButtons, true);
        handleButtons(devSpaceButtons, true);
        handleButtons(playedCardsButtons, true);
        handleButtons(handButtons, true);
        handleButtons(devDeckButtons, true);
        handleButtons(chestButtons, true);
        handleButtons(warehouseButtons, true);
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
        devDeckButtons.add(devdeck_00);
        devDeckButtons.add(devdeck_01);
        devDeckButtons.add(devdeck_02);
        devDeckButtons.add(devdeck_10);
        devDeckButtons.add(devdeck_11);
        devDeckButtons.add(devdeck_12);
        devDeckButtons.add(devdeck_20);
        devDeckButtons.add(devdeck_21);
        devDeckButtons.add(devdeck_22);
        devDeckButtons.add(devdeck_30);
        devDeckButtons.add(devdeck_31);
        devDeckButtons.add(devdeck_32);
        row = -1;                                                                                                       //these are row and columns according to the disposition of devCards in the GUI, for the view one must invert them
        column = -1;
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
        devSpace.add(firstSlot);
        devSpace.add(secondSlot);
        devSpace.add(thirdSlot);
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
        if (view.getGamePhase() != GamePhase.SETUP || (view.getGamePhase() == GamePhase.SETUP && view.getHand().size() <= 2)){
            String url;
            Image m;
            if (view.getHand().size() > 0 && currentGameboard.getOwner().equalsIgnoreCase(view.getNickname())){
                url = "/graphics/leadcards/" + view.getHand().get(0).getUrl();
                m = new Image(getClass().getResourceAsStream(url));
                first_hand.setImage(m);
            }
            else first_hand.imageProperty().set(null);
            if (view.getHand().size() > 1 && currentGameboard.getOwner().equalsIgnoreCase(view.getNickname())){
                url = "/graphics/leadcards/" + view.getHand().get(1).getUrl();
                m = new Image(getClass().getResourceAsStream(url));
                second_hand.setImage(m);
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
            List<DevCardInfo> slot1 = currentGameboard.getDevSpace().get(0);
            for (int i = 0; i < 3; i++){
                if (slot1.size() <= i) firstSlot.get(i).imageProperty().set(null);
                else {
                    String url = "/graphics/devcards/" + slot1.get(i).getUrl();
                    Image m = new Image(getClass().getResourceAsStream(url));
                    firstSlot.get(slot1.size() - 1 - i).setImage(m);
                }
            }
            List<DevCardInfo> slot2 = currentGameboard.getDevSpace().get(1);
            for (int i = 0; i < 3; i++){
                if (slot2.size() <= i) secondSlot.get(i).imageProperty().set(null);
                else {
                    String url = "/graphics/devcards/" + slot2.get(i).getUrl();
                    Image m = new Image(getClass().getResourceAsStream(url));
                    secondSlot.get(slot2.size() - 1 - i).setImage(m);
                }
            }
            List<DevCardInfo> slot3 = currentGameboard.getDevSpace().get(2);
            for (int i = 0; i < 3; i++){
                if (slot3.size() <= i) thirdSlot.get(i).imageProperty().set(null);
                else {
                    String url = "/graphics/devcards/" + slot3.get(i).getUrl();
                    Image m = new Image(getClass().getResourceAsStream(url));
                    thirdSlot.get(slot3.size() - 1 - i).setImage(m);
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
                blackcross.setLayoutX(loc[0]+7);
                blackcross.setLayoutY(loc[1]+7);
            }
            cross.setLayoutX(coordinates.get(currentGameboard.getPosition())[0]);
            cross.setLayoutY(coordinates.get(currentGameboard.getPosition())[1]);
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

    private void updateGUI(String owner){
        updateDevDecks();
        updateMarket();
        updateChest(owner);
        updateWarehouse(owner);
        updateDevSpace(owner);
        updateItinerary(owner);
        updateHandCards();
        updatePlayedCards(owner);
        for (int i = 0; i < 4; i++){
            messageResources.get(i).setImage(null);
            messageResourcesButtons.get(i).setDisable(true);
            messageResourcesNumbers.get(i).setText("");
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

    private void checkDevDeckButtons(){
        for (int j = 0; j < 4; j++){
            for (int i = 0; i < 3; i++) {
                if (view.getDevDecks()[i][j] != null) {
                    devDeckButtons.get(j * 3 + i).setDisable(
                            !view.getOwnGameBoard().totalResourceCheck(view.getDevDecks()[i][j].getResourceRequirements()));
                }
                else
                    devDeckButtons.get(j * 3 + i).setDisable(true);
            }
        }
    }

    private void checkHandButtons(){
        for(int i = 0; i < handButtons.size(); i++) {
            if (view.getHand().get(i) != null) {
                if (view.getHand().get(i).getResourceRequirements() != null) {
                    handButtons.get(i).setDisable(
                            !view.getOwnGameBoard().totalResourceCheck(view.getHand().get(i).getResourceRequirements()));
                } else
                    handButtons.get(i).setDisable(!view.getOwnGameBoard().devCardsCheck(view.getHand().get(i).getCardRequirements()));
            } else
                handButtons.get(i).setDisable(true);
        }
    }

    public void enableAction(String s) {
        updateGUI(view.getNickname());
        message.setText(s);
        handleButtons(marketButtons, false);
        handleButtons(devSpaceButtons, false);
        back_button.setDisable(true);
        confirm_button.setDisable(true);
        end_turn.setDisable(false);
        for(int i = 0; i < 3; i++){
            devSpaceButtons.get(i).setDisable(view.getOwnGameBoard().getDevSpace().get(i).isEmpty());
        }
        checkHandButtons();
        checkDevDeckButtons();
        handleButtons(playedCardsButtons, false);
        for(int i = 0; i < 2; i++){
            playedCardsButtons.get(i).setDisable(view.getOwnGameBoard().getPlayedCards().size() <= i);
        }
        checkWarehouseButtons(false);
        handleButtons(chestButtons, true);
        currentAction = null;
        position = -1;
        resourcesForAction.clear();
        selectedResource = null;
        sourceShelf = -1;
        destinationShelf = -1;
        row = -1;
        column = -1;
        resourceRequirements.clear();
        selectedSlot = -1;
        trashcan.setImage(null);
        trashcan_button.setDisable(true);
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
        Integer index = view.getOwnGameBoard().getIndex();
        if(index > 0) {
            String s;
            s = "You are player number " +(index+1)+ ", this gives you access to\n";
            switch (index){
                case 1 -> {s = s + "an additional resource, please select one:";
                    resourceToSelect = 1;}
                case 2 -> {s = s +"one faithpoint and an additional resource, please select one:";
                    resourceToSelect = 1;}
                case 3 -> {s = s +"one faithpoint and two additional resources, please select them:";
                    resourceToSelect = 2;}
                default -> s = s + "";
            }
            message.setText(s);
        }
    }

    /*used when some resources are needed*/
    private void selectResourcesPosition(boolean toStore){
        checkWarehouseButtons(toStore);
        handleButtons(chestButtons, true);
        handleButtons(devSpaceButtons, true);
        handleButtons(devDeckButtons, true);
        handleButtons(marketButtons, true);
        handleButtons(handButtons, true);
        handleButtons(playedCardsButtons, true);
        right_gameboard.setDisable(true);
        left_gameboard.setDisable(true);
        handleButtons(messageResourcesButtons, true);
        if(currentAction == UserAction.BUY_RESOURCES){
            message.setText("Now click on one of the warehouse shelves or on the trashcan");
            trashcan.setImage(new Image("/graphics/trashcan.png"));
            trashcan_button.setDisable(false);
        }
        else
            message.setText("Now click on one of the warehouse shelves");
    }

    public void select_coin() {
        selectedResource = "Coin";
        selectResourcesPosition(true );
    }

    public void select_stone() {
        selectedResource = "Stone";
        selectResourcesPosition(true);
    }

    public void select_servant() {
        selectedResource = "Servant";
        selectResourcesPosition(true );
    }

    public void select_shield() {
        selectedResource = "Shield";
        selectResourcesPosition(true );
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
            handleButtons(warehouseButtons, true);
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
            else message.setText("Select the next resource to place");
            handleButtons(warehouseButtons, true);
            enableMessageResourceButtons();
            trashcan_button.setDisable(true);
            trashcan.setImage(null);
        }
        else if(currentAction == UserAction.START_PRODUCTION){

        }
        else if(currentAction == UserAction.BUY_DEVCARD) {
            String resStored = view.getOwnGameBoard().getWarehouse().get(NumOfShelf.valueOf(shelf).ordinal()).get(0);
            int quantity = (int) warehouse.get(NumOfShelf.valueOf(shelf).ordinal()).stream().filter(i -> i.getImage() != null).count();
            for (String s : resourceRequirements) {
                if (resStored.equalsIgnoreCase(s)) {
                    resourcesForAction.add(new ResourcePosition(Resource.valueOf(s.toUpperCase()),
                            Place.WAREHOUSE, NumOfShelf.valueOf(shelf.toUpperCase())));
                    resourceRequirements.remove(s);
                    warehouse.get(NumOfShelf.valueOf(shelf).ordinal()).get(quantity - 1).setImage(null);
                    String oldQuantity = messageResourcesNumbers.get(Resource.valueOf(s.toUpperCase()).ordinal())
                            .getText().substring(0, 1);
                    int newQuantity = Integer.parseInt(oldQuantity) + 1;
                    String max = String.valueOf(messageResourcesNumbers.get(Resource.valueOf(s.toUpperCase()).ordinal())
                            .getText().charAt(2));
                    messageResourcesNumbers.get(Resource.valueOf(s.toUpperCase()).ordinal()).setText(newQuantity + "/" +max);
                    break;
                }
            }
            checkWarehouseButtons(false);
            if(resourceRequirements.isEmpty()){
                confirm_button.setDisable(false);
                message.setText("Click on the check button below to confirm your choice");
                handleButtons(warehouseButtons, true);
                handleButtons(chestButtons, true);
            }
        }
        else if(currentAction == UserAction.MOVE_RESOURCES){
            int srcQuantity = view.getOwnGameBoard().getWarehouse().get(sourceShelf).size();
            int destShelf = NumOfShelf.valueOf(shelf).ordinal();
            int destQuantity = view.getOwnGameBoard().getWarehouse().get(destShelf).size();
            int destDim = destShelf + 1;
            if (srcQuantity > destDim - destQuantity)
                enableAction("There is not enough space to move the selected resources in this shelf\nPlease choose an action");
            else {
                int j = 0;
                for (int i = destQuantity; i < warehouse.get(destShelf).size() && srcQuantity > 0; i++, j++, srcQuantity--) {
                    ImageView destIm = warehouse.get(destShelf).get(i);
                    ImageView srcIm = warehouse.get(sourceShelf).get(j);
                    destIm.setImage(new Image("/graphics/resources/"
                                + view.getOwnGameBoard().getWarehouse().get(sourceShelf).get(0).toLowerCase() + ".png"));
                    srcIm.setImage(null);
                    }
                destinationShelf = NumOfShelf.valueOf(shelf).ordinal();
                confirm_button.setDisable(false);
                handleButtons(warehouseButtons, true);
                message.setText("Click on the check button below to confirm your choice");
            }
        }
        else{                                                                                                           //when we will implement depots, if shelf > 3 we must ask for the number of resources the user wants to move
            currentAction = UserAction.MOVE_RESOURCES;
            sourceShelf = NumOfShelf.valueOf(shelf).ordinal();
            checkWarehouseButtons(true);
            warehouseButtons.get(NumOfShelf.valueOf(shelf).ordinal()).setDisable(true);
            handleButtons(marketButtons, true);
            handleButtons(devSpaceButtons, true);
            handleButtons(devDeckButtons, true);
            handleButtons(handButtons, true);
            handleButtons(playedCardsButtons, true);
            handleButtons(chestButtons, true);
            handleButtons(messageResourcesButtons, true);
            back_button.setDisable(false);
            message.setText("You have chosen to move resources, select the shelf\nwhere you want to move the resources");
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

    public void selectFirstShelf() {
        selectWarehouse("ONE");
    }

    public void selectSecondShelf() {
        selectWarehouse("TWO");
    }

    public void selectThirdShelf() {
        selectWarehouse("THREE");
    }

    private void selectMarket(boolean isRow, int n){
        message.setText("You have chosen to buy resources from the market," +
                " please place\n the gained resources in the warehouse or in the trashcan");
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
        for (int i = 0; i < Resource.values().length - 2; i++) {
            Resource r = Resource.values()[i];
            int quantity = (int) resToReceive.stream().filter(s -> s.equalsIgnoreCase(r.toString())).count();
            if(quantity > 0){
                resourceToSelect += quantity;
                messageResourcesNumbers.get(i).setText(String.valueOf(quantity));
                messageResources.get(i).setImage(new Image(resourcesUrl.get(i)));
                messageResourcesButtons.get(i).setDisable(false);
            }
        }
        if (colours.stream().anyMatch(s -> s.equalsIgnoreCase("red")))
            resourcesForAction.add(new ResourcePosition(Resource.FAITHPOINT, null, null));
        if(colours.size() == 1 && colours.get(0).equalsIgnoreCase("red"))
            confirm_button.setDisable(false);
    }

    private void enableMessageResourceButtons(){
        for (int i = 0; i < Resource.values().length - 2; i++)
            messageResourcesButtons.get(i).setDisable(messageResourcesNumbers.get(i).getText().isEmpty());
    }

    public String[] getColumn(String[][] array, int index) {
        String[] column = new String[3];
        for (int i = 0; i < column.length; i++) {
            column[i] = array[i][index];
        }
        return column;
    }

    public void selectMarketC1() {
        selectMarket(false, 0);
        position = 1;
        selection = MarketSelection.COLUMN;
    }

    public void selectMarketC2() {
        selectMarket(false, 1);
        position = 2;
        selection = MarketSelection.COLUMN;
    }

    public void selectMarketC3() {
        selectMarket(false, 2);
        position = 3;
        selection = MarketSelection.COLUMN;
    }

    public void selectMarketC4() {
        selectMarket(false, 3);
        position = 4;
        selection = MarketSelection.COLUMN;
    }

    public void selectMarketR1() {
        selectMarket(true, 0);
        position = 1;
        selection = MarketSelection.ROW;
    }

    public void selectMarketR2() {
        selectMarket(true, 1);
        position = 2;
        selection = MarketSelection.ROW;
    }

    public void selectMarketR3() {
        selectMarket(true, 2);
        position = 3;
        selection = MarketSelection.ROW;
    }

    public void selectConfirmButton(){
        if(currentAction == UserAction.BUY_RESOURCES){
            connectionSocket.send(new BuyResources(new ArrayList<>(), position, selection, resourcesForAction));
        }
        if(currentAction == UserAction.MOVE_RESOURCES){
            connectionSocket.send(new MoveResources(NumOfShelf.values()[sourceShelf],
                    NumOfShelf.values()[destinationShelf], view.getOwnGameBoard().getWarehouse().get(sourceShelf).size()));
        }
        if(currentAction == UserAction.BUY_DEVCARD){
            DevCardInfo d = view.getDevDecks()[column][row];
            connectionSocket.send(new BuyDevCard(d.getLevel(), Colour.valueOf(d.getColour().toUpperCase()),
                    DevSpaceSlot.values()[selectedSlot], resourcesForAction, new ArrayList<>()));
        }
    }

    public void quitAction() {
        enableAction("Please choose an action.");
    }

    public void endTurn(){
        disableButtons();
        connectionSocket.send(new EndTurn());
    }

    private void selectDevDeck(int i, int j){
        currentAction = UserAction.BUY_DEVCARD;
        message.setText("You have chosen to buy a development card, please select\nthe slot where you want to place it");
        row = i;
        column = j;
        DevCardInfo d = view.getDevDecks()[column][row];
        resourceRequirements.addAll(d.getResourceRequirements());
        handleButtons(warehouseButtons, true);
        handleButtons(chestButtons, true);
        handleButtons(messageResourcesButtons,  true);
        handleButtons(playedCardsButtons, true);
        handleButtons(handButtons, true);
        handleButtons(marketButtons, true);
        handleButtons(devDeckButtons, true);
        handleButtons(devSpaceButtons, false);
        board_production.setDisable(true);
        back_button.setDisable(false);
    }

    public void selectDev30() {
        selectDevDeck(3, 0);
    }

    public void selectDev31() {
        selectDevDeck(3, 1);
    }

    public void selectDev32() {
        selectDevDeck(3, 2);
    }

    public void selectDev20() {
        selectDevDeck(2, 0);
    }

    public void selectDev21() {
        selectDevDeck(2, 1);
    }

    public void selectDev22() {
        selectDevDeck(2, 2);
    }

    public void selectDev12() {
        selectDevDeck(1, 2);
    }

    public void selectDev11() {
        selectDevDeck(1, 1);
    }

    public void selectDev10() {
        selectDevDeck(1, 0);
    }

    public void selectDev00() {
        selectDevDeck(0, 0);
    }

    public void selectDev01() {
        selectDevDeck(0, 1);
    }

    public void selectDev02() {
        selectDevDeck(0, 2);
    }

    private void selectDevSpace(int slot){
        if(currentAction == UserAction.BUY_DEVCARD){
            DevCardInfo d = view.getDevDecks()[column][row];
            selectedSlot = slot;
            if(!((view.getOwnGameBoard().getDevSpace().get(slot).isEmpty() && d.getLevel() == 1) ||
                    (!view.getOwnGameBoard().getDevSpace().get(slot).isEmpty() &&
                            view.getOwnGameBoard().getDevSpace().get(slot).get(0).getLevel() == d.getLevel() - 1))){
                enableAction("This slot cannot host the selected development card\nPlease choose an action");
            }
            else{
                devSpace.get(slot).get(view.getOwnGameBoard().getDevSpace().get(slot).size()).
                        setImage(devDecks.get(row + column * 4).getImage());
                devDecks.get(row + column * 4).setImage(null);
                handleButtons(devSpaceButtons, true);
                checkWarehouseButtons(false);
                chest.setDisable(false);
                for(String s : d.getResourceRequirements()){
                    messageResources.get(Resource.valueOf(s.toUpperCase()).ordinal()).setImage
                            (new Image("/graphics/resources/" +s.toLowerCase()+ ".png"));
                    messageResourcesNumbers.get(Resource.valueOf(s.toUpperCase()).ordinal()).setText("0/"
                            +d.getResourceRequirements().stream().filter(c -> c.equalsIgnoreCase(s)).count());
                }
                message.setText("You have selected slot number " +(slot+1)+ ", now click on the warehouse or chest" +
                        "\nto select where you want to take the needed resources from");
            }
        }
    }


    public void selectSlot1() {
        selectDevSpace(0);
    }

    public void selectSlot2() {
        selectDevSpace(1);
    }

    public void selectSlot3() {
        selectDevSpace(2);
    }

    public void selectTrashcan() {
        if(currentAction == UserAction.BUY_RESOURCES){
            resourcesForAction.add(new ResourcePosition(Resource.valueOf(selectedResource.toUpperCase()),
                    Place.TRASH_CAN, null));
            int newQuantity = Integer.parseInt(messageResourcesNumbers.
                    get(Resource.valueOf(selectedResource.toUpperCase()).ordinal()).getText()) - 1;
            resourceToSelect--;
            if(newQuantity == 0) {
                messageResourcesButtons.get(Resource.valueOf(selectedResource.toUpperCase()).ordinal()).setDisable(true);
                messageResourcesNumbers.get(Resource.valueOf(selectedResource.toUpperCase()).ordinal()).setText("");
                messageResources.get(Resource.valueOf(selectedResource.toUpperCase()).ordinal()).setImage(null);
                if (resourceToSelect == 0) {
                    confirm_button.setDisable(false);
                    message.setText("Click on the check button below to confirm your choice");
                }
            }
            else
                messageResourcesNumbers.get(Resource.valueOf(selectedResource.toUpperCase()).ordinal()).
                        setText(String.valueOf(newQuantity));
            message.setText("Select the next resource to place");
            trashcan_button.setDisable(true);
            enableMessageResourceButtons();
        }
    }

}
