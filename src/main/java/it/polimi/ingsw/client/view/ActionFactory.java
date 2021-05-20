package it.polimi.ingsw.client.view;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.messages.actions.BuyDevCard;
import it.polimi.ingsw.messages.actions.BuyResources;
import it.polimi.ingsw.messages.actions.StartProduction;
import it.polimi.ingsw.messages.clientMessages.EndTurn;
import it.polimi.ingsw.server.controller.leaders.*;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.gameboard.DevSpaceSlot;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

public class ActionFactory {
    private final PrintStream output;
    private final Scanner input;
    private final CLI cli;

    public ActionFactory(PrintStream output, Scanner input, CLI cli) {
        this.output = output;
        this.input = input;
        this.cli = cli;
    }

    public Message createAction(int actionNumber){
        switch (actionNumber){
            case 0: return buildBuyResources();
            case 1: return buildBuyDevCard();
            case 2: return buildStartProduction();
            case 3: return buildDiscardLeadCard();
            case 4: return buildPlayLeadCard();
            case 5: return buildMoveResources();
            case 10: return new EndTurn();
        }
        return null;
    }

    private Action buildBuyResources(){
        output.print("Would you like to buy resources from a column or a row? [column/row]");
        String selection = readInputString();
        MarketSelection marketSelection = null;
        while (true){
            try{
                marketSelection = MarketSelection.valueOf(selection);
                break;
            } catch (InputMismatchException e){
                output.print("Please insert a valid input:\n>");
            }
        }
        int source = -1;
        int whiteMarbles;
        int firstType = 0;
        int secondType = 0;
        List<String> marbleToReceive;
        List<String> resToReceive = new ArrayList<>();
        List<LeaderEffect> leaderEffects = new ArrayList<>();
        List<ResourcePosition> result;
        if (marketSelection == MarketSelection.ROW){
            do {
                output.print("Which row would you like to buy resources from? [1/2/3]\n>");
                source = readInputInt() - 1;
                if (source < 0 || source > 2) output.print("Please select a number between 1 and 3:\n>");
            } while (source < 0 || source > 2);
            whiteMarbles = (int) Arrays.stream(cli.getClientView().getMarket()[source])
                    .filter(s -> s.equalsIgnoreCase("WHITE")).count();
            marbleToReceive = Arrays.stream(cli.getClientView().getMarket()[source])
                    .filter(s -> !s.equalsIgnoreCase("WHITE")).collect(Collectors.toList());
        }
        else {
            do {
                output.print("Which column would you like to buy resources from? [1/2/3/4]\n>");
                source = readInputInt() - 1;
                if (source < 0 || source > 3) output.print("Please select a number between 1 and 4:\n>");
            } while (source < 0 || source > 3);
            whiteMarbles = (int) Arrays.stream(getColumn(cli.getClientView().getMarket(), source))
                    .filter(s -> s.equalsIgnoreCase("WHITE")).count();
            marbleToReceive = Arrays.stream(getColumn(cli.getClientView().getMarket(), source))
                    .filter(s -> !s.equalsIgnoreCase("WHITE")).collect(Collectors.toList());
        }
        for (String s : marbleToReceive){
            switch (s.toUpperCase()){
                case "GREY" -> resToReceive.add("STONE");
                case "YELLOW" -> resToReceive.add("COIN");
                case "BLUE" -> resToReceive.add("SHIELD");
                case "PURPLE" -> resToReceive.add("SERVANT");
                case "RED" -> resToReceive.add("FAITHPOINT");
            }
        }
        result = cli.askForLocation(resToReceive, true, true);
        int marbleLeaderNumber = (int) cli.getClientView().getOwnGameBoard().getPlayedCards()
                .stream().filter(p -> p.getType().equalsIgnoreCase("MARBLE")).count();
        if (marbleLeaderNumber == 2){
            List<String> res = new ArrayList<>();
            for (LeadCardInfo l : cli.getClientView().getOwnGameBoard().getPlayedCards())
                res.add(l.getResource());
            output.println("You have two marble leaders, respectively of "
                    + res.get(0) + " and " + res.get(1) + " resource type, and " +
                    whiteMarbles + "white marbles.");
            do{
                output.print("How many of them would you like to convert to " + res.get(0) + "s?\n>");
                firstType = readInputInt();
                output.print("How many of them would you like to convert to " + res.get(0) + "s?\n>");
                secondType = readInputInt();
                if (firstType + secondType != whiteMarbles)
                    output.print("Please insert a valid amount (the sum of the resources has to be " +
                            whiteMarbles + ").\n");
            } while (firstType + secondType != whiteMarbles);
            List<String> s1 = new ArrayList<>();
            List<String> s2 = new ArrayList<>();
            for (int i = 0; i < firstType; i++)
                s1.add(res.get(0));
            for (int i = 0; i < firstType; i++)
                s1.add(res.get(1));
            List<ResourcePosition> rp1 = cli.askForLocation(s1, true, true);
            List<ResourcePosition> rp2 = cli.askForLocation(s2, true, true);
            leaderEffects.add(new MarbleEffect(firstType, Resource.valueOf(res.get(0).toUpperCase()), rp1));
            leaderEffects.add(new MarbleEffect(secondType, Resource.valueOf(res.get(1).toUpperCase()), rp2));
        }
        else if (marbleLeaderNumber == 1){
            List<String> s1 = new ArrayList<>();
            List<ResourcePosition> rp1 = new ArrayList<>();
            Resource res = Resource.valueOf(cli.getClientView().getOwnGameBoard().getPlayedCards()
                    .stream().filter(p -> p.getType().equalsIgnoreCase("MARBLE"))
                    .collect(Collectors.toList()).get(0).getResource());
            for (int i = 0; i < whiteMarbles; i++)
                s1.add(res.toString());
            rp1 = cli.askForLocation(s1, true, true);
            leaderEffects.add(new MarbleEffect(whiteMarbles, res, rp1));
        }
        if(cli.getClientView().getOwnGameBoard().getPlayedCards().stream().anyMatch(L -> L.getType().equals("Depot"))){ //adding depots leader effects
            for (LeadCardInfo l : cli.getClientView().getOwnGameBoard().getPlayedCards()){
                if(l.getType().equals("Depot"))
                    leaderEffects.add(new DepotEffect(Resource.valueOf(l.getResource())));
            }
        }
        return new BuyResources(leaderEffects, source, marketSelection, result);
    }

    private Action buildBuyDevCard(){
        Colour colour = null;
        int lev = -1;
        int slot = -1;
        List<ResourcePosition> res = new ArrayList<>();
        List<LeaderEffect> leaders = new ArrayList<>();
        while (true){
            while (true){
                output.print("What colour is the card you would like to buy? [Green/Blue/Yellow/Purple]\n>");
                String col = readInputString();
                try{
                    colour = Colour.valueOf(col);
                    break;
                } catch (InputMismatchException e){
                    output.println("Please insert a valid colour.");
                }
            }
            do {
                output.print("What level is the card you would like to buy? [1/2/3]\n>");
                lev = readInputInt() - 1;
                if (lev < 0 || lev > 2) output.print("Please insert a valid number.");
            } while (lev < 0 || lev > 2);
            if (cli.getClientView().getDevDecks()[lev][colour.ordinal()] != null) break;
            else
                output.println("The selected deck is empty, please select another one.");
        }
        while (true){
            output.print("In which slot would you like to put your card? [1/2/3]\n>");
            slot = readInputInt() - 1;
            if (slot >= 0 && slot <= 2 && checkDevSpaceSlot(slot, lev)) break;
            else if (slot < 0 || slot >2) output.println("Please insert a valid number.");
            else output.println("The selected slot cannot host your card, please choose another one.");
        }
        List<String> req = cli.getClientView().getDevDecks()[lev][colour.ordinal()].getResourceRequirements();
        for (LeadCardInfo l : cli.getClientView().getOwnGameBoard().getPlayedCards()){
            if (l.getType().equalsIgnoreCase("Discount")){
                for (String s : req){
                    if (s.equalsIgnoreCase(l.getResource())){
                        output.print("You have a leader card which could grant you this card for one less "
                                + l.getResource().toLowerCase() + ", would you like to use this discount? [Y/N]\n>");
                        boolean discount;
                        while (true){
                            String answer = readInputString();
                            if (answer.equalsIgnoreCase("Y")){
                                discount = true;
                                break;
                            }
                            else if (answer.equalsIgnoreCase("N")){
                                discount = false;
                                break;
                            }
                            else output.print("Please insert a valid input.\n>");
                        }
                        if (discount){
                            req.remove(s);
                            leaders.add(new DiscountEffect(Resource.valueOf(s.toUpperCase())));
                        }
                        break;
                    }
                }
            }
        }
        output.println("Your card has the following requirements:\n" + cli.buildResourceString(req));
        res = cli.askForLocation(req, false, false);
        if(cli.getClientView().getOwnGameBoard().getPlayedCards().stream().anyMatch(L -> L.getType().equals("Depot"))){ //adding depots leader effects
            for (LeadCardInfo l : cli.getClientView().getOwnGameBoard().getPlayedCards()){
                if(l.getType().equals("Depot"))
                    leaders.add(new DepotEffect(Resource.valueOf(l.getResource())));
            }
        }
        return new BuyDevCard(lev, colour, DevSpaceSlot.values()[slot], res, leaders);
    }

    private boolean checkDevSpaceSlot(int slot, int lev){
        return (cli.getClientView().getOwnGameBoard().getDevSpace().get(slot).isEmpty() && lev == 1) ||
                (cli.getClientView().getOwnGameBoard().getDevSpace().get(slot).get(0).getLevel() == lev - 1);
    }

    private Action buildStartProduction(){
        List <Integer> slots = new ArrayList<>();
        List<ResourcePosition> inp = new ArrayList<>();
        List<ResourcePosition> out = new ArrayList<>();
        List <LeaderEffect> leaderEffects = new ArrayList<>();
        boolean request = true;
        int slot;
        while(request) {                                                                                                //getting all the devCards the user wants to activate
            output.print("Select the slot in the development space:\n>");
            while (true) {
                slot = readInputInt();
                if (slot >= 1 && slot <= 3 && !cli.getClientView().getOwnGameBoard().getDevSpace().get(slot).isEmpty())
                    break;
                output.print("Please select a number from 1 to 3 and make sure that the selected slot is not empty.\n");
            }
            slots.add(slot);
            DevCardInfo d = cli.getClientView().getOwnGameBoard().getDevSpace().get(slot).get(0);
            inp.addAll(cli.askForLocation(d.getProductionInput(), false, false));
            out.addAll(cli.askForLocation(d.getProductionOutput(), true, false));
            request = askYesNo("Would you like to select another development card? [yes/no]\n>");
        }
        if(askYesNo("Would you like to start the board production? [yes/no]\n>")){
            List <String> boardInput = new ArrayList<>();
            List <String> boardOutput = new ArrayList<>();
            for(int i = 0; i < 3; i++) {
                if (i < 2)                                                                                              //input of boardProduction
                    output.print("Choose the resource number " +(i+1)+ "that you want to use: " +
                            "[Coin, Stone, Servant, Shield]\n>");
                else {                                                                                                  //output of BoardProduction
                    inp.addAll(cli.askForLocation(boardInput, false, false));
                    output.print("Choose the resource number " +(i+1)+ "that you want to generate: " +
                            "[Coin, Stone, Servant, Shield]\n>");
                }
                String res;
                while (true) {
                    res = readInputString();
                    if (!res.equalsIgnoreCase("Coin") && !res.equalsIgnoreCase("Shield") &&
                            !res.equalsIgnoreCase("Servant") && !res.equalsIgnoreCase("Shield"))
                        output.print("Please select a resource between [Coin / Stone / Servant / Shield]\n>");
                    else break;
                }
                if(i < 2)                                                                                               //input of boardProduction
                    boardInput.add(res);
                else                                                                                                    //output of BoardProduction
                    boardOutput.add(res);
            }
            out.addAll(cli.askForLocation(boardOutput, false, false));
        }
        if(cli.getClientView().getOwnGameBoard().getPlayedCards().stream().anyMatch(l -> l.getType().equals("Product"))                                          //productLeader
                && askYesNo("Would you like to use a product leader card?\n>")){
            List <Integer> indexLead = new ArrayList<>();
            String resOut = "";
            for(int i = 0; i < 2; i++) {
                int n;
                output.print("Select the index of the product leader card on your game board: [1 / 2]\n>");
                while (true) {
                    n = readInputInt();
                    if (n > cli.getClientView().getOwnGameBoard().getPlayedCards().size())
                        output.print("There is no played leader card of index " + n + ". Please try again:\n>");
                    else if (!cli.getClientView().getOwnGameBoard().getPlayedCards().get(n - 1).getType().equalsIgnoreCase("Product"))
                        output.print("The selected leader card is not a product leader. Please try again: \n>");
                    else {
                        indexLead.add(n);
                        break;
                    }
                }
                output.print("Select the resource you want to generate: [Coin / Stone / Servant / Shield] \n>");
                while (true) {
                    resOut = readInputString();
                    if (!resOut.equalsIgnoreCase("Coin") && !resOut.equalsIgnoreCase("Stone") &&
                        !resOut.equalsIgnoreCase("Servant") && !resOut.equalsIgnoreCase("Shield"))
                        output.print("Please select a resource between [Coin / Stone / Servant / Shield]:\n>");
                    else break;
                }
                if(i == 0 && !askYesNo("Would you like to select another product leader card?\n>"))
                    break;
            }
            for(int n : indexLead){                                                                                     //construction of leader effects
                List<String> i = new ArrayList<>();
                List<String> o = new ArrayList<>();
                i.add(cli.getClientView().getOwnGameBoard().getPlayedCards().get(n-1).getResource());
                List<ResourcePosition> inputLead = new ArrayList<>(cli.askForLocation(i, false, false));
                o.add(resOut);
                List<ResourcePosition> outputLead = new ArrayList<>(cli.askForLocation(o, true, false));
                leaderEffects.add(new ProductionEffect(inputLead.get(0), outputLead.get(0)));
            }
        }
        if(cli.getClientView().getOwnGameBoard().getPlayedCards().stream().anyMatch(L -> L.getType().equals("Depot"))){ //adding depots leader effects
            for (LeadCardInfo l : cli.getClientView().getOwnGameBoard().getPlayedCards()){
                if(l.getType().equals("Depot"))
                    leaderEffects.add(new DepotEffect(Resource.valueOf(l.getResource())));
            }
        }
    return new StartProduction(slots, inp, out, leaderEffects);
    }

    private boolean askYesNo(String question){
        output.print(question);
        while(true) {
            String s = readInputString();
            if (s.equalsIgnoreCase("no")) {
                return false;
            }
            else if (s.equalsIgnoreCase("yes"))
                return true;
            else
                output.print("Please type yes or no:\n>");
        }
    }

    private Action buildDiscardLeadCard(){
        return null;
    }

    private Action buildPlayLeadCard(){
        return null;
    }

    private Action buildMoveResources(){
        return null;
    }

    private String readInputString(){
        try{
            return input.nextLine();
        } catch (InputMismatchException e){
            output.print("Please insert a valid input.\n>");
            input.next();
            return readInputString();
        }
    }

    private int readInputInt(){
        try{
            return input.nextInt();
        } catch (InputMismatchException e){
            output.print("Please insert a valid input.\n>");
            input.next();
            return readInputInt();
        }
    }

    public String[] getColumn(String[][] array, int index){
        String[] column = new String[3];
        for(int i=0; i<column.length; i++){
            column[i] = array[i][index];
        }
        return column;
    }
}
