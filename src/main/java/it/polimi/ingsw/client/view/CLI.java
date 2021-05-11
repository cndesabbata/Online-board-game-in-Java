package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientNetwork.ClientConnectionSocket;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.client.clientNetwork.MessageHandler;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.messages.clientMessages.LeaderCardSelection;
import it.polimi.ingsw.messages.clientMessages.ResourceSelection;
import it.polimi.ingsw.messages.clientMessages.SetPlayersNumber;
import it.polimi.ingsw.messages.clientMessages.internal.*;
import it.polimi.ingsw.messages.serverMessages.PlayersNumberMessage;
import it.polimi.ingsw.messages.serverMessages.newElement.NewIndex;
import it.polimi.ingsw.server.controller.Place;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.ResourcePosition;
import it.polimi.ingsw.server.model.gameboard.NumOfShelf;
import it.polimi.ingsw.server.observer.Observer;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class CLI implements Observer {
    private final PrintStream output;
    private final Scanner input;
    private final ClientView clientView;
    private final MessageHandler messageHandler;
    private boolean active;
    private ClientConnectionSocket connectionSocket;

    public CLI() {
        input = new Scanner(System.in);
        output = new PrintStream(System.out);
        clientView = new ClientView(this);
        messageHandler = new MessageHandler(clientView);
        active = true;
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insert the server IP address\n>");
        String ip = scanner.nextLine();
        System.out.print("Insert the server port\n>");
        int port = scanner.nextInt();
        Constants.setAddress(ip);
        Constants.setPort(port);
        CLI cli = new CLI();
        cli.setup();
    }

    public boolean isActive() {
        return active;
    }

    private void setup(){
        connectionSocket = new ClientConnectionSocket(this, messageHandler);
        try {
            while(!connectionSocket.setupConnection(clientView)) {
                output.println("The entered IP/port doesn't match any active server. Please try again!");
                output.print("Insert the server IP address\n>");
                Constants.setAddress(input.nextLine());
                output.print("Insert the server port\n>");
                Constants.setPort(input.nextInt());
            }
            System.out.println("Socket Connection setup completed!");
        } catch (IOException e){
            System.err.println("Error during socket configuration! Application will now close.");
            System.exit(0);
        }
        String nickname = null;
        boolean confirmation = false;
        boolean newPlayer = true;
        while (!confirmation) {
            boolean ok = false;
            while (!ok){
                output.print("Would you like to start a new match or resume an existing one? [start/resume]\n>");
                String answer = input.nextLine().toUpperCase();
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
            nickname = input.nextLine();
            if (nickname != null && !nickname.isEmpty() && connectionSocket.setupNickname(nickname, newPlayer))
                confirmation = true;
        }
        clientView.setNickname(nickname);
    }

    @Override
    public void update(Message message) {
        ViewMessage m = (ViewMessage) message;
        boolean request = true;
        if (m instanceof DisplayMessage){
            output.println(m.getMessage());
        }
        else if (m instanceof RequestPlayersNumber){
            output.print(m.getMessage() + "\n>");
            int number = 0;
            while (request){
                number = input.nextInt();
                if (number >= 1 && number <= 4) request = false;
                else output.print("Please choose a number between 1 and 4:\n>");
            }
            connectionSocket.send(new SetPlayersNumber(number));
        }
        else if (m instanceof SetupDiscard){
            output.print(m.getMessage());
            int index1 = 0;
            int index2 = 0;
            while (request){
                index1 = input.nextInt();
                index2 = input.nextInt();
                if (index1 < 1 || index1 > 4 || index2 < 1 || index2 > 4)
                    output.print("Please choose two numbers between 1 and 4:\n>");
                else request = false;
            }
            connectionSocket.send(new LeaderCardSelection(new int[]{index1, index2}));
        }
        else if (m instanceof SetupResources){
            output.print(m.getMessage());
            List<ResourcePosition> rp = new ArrayList<>();
            if (clientView.getPlayerIndex() == 1 || clientView.getPlayerIndex() == 2){
                Resource r = askForResource();
                rp.add(askForLocation(r, false));
            }
            else if (clientView.getPlayerIndex() == 3){
                for (int i = 0; i < 2; i++){
                    Resource r = askForResource();
                    rp.add(askForLocation(r, false));
                }
            }
            connectionSocket.send(new ResourceSelection(rp));
        }
        else if (m instanceof ChooseAction){
            output.print(m.getMessage());
            int n = -1;
            while (request){
                n = input.nextInt();
                if (n < 0 || n > 9) output.print("Please choose a number between 0 and 9:\n>");
                else request = false;
            }
            connectionSocket.send(ActionFactory.createAction(n));
        }
        else if (m instanceof NewView){
            printMarket();
            printDevDecks();
            printHandCards();
            output.println("YOUR GAME BOARD: \n");
            printGameBoard(clientView.getOwnGameBoard());
            for (GameBoardInfo g : clientView.getOtherGameBoards()){
                output.println(g.getOwner().toUpperCase() + "'S GAME BOARD: \n");
                printGameBoard(g);
            }

        }
    }

    private Resource askForResource(){
        boolean request = true;
        Resource r = null;
        while (request){
            String s = input.nextLine().toUpperCase();
            try{
                r = Resource.valueOf(s);
                request = false;
            } catch (IllegalArgumentException e){
                output.print("Please select a valid resource:\n>");
            }
        }
        return r;
    }

    private ResourcePosition askForLocation(Resource resource, boolean canDiscard){
        Place p;
        output.print("Where would you like to put it? [Warehouse, Chest]");
        while (true){
            String s = input.nextLine().toUpperCase();
            try{
                p = Place.valueOf(s);
                NumOfShelf num = null;
                if (!(p == Place.TRASH_CAN && !canDiscard)) {
                    if (p == Place.WAREHOUSE){
                        output.print("In which shelf would you like to put it? [ 1 / 2 / 3 (4 & 5 are the depots)]\n>");
                        boolean ok = false;
                        while (!ok){
                            int n = input.nextInt();
                            if (n < 1 || n > clientView.getOwnGameBoard().getWarehouse().size())
                                output.print("Please select a number between 1 and"
                                        + clientView.getOwnGameBoard().getWarehouse().size() + " :\n>");
                            else{
                                ok = true;
                                num = NumOfShelf.values()[n-1];
                            }
                        }
                    }
                    return new ResourcePosition(resource, p, num);
                }
                output.print("You cannot put resources in the trash-can, please select between Warehouse and Chest:\n>");
            } catch (IllegalArgumentException e){
                output.print("Please select a valid position:\n>");
            }
        }
    }

    private void printMarket(){
        String[][] market = clientView.getMarket();
        output.print("MARKET: \n");
        output.println("EXTERNAL MARBLE = " + clientView.getExternalMarble() + "\n");
        for (int n = 0; n < 3; n++){
            for (int i = 0; i < 36; i++)
                output.print("*");
            output.print("*\n");
            for (int i = 0; i < 4; i++)
                output.print("*        ");
            output.print("*\n");
            for (int i = 0; i < 4; i++){
                output.print("* ");
                output.print(market[n][i]);
                for (int j = 0; j < 7 - market[n][i].length(); j++)
                    output.print(" ");
            }
            output.print("*\n");
            for (int i = 0; i < 4; i++)
                output.print("*        ");
            output.print("*\n");
            for (int i = 0; i < 36; i++)
                output.print("*");
            output.print("*\n");
        }
    }

    private void printDevDecks(){
        output.println("DEVELOPMENT DECKS:\n");
        for (DevCardInfo[] a : clientView.getDevDecks()){
            for (DevCardInfo d : a){
                printDevCard(d);
            }
        }
    }

    private void printDevCard(DevCardInfo d){
        printCardElement("Colour: " + d.getColour());
        printCardElement("Level: " + d.getLevel());
        printCardElement("Victory Points: " + d.getVictoryPoints());
        printCardElement("Requisites = " + buildResourceString(d.getResourceRequirements()));
        printCardElement("Production input =  " + buildResourceString(d.getProductionInput()));
        printCardElement("Production output =  " + buildResourceString(d.getProductionOutput()));
        output.println("\n");
    }

    private void printCardElement(String s){
        for (int i = 0; i < 64; i++)
            output.print("*");
        output.print("*\n*");
        for (int i = 0; i < 63; i++)
            output.print(" ");
        output.print("*\n* "+ s);
        for (int i = 0; i < 62 - s.length(); i++)
            output.print(" ");
        output.print("*\n*");
        for (int i = 0; i < 63; i++)
            output.print(" ");
        output.print("*\n");
        for (int i = 0; i < 64; i++)
            output.print("*");
        output.print("*\n");
    }

    private String buildResourceString(List<String> list){
        int[] quantity = {0,0,0,0};
        StringBuilder result = new StringBuilder();
        for (String s : list){
            switch (s.toUpperCase()){
                case "COIN": quantity[0]++; break;
                case "STONE": quantity[1]++; break;
                case "SERVANT": quantity[2]++; break;
                case "SHIELD": quantity[3]++; break;
            }
        }
        for (int n = 0; n < 4; n++){
            String resType = "";
            switch (n){
                case 0: resType = "Coins: "; break;
                case 1: resType = "Stones: "; break;
                case 2: resType = "Servants: "; break;
                case 3: resType = "Shields: "; break;
            }
            if (quantity[n] != 0)
                result.append(resType).append(quantity[n]);
            if (n != 3)
                result.append(" + ");
        }
        return result.toString();
    }

    private void printHandCards(){
        output.println("HAND LEADER CARDS:\n");
        for (LeadCardInfo l : clientView.getHand()){
            printCardElement("Victory points: " + l.getVictoryPoints());
            if (l.getCardRequirements() == null)
                printCardElement("Resource requisites: " + buildResourceString(l.getResourceRequirements()));
            else
                printCardElement("Card requisites: " + buildDevCardString(l.getCardRequirements()));
            printCardElement("Leader Power Type: " + l.getType());
            printCardElement("Leader Resource: " + l.getResource());
        }
        output.println("\n");
    }

    private String buildDevCardString(List<DevCardInfo> list){
        StringBuilder result = new StringBuilder();
        for (DevCardInfo d : list){
            result.append("Colour: ").append(d.getColour()).append("Level: ").append(d.getLevel());
            if (list.indexOf(d) != list.size() - 1)
                result.append(" + ");
        }
        return result.toString();
    }

    private void printGameBoard(GameBoardInfo g){
        printItinerary(g);
        printChest(g);
        printWarehouse(g);
        printDevSpace(g);
        printPlayedLeadCards(g);
    }

    private void printItinerary(GameBoardInfo g){
        output.println("ITINERARY:\n\n" +
                "Position: " + g.getPosition() + "/24");
        if (g.getBlackCrossPosition() != null)
            output.println("Lorenzo De Medici's position: " + g.getPosition() + "/24");
        output.println("Papal cards status: ");
        for (int i = 0; i < g.getPapalCards().size(); i++)
            output.println(i + " -> " + g.getPapalCards().get(i) + " (value: " + (i + 2) + ")");
        output.println("\n");
    }

    private void printChest(GameBoardInfo g){
        output.print("CHEST:\n\n| ");
        List<String> resources = new ArrayList<>(g.getChest().keySet());
        for (String res : resources){
            output.print(res + "s: " + g.getChest().get(res) + " | ");
        }
        output.println("\n");
    }

    private void printWarehouse(GameBoardInfo g){
        output.print("WAREHOUSE: \n\n");
        for (int i = 0; i < g.getWarehouse().size(); i++){
            if (i < 3)
                output.print("Shelf number " + (i + 1) + ": ");
            else
                output.print("Depot number: " + (i - 2) + ": ");
            for (String s : g.getWarehouse().get(i))
                output.print( s + " ");
            output.println();
        }
        output.println("\n");
    }

    private void printDevSpace(GameBoardInfo g){
        output.print("DEVELOPMENT SPACE: \n\n");
        for (int i = 0; i < 3; i++){
            output.print("Slot number " + (i+1) + ":\n");
            printDevCard(g.getDevSpace().get(i).get(0));
        }
        output.println("\n");
    }

    private void printPlayedLeadCards(GameBoardInfo g){
        output.println("PLAYED LEADER CARDS:\n");
        for (LeadCardInfo l :g.getPlayedCards()){
            printCardElement("Victory points: " + l.getVictoryPoints());
            if (l.getCardRequirements() == null)
                printCardElement("Resource requisites: " + buildResourceString(l.getResourceRequirements()));
            else
                printCardElement("Card requisites: " + buildDevCardString(l.getCardRequirements()));
            printCardElement("Leader Power Type: " + l.getType());
            printCardElement("Leader Resource: " + l.getResource());
        }
        output.println("\n");
    }


}