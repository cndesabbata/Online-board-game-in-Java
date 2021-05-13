package it.polimi.ingsw.client.view;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.messages.actions.BuyResources;
import it.polimi.ingsw.messages.actions.PlayLeadCard;
import it.polimi.ingsw.messages.clientMessages.EndTurn;
import it.polimi.ingsw.server.controller.leaders.LeaderEffect;
import it.polimi.ingsw.server.controller.leaders.MarbleEffect;
import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.MarketSelection;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.ResourcePosition;

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
        List<LeaderEffect> leaderEffects = new ArrayList<>();
        List<ResourcePosition> result = new ArrayList<>();
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
        for (String s : marbleToReceive)
            result.add(cli.askForLocation(Resource.valueOf(s.toUpperCase()), true));
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
            List<ResourcePosition> rp1 = new ArrayList<>();
            List<ResourcePosition> rp2 = new ArrayList<>();
            for (int i = 0; i < firstType; i++)
                rp1.add(cli.askForLocation(Resource.valueOf(res.get(0).toUpperCase()), true));
            for (int i = 0; i < firstType; i++)
                rp2.add(cli.askForLocation(Resource.valueOf(res.get(1).toUpperCase()), true));
            leaderEffects.add(new MarbleEffect(firstType, Resource.valueOf(res.get(0).toUpperCase()), rp1));
            leaderEffects.add(new MarbleEffect(secondType, Resource.valueOf(res.get(1).toUpperCase()), rp2));
        }
        else if (marbleLeaderNumber == 1){
            List<ResourcePosition> rp1 = new ArrayList<>();
            Resource res = Resource.valueOf(cli.getClientView().getOwnGameBoard().getPlayedCards()
                    .stream().filter(p -> p.getType().equalsIgnoreCase("MARBLE"))
                    .collect(Collectors.toList()).get(0).getResource());
            for (int i = 0; i < whiteMarbles; i++)
                rp1.add(cli.askForLocation(res, true));
            leaderEffects.add(new MarbleEffect(whiteMarbles, res, rp1));
        }
        return new BuyResources(leaderEffects, source, marketSelection, result);
    }

    private Action buildBuyDevCard(){
        return null;
    }

    private Action buildStartProduction(){
        return null;
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
