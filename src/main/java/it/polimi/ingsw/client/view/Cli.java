package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientNetwork.ClientConnectionSocket;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.messages.clientMessages.JoinLobby;
import it.polimi.ingsw.client.clientNetwork.MessageHandler;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.clientMessages.LeaderCardSelection;
import it.polimi.ingsw.messages.clientMessages.ResourceSelection;
import it.polimi.ingsw.messages.clientMessages.SetPlayersNumber;
import it.polimi.ingsw.messages.clientMessages.internal.*;
import it.polimi.ingsw.server.controller.Place;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.ResourcePosition;
import it.polimi.ingsw.server.model.ResourceQuantity;
import it.polimi.ingsw.server.model.gameboard.NumOfShelf;
import it.polimi.ingsw.server.observer.Observer;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

/**
 * Class Cli it's the main class which manages the game if the player decides to
 * play with the command line interface.
 *
 */
public class Cli implements Observer {
    private final PrintStream output;
    private final Scanner input;
    private final ClientView clientView;
    private final MessageHandler messageHandler;
    private boolean active;
    private final ActionFactory actionFactory;
    private ClientConnectionSocket connectionSocket;

    /**
     * Creates a new Cli instance.
     *
     */
    public Cli() {
        input = new Scanner(System.in);
        output = new PrintStream(System.out);
        clientView = new ClientView(this);
        messageHandler = new MessageHandler(clientView);
        active = true;
        actionFactory = new ActionFactory(output, input, this);
    }

    /**
     * Asks for the server ip address and port, then it creates a new Cli object and calls
     * the {@link #setup()} method on it.
     *
     * @param args the main args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insert the server IP address\n>");
        String ip = scanner.nextLine();
        System.out.print("Insert the server port\n>");
        try {
            int port = scanner.nextInt();
            Constants.setAddress(ip);
            Constants.setPort(port);
            Cli cli = new Cli();
            cli.setup();
        }
        catch (InputMismatchException e){
            System.out.println("Numeric format requested for the port. Application will now close.");
            System.exit(0);
        }
    }

    /**
     * Returns the active attribute.
     *
     * @return the active attribute
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets up the connection with the server, asking if the player wants to join a new game
     * or reconnect to a previous one. Tries to register the player with a provided nickname
     * and start a thread that will contain the ClientConnectionSocket object used to send and
     * receive messages from the server.
     *
     */
    private void setup() {
        connectionSocket = new ClientConnectionSocket(this, messageHandler);
        try {
            while (!connectionSocket.setupConnection()) {
                output.println("The entered IP/port doesn't match any active server. Please try again!");
                output.print("Insert the server IP address\n>");
                Constants.setAddress(readInputString());
                output.print("Insert the server port\n>");
                Constants.setPort(readInputInt(false));
            }
            System.out.println("Socket Connection setup completed!");
        } catch (IOException e) {
            System.err.println("Error during socket configuration! Application will now close.");
            System.exit(0);
        }
        String nickname = null;
        boolean confirmation = false;
        boolean newPlayer = true;
        while (!confirmation) {
            boolean ok = false;
            while (!ok) {
                output.print("Would you like to start a new match or resume an existing one? [start/resume]\n>");
                String answer = readInputString().toUpperCase();
                switch (answer) {
                    case "START" -> {
                        newPlayer = true;
                        ok = true;
                    }
                    case "RESUME" -> {
                        newPlayer = false;
                        ok = true;
                    }
                    default -> output.println("Please select one of the two options.");
                }
            }
            output.print("Insert your nickname:\n>");
            nickname = readInputString();
            if (nickname != null && !nickname.isEmpty() && connectionSocket.setupNickname(nickname, newPlayer))
                confirmation = true;
        }
        clientView.setNickname(nickname);
        clientView.getOwnGameBoard().setOwner(nickname);
        Thread thread = new Thread(connectionSocket);
        thread.start();
    }

    /**
     * Acts based on the {@link ViewMessage} received, asking different things to player,
     * printing updated view elements and sending different messages to the server.
     *
     * @param message the ViewMessage received
     */
    @Override
    public void update(Message message) {
        ViewMessage m = (ViewMessage) message;
        boolean request = true;
        if (m instanceof DisplayMessage) {
            output.println(m.getMessage());
        } else if (m instanceof RequestPlayersNumber) {
            RequestPlayersNumber r = (RequestPlayersNumber) m;
            if(r.getInfoLobbies().isEmpty())
                output.println(r.getMessage()+ " Please type start to begin: [start]");
            else
                output.println(r.getMessage());
            for(int i = 0; i < r.getInfoLobbies().size(); i++){
                output.println((i+1) + ". " +r.getInfoLobbies().get(i));
            }
            if(!r.getInfoLobbies().isEmpty())
                output.println("Please type start if you want to create a new lobby, " +
                        "or join if you want to join an existing one: [start/join]");
            output.print(">");
            String answer = null;
            while (request) {
                answer = readInputString();
                if (answer.equalsIgnoreCase("join")) {
                    if(r.getInfoLobbies().isEmpty())
                        output.print("There is no lobby available at the moment, please type start.\n>");
                    else {
                        output.print("Type the number of the lobby you want to join: \n>");
                        boolean lobbySel = true;
                        int l = 0;
                        while (lobbySel) {
                            l = readInputInt(false);
                            if (l >= 1 && l <= r.getInfoLobbies().size()) lobbySel = false;
                            else output.print("Please choose a number between 1 and " + r.getInfoLobbies().size() + ":\n>");
                        }
                        connectionSocket.send(new JoinLobby(r.getOwners().get(l - 1)));
                        request = false;
                    }
                } else if (answer.equalsIgnoreCase("start")) {
                    Integer number = 0;
                    output.print("Please choose a number of players : [1/2/3/4]\n>");
                    while (request) {
                        number = readInputInt(false);
                        if (number >= 1 && number <= 4) request = false;
                        else output.print("Please choose a number between 1 and 4:\n>");
                    }
                    connectionSocket.send(new SetPlayersNumber(number));
                    request = false;
                } else {
                    if (r.getInfoLobbies().isEmpty())
                        output.print("Invalid input. There is no lobby available at the moment, please type start.\n>");
                    else
                        output.print("Invalid input. Please select a valid option: [start/join]\n>");
                }
            }
        } else if (m instanceof SetupDiscard) {
            output.print(m.getMessage());
            int index1 = 0;
            int index2 = 0;
            while (request) {
                index1 = readInputInt(false);
                output.print(">");
                index2 = readInputInt(false);
                if (index1 < 1 || index1 > 4 || index2 < 1 || index2 > 4 || index1 == index2)
                    output.print("Please choose two distinct numbers between 1 and 4:\n>");
                else request = false;
            }
            connectionSocket.send(new LeaderCardSelection(new int[]{index1, index2}));
        } else if (m instanceof SetupResources) {
            output.print(m.getMessage());
            List<ResourcePosition> rp;
            List<String> s = new ArrayList<>();
            if (clientView.getOwnGameBoard().getIndex() == 1 || clientView.getOwnGameBoard().getIndex() == 2) {
                Resource r = askForResource();
                s.add(r.toString());
            } else if (clientView.getOwnGameBoard().getIndex() == 3) {
                for (int i = 0; i < 2; i++) {
                    Resource r = askForResource();
                    if(i == 0)
                        output.print(">");
                    s.add(r.toString());
                }
            }
            rp = (askForLocation(s, true, false, true));
            connectionSocket.send(new ResourceSelection(rp));
        } else if (m instanceof ChooseAction) {
            output.print(m.getMessage()+ " (select a number between 0 and 11):\n" +
                                            Constants.getChooseAction() + "\n>");
            Integer n;
            while (request) {
                Message toSend;
                n = readInputInt(false);
                if (n < 0 || n > 11) {
                    output.print("Please choose a number between 0 and 11:\n>");
                } else if (n > 5 && n < 11) {
                    showElements(n);
                } else {
                    if ((n == 3 || n == 4) && getClientView().getHand().size() == 0) {
                        output.print("You don't have any leader card in your hand, " +
                                "please select a different action.\n>");
                    } else{
                        toSend = actionFactory.createAction(n);
                        if(toSend == null)
                            output.print(Constants.getChooseAction()+ "\n>");
                        else{
                            request = false;
                            connectionSocket.send(toSend);
                        }
                    }
                }
            }

        } else if (m instanceof PrintChest) {
            printChest(printGameBoardElem(m.getMessage()));
        } else if (m instanceof PrintDevDecks){
            printDevDecks();
        } else if (m instanceof PrintDevSpace){
            printDevSpace(printGameBoardElem(m.getMessage()));
        } else if (m instanceof PrintHandCards){
            printHandCards();
        } else if (m instanceof PrintItinerary){
            printItinerary(printGameBoardElem(m.getMessage()));
        } else if (m instanceof PrintMarket){
            printMarket();
        } else if (m instanceof PrintPlayedCards){
            printPlayedLeadCards(printGameBoardElem(m.getMessage()));
        } else if (m instanceof PrintWarehouse){
            printWarehouse(printGameBoardElem(m.getMessage()));
        } else if (m instanceof NewView){
            printMarket();
            printDevDecks();
        } else if (m instanceof PrintEndGame){
            output.println(m.getMessage());
            output.print("Please press any key to close the game: \n>");
            input.next();
        }
    }

    /**
     * Used when printing an updated game board element. Prints the nickname of the owner of the
     * element and returns the owner of the game board.
     *
     * @param owner the owner of the game board
     * @return the game board of the specified player, {@code null} if no game board is found
     */
    private GameBoardInfo printGameBoardElem(String owner){
        if (owner.equals(clientView.getNickname())){
            output.print("YOUR ");
            return clientView.getOwnGameBoard();
        }
        else {
            output.print(owner.toUpperCase() + "'S ");
            for (GameBoardInfo g : getClientView().getOtherGameBoards()){
                if (g.getOwner().equalsIgnoreCase(owner)){
                    return g;
                }
            }
        }
        return null;
    }

    /**
     * Prints some view elements based on the input provided by the player.
     *
     * @param n the number chosen by the player
     */
    private void showElements(int n) {
        switch (n) {
            case 6 -> printGameBoard(clientView.getOwnGameBoard());
            case 7 -> askForGameBoard();
            case 8 -> printMarket();
            case 9 -> printDevDecks();
            case 10 -> printHandCards();
        }
        output.print("Please choose an action (select a number between 0 and 11):\n" +
                Constants.getChooseAction() + "\n>");
    }

    /**
     * Asks the player whose game board he wants to see printed in the terminal and prints it. If the player is
     * in a single player game it prints Lorenzo de Medici's position on the itinerary.
     *
     */
    private void askForGameBoard() {
        if (clientView.getOwnGameBoard().getBlackCrossPosition() != null)
            output.println("Lorenzo De Medici's position: " + clientView.getOwnGameBoard().getBlackCrossPosition() + "/24\n");
        else {
            while (true) {
                StringBuilder names = new StringBuilder();
                for (GameBoardInfo gbi : clientView.getOtherGameBoards()){
                    if (clientView.getOtherGameBoards().indexOf(gbi) != 0) names.append("/");
                    names.append(gbi.getOwner().toUpperCase());
                }
                output.print("Whose game board would you like to view? [" + names + "]\n>");
                String s = readInputString().toUpperCase();
                if(s.equalsIgnoreCase("back")) return;
                for (GameBoardInfo g : clientView.getOtherGameBoards()) {
                    if (g.getOwner().equalsIgnoreCase(s)) {
                        printGameBoard(g);
                        return;
                    }
                }
                output.print("Please select the nickname of a player in the match. ");
            }
        }
    }

    /**
     * Asks the player for a resource.
     *
     * @return the resource chosen by the player
     */
    private Resource askForResource() {
        boolean request = true;
        Resource r = null;
        while (request) {
            String s = readInputString().toUpperCase();
            try {
                r = Resource.valueOf(s);
                request = false;
            } catch (IllegalArgumentException e) {
                output.print("Please select a valid resource:\n>");
            }
        }
        return r;
    }

    /**
     * Used when the player has to store resources or when he has to expend them. Returns the
     * list of ResourcePosition objects needed to perform the action.
     *
     * @param stringList     the list of strings which represents the resources that need to be stored or expended
     * @param deposit        {@code true} if the resources are meant to be stored, {@code false} otherwise
     * @param canDiscard     {@code true} if the player can discard the resources, {@code false} otherwise
     * @param setupResources {@code true} when selecting resources in the setup phase, {@code false} otherwise
     * @return the list of ResourcePosition objects needed to perform the action
     */
    protected List<ResourcePosition> askForLocation(List<String> stringList, boolean deposit, boolean canDiscard, boolean setupResources) {
        List<ResourceQuantity> req = new ArrayList<>();
        List<ResourcePosition> result = new ArrayList<>();
        for (Resource r : Resource.values())
            req.add(new ResourceQuantity((int) stringList.
                    stream().filter(s -> s.equalsIgnoreCase(r.toString())).count(), r));
        String order = "";
        Place place;
        NumOfShelf shelf;
        for (ResourceQuantity r : req) {
            for (int i = 0; i < r.getQuantity(); i++) {
                if (r.getResource() != Resource.EMPTY && r.getResource() != Resource.FAITHPOINT) {
                    switch (i) {
                        case 0 -> order = "first ";
                        case 1 -> order = "second ";
                        case 2 -> order = "third ";
                        case 3 -> order = "fourth ";
                        case 4 -> order = "fifth ";
                    }
                    while (true) {
                        String s;
                        if (!deposit)
                            output.print("Where would you like to take your " + order +
                                    r.getResource().toString().toLowerCase() + " from? [Warehouse/Chest]\n>");
                        else {
                            if(!setupResources)
                                output.print("Where would you like to store your " + order +
                                        r.getResource().toString().toLowerCase() + " in? [Warehouse/Discard]\n>");
                            else
                                output.print("Where would you like to store your " + order +
                                        r.getResource().toString().toLowerCase() + " in? [Warehouse]\n>");
                        }
                        s = readInputString().toUpperCase();
                        if(s.equalsIgnoreCase("back") && !setupResources) return null;
                        if (s.equalsIgnoreCase("DISCARD"))
                            s = "TRASH_CAN";
                        try {
                            place = Place.valueOf(s);
                            if (place == Place.WAREHOUSE) {
                                Integer loc = inWarehouse(r.getResource().toString());
                                if (deposit){
                                    if (clientView.getOwnGameBoard().getWarehouse().size() > 3) loc = null;
                                    if (loc == null){
                                        if(clientView.getOwnGameBoard().getWarehouse().size() == 3)
                                            output.print("Which shelf would you like to store it in? " +
                                                    "[1/2/3]\n>");
                                        else if(clientView.getOwnGameBoard().getWarehouse().size() == 4)
                                            output.print("Which shelf would you like to store it in? " +
                                                    "[1/2/3/4] (4 is the first depot)\n>");
                                        else
                                            output.print("Which shelf would you like to store it in? " +
                                                    "[1/2/3/4/5] (4, 5 are the depots)\n>");
                                        while (true) {
                                            Integer n;
                                            if(setupResources)
                                                n = readInputInt(false);
                                            else
                                                n = readInputInt(true);
                                            if(n == null) return null;
                                            int size = getClientView().getOwnGameBoard().getWarehouse().size();
                                            if (n < 1 || n > size)
                                                output.print("Please select a number between 1 and " + size + " :\n>");
                                            else {
                                                shelf = NumOfShelf.values()[n - 1];
                                                break;
                                            }
                                        }
                                    }
                                    else shelf = NumOfShelf.values()[loc];
                                    result.add(new ResourcePosition(r.getResource(), place, shelf));
                                    break;
                                } else {
                                    if (loc == null){
                                        output.print("You don't have this resource in the warehouse, please try again:\n");
                                    }
                                    else {
                                        shelf = NumOfShelf.values()[loc];
                                        result.add(new ResourcePosition(r.getResource(), place, shelf));
                                        break;
                                    }
                                }
                            } else if (place == Place.TRASH_CAN) {
                                if (!deposit)
                                    output.print("You cannot take resources that have been discarded.\n");
                                else {
                                    if (!canDiscard)
                                        output.print("You cannot discard this resource.\n");
                                    else {
                                        result.add(new ResourcePosition(r.getResource(), place, null));
                                        break;
                                    }
                                }
                            } else if (place == Place.CHEST) {
                                if (deposit)
                                    output.print("This resource cannot be stored in the chest.\n");
                                else{
                                    result.add(new ResourcePosition(r.getResource(), place, null));
                                    break;
                                }
                            }
                        } catch (IllegalArgumentException e) {
                            output.print("Please select a valid source. ");
                        }
                    }
                } else result.add(new ResourcePosition(r.getResource(), null, null));
            }
        }
        return result;
    }

    /**
     * Returns a integer which represents the warehouse shelf that contains a
     * specified resource.
     *
     * @param res the resource
     * @return the integer representing the warehouse shelf that contains the resource, {@code null} if no shelf
     * contains that resource
     */
    private Integer inWarehouse (String res){
        for (int i = 0; i < 3; i++){
            List<String> l = clientView.getOwnGameBoard().getWarehouse().get(i);
            if (l != null && !l.isEmpty() && l.get(0).equalsIgnoreCase(res)) return i;
        }
        return null;
    }

    /**
     * Prints the market disposition and the external marble.
     *
     */
    private void printMarket() {
        String[][] market = clientView.getMarket();
        output.print("MARKET: \n\n");
        output.println("EXTERNAL MARBLE = " + clientView.getExternalMarble() + "\n");
        for (int n = 0; n < 3; n++) {
            for (int i = 0; i < 36; i++)
                output.print("*");
            output.print("*\n");
            for (int i = 0; i < 4; i++)
                output.print("*        ");
            output.print("*\n");
            for (int i = 0; i < 4; i++) {
                output.print("* ");
                output.print(market[n][i]);
                for (int j = 0; j < 7 - market[n][i].length(); j++)
                    output.print(" ");
            }
            output.print("*\n");
            for (int i = 0; i < 4; i++)
                output.print("*        ");
            output.print("*\n");
        }
        for (int i = 0; i < 36; i++)
            output.print("*");
        output.print("*\n\n");
    }

    /**
     * Prints the development decks.
     *
     */
    private void printDevDecks() {
        output.println("DEVELOPMENT DECKS:\n");
        for (DevCardInfo[] a : clientView.getDevDecks()) {
            for (int i = 0; i < 4; i++)
                output.print(Constants.devCardBorder + " ");
            output.print("\n");
            for (int i = 0; i < 4; i++) {
                if (a[i] == null)
                    output.print(Constants.emptyDevCardBorder);
                else
                    printCardElement("* COL: " + a[i].getColour(), true);
            }
            output.print("\n");
            for (int i = 0; i < 4; i++) {
                if (a[i] == null)
                    output.print(Constants.emptyDevCardBorder);
                else
                    printCardElement("* LVL: " + a[i].getLevel(), true);
            }
            output.print("\n");
            for (int i = 0; i < 4; i++) {
                if (a[i] == null)
                    output.print(Constants.emptyDevCardBorder);
                else
                    printCardElement("* VP:  " + a[i].getVictoryPoints(), true);
            }
            output.print("\n");
            for (int i = 0; i < 4; i++) {
                if (a[i] == null)
                    output.print(Constants.emptyDevCardBorder);
                else
                    printCardElement("* REQ: " + buildResourceString(a[i].getResourceRequirements()), true);
            }
            output.print("\n");
            for (int i = 0; i < 4; i++) {
                if (a[i] == null)
                    output.print(Constants.emptyDevCardBorder);
                else
                    printCardElement("* IN:  " + buildResourceString(a[i].getProductionInput()), true);
            }
            output.print("\n");
            for (int i = 0; i < 4; i++) {
                if (a[i] == null)
                    output.print(Constants.emptyDevCardBorder);
                else
                    printCardElement("* OUT: " + buildResourceString(a[i].getProductionOutput()), true);
            }
            output.print("\n");
            for (int i = 0; i < 4; i++)
                output.print(Constants.devCardBorder + " ");
            output.print("\n\n");
        }
    }

    /**
     * Prints an attribute of a card, used when printing cards in the terminal.
     *
     * @param s         the attribute to print
     * @param isDevCard {@code true} if the card is a development card, {@code false} if the card is a leader card
     */
    private void printCardElement(String s, boolean isDevCard) {
        output.print(s);
        if (isDevCard) {
            for (int i = 0; i < 34 - s.length(); i++)
                output.print(" ");
        } else {
            for (int i = 0; i < 38 - s.length(); i++)
                output.print(" ");
        }
        output.print("* ");
    }

    /**
     * Returns a string which represents a list of resources that can be either
     * the input, output or the requirements of a card.
     *
     * @param list the list of resources
     * @return the built string
     */
    protected String buildResourceString(List<String> list) {
        int[] quantity = {0, 0, 0, 0, 0};
        StringBuilder result = new StringBuilder();
        for (String s : list) {
            switch (s.toUpperCase()) {
                case "COIN" -> quantity[0]++;
                case "STONE" -> quantity[1]++;
                case "SERVANT" -> quantity[2]++;
                case "SHIELD" -> quantity[3]++;
                case "FAITHPOINT" -> quantity[4]++;
            }
        }
        int firstNotNull = 0;
        for (int n = 0; n < 5; n++) {
            String resType = switch (n) {
                case 0 -> " CO";
                case 1 -> " ST";
                case 2 -> " SE";
                case 3 -> " SH";
                case 4 -> " FP";
                default -> "";
            };
            if (quantity[n] != 0) {
                if (n != firstNotNull) {
                    result.append(" + ");
                }
                result.append(quantity[n]).append(resType);
            } else
                firstNotNull++;
        }
        return result.toString();
    }

    /**
     * Prints the hand leader cards.
     *
     */
    private void printHandCards() {
        output.println("HAND LEADER CARDS:\n");
        printLeadCards(clientView.getHand());
    }

    /**
     * Returns a string which represents a list of cards needed to play
     * a leader card.
     *
     * @param list the list of development cards
     * @return the built string
     */
    private String buildDevCardString(List<DevCardInfo> list) {
        StringBuilder result = new StringBuilder();
        for (DevCardInfo d : list) {
            result.append(d.getColour());
            if (d.getLevel() != 0)
                result.append(" LVL ").append(d.getLevel());
            if (list.indexOf(d) != list.size() - 1)
                result.append(" + ");
        }
        return result.toString();
    }

    /**
     * Prints all the game board elements of a player's game board.
     *
     * @param g the game board to print
     */
    private void printGameBoard(GameBoardInfo g) {
        printItinerary(g);
        printChest(g);
        printWarehouse(g);
        printDevSpace(g);
        printPlayedLeadCards(g);
    }

    /**
     * Prints the itinerary of a player.
     *
     * @param g the game board that contains the itinerary
     */
    private void printItinerary(GameBoardInfo g) {
        if (g != null){
            output.println("ITINERARY:\n\n" +
                    "Position: " + g.getPosition() + "/24");
            if (g.getBlackCrossPosition() != null)
                output.println("Lorenzo De Medici's position: " + g.getBlackCrossPosition() + "/24");
            output.println("Papal cards status: ");
            for (int i = 0; i < g.getPapalCards().size(); i++)
                output.println(i + " -> " + g.getPapalCards().get(i) + " (value: " + (i + 2) + ")");
            output.println("\n");
        }
    }

    /**
     * Prints a player's chest.
     *
     * @param g the game board that contains the chest
     */
    private void printChest(GameBoardInfo g) {
        if (g != null){
            output.print("CHEST:\n\n| ");
            List<String> resources = new ArrayList<>(g.getChest().keySet());
            for (String res : resources) {
                output.print(res + ": " + g.getChest().get(res) + " | ");
            }
            output.println("\n");
        }
    }

    /**
     * Prints a player's warehouse.
     *
     * @param g the game board that contains the warehouse
     */
    private void printWarehouse(GameBoardInfo g) {
        if (g != null){
            output.print("WAREHOUSE: \n\n");
            for (int i = 0; i < g.getWarehouse().size(); i++) {
                if (i < 3)
                    output.print("Shelf number " + (i + 1) + ": ");
                else
                    output.print("Depot number " + (i - 2) + ": ");
                for (String s : g.getWarehouse().get(i))
                    output.print(s + " ");
                output.println();
            }
            output.println("\n");
        }
    }

    /**
     * Prints the development space of a player.
     *
     * @param g the game board that contains the development space
     */
    private void printDevSpace(GameBoardInfo g) {
        if (g != null){
            output.print("DEVELOPMENT SPACE: \n\n");
            for (int i = 0; i < 3; i++)
                output.print(Constants.devCardBorder + " ");
            output.print("\n");
            for (int i = 0; i < 3; i++) {
                if (g.getDevSpace().get(i).isEmpty())
                    output.print(Constants.emptyDevCardBorder);
                else
                    printCardElement("* COL: " + g.getDevSpace().get(i).get(0).getColour(), true);
            }
            output.print("\n");
            for (int i = 0; i < 3; i++) {
                if (g.getDevSpace().get(i).isEmpty())
                    output.print(Constants.emptyDevCardBorder);
                else
                    printCardElement("* LVL: " + g.getDevSpace().get(i).get(0).getLevel(), true);
            }
            output.print("\n");
            for (int i = 0; i < 3; i++) {
                if (g.getDevSpace().get(i).isEmpty())
                    output.print(Constants.emptyDevCardBorder);
                else
                    printCardElement("* VP:  " + g.getDevSpace().get(i).get(0).getVictoryPoints(), true);
            }
            output.print("\n");
            for (int i = 0; i < 3; i++) {
                if (g.getDevSpace().get(i).isEmpty())
                    output.print(Constants.emptyDevCardBorder);
                else
                    printCardElement("* REQ: " + buildResourceString(g.getDevSpace().get(i).get(0).getResourceRequirements()), true);
            }
            output.print("\n");
            for (int i = 0; i < 3; i++) {
                if (g.getDevSpace().get(i).isEmpty())
                    output.print(Constants.emptyDevCardBorder);
                else
                    printCardElement("* IN:  " + buildResourceString(g.getDevSpace().get(i).get(0).getProductionInput()), true);
            }
            output.print("\n");
            for (int i = 0; i < 3; i++) {
                if (g.getDevSpace().get(i).isEmpty())
                    output.print(Constants.emptyDevCardBorder);
                else
                    printCardElement("* OUT: " + buildResourceString(g.getDevSpace().get(i).get(0).getProductionOutput()), true);
            }
            output.print("\n");
            for (int i = 0; i < 3; i++)
                output.print(Constants.devCardBorder + " ");
            output.print("\n\n");
        }
    }

    /**
     * Prints the player's played leader cards.
     *
     * @param g the game board that contains the played leader cards
     */
    private void printPlayedLeadCards(GameBoardInfo g) {
        if (g != null){
            output.println("PLAYED LEADER CARDS:\n");
            printLeadCards(g.getPlayedCards());
        }
    }

    /**
     * Prints leader cards.
     *
     * @param cards the list of leader cards to print
     */
    private void printLeadCards(List<LeadCardInfo> cards) {
        if (!(cards.isEmpty())) {
            for (int i = 0; i < cards.size(); i++) {
                output.print(Constants.leadCardBorder + " ");
            }
            output.print("\n");
            for (LeadCardInfo card : cards) {
                printCardElement("* VP: " + card.getVictoryPoints(), false);
            }
            output.print("\n");
            for (LeadCardInfo card : cards) {
                if (card.getCardRequirements() == null)
                    printCardElement("* RES REQ: " + buildResourceString(card.getResourceRequirements()), false);
                else
                    printCardElement("* CARD REQ: " + buildDevCardString(card.getCardRequirements()), false);
            }
            output.print("\n");
            for (LeadCardInfo card : cards) {
                printCardElement("* TYPE: " + card.getType(), false);
            }
            output.print("\n");
            for (LeadCardInfo card : cards) {
                printCardElement("* RES: " + card.getResource(), false);
            }
            output.print("\n");
            for (int i = 0; i < cards.size(); i++) {
                output.print(Constants.leadCardBorder + " ");
            }
            output.print("\n\n");
        }

    }

    /**
     * Reads input from the player, expecting a string.
     *
     * @return the string inserted by the player
     */
    private String readInputString() {
        String inputString;
        do {
            try {
                inputString = input.nextLine();
                break;
            } catch (InputMismatchException e) {
                output.print("Please insert a valid input.\n>");
            }
        } while (true);
        return inputString;
    }

    /**
     * Reads input from the player, expecting an int.
     *
     * @return the int inserted by the player
     */
    private Integer readInputInt(boolean canQuit) {
        int inputInt;
        String line;
        do {
            try {
                line = input.nextLine();
                if(line.equalsIgnoreCase("back") && canQuit) return null;
                inputInt = Integer.parseInt(line);
                break;
            } catch (InputMismatchException | NumberFormatException e) {
                output.print("Please insert a valid input.\n>");
            }
        } while (true);
        return inputInt;
    }

    /**
     * Returns the ClientView object associated with this cli.
     *
     * @return the ClientView object associated with this cli
     */
    public ClientView getClientView() {
        return clientView;
    }
}