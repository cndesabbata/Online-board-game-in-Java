package it.polimi.ingsw.client.view;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.actions.*;
import it.polimi.ingsw.messages.clientMessages.EndTurn;
import it.polimi.ingsw.server.controller.Place;
import it.polimi.ingsw.server.controller.leaders.*;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.gameboard.DevSpaceSlot;
import it.polimi.ingsw.server.model.gameboard.NumOfShelf;
import it.polimi.ingsw.server.controller.leaders.ProductionEffect;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

public class ActionFactory {
    private final PrintStream output;
    private final Scanner input;
    private final Cli cli;

    public ActionFactory(PrintStream output, Scanner input, Cli cli) {
        this.output = output;
        this.input = input;
        this.cli = cli;
    }

    public Message createAction(int actionNumber) {
        return switch (actionNumber) {
            case 0 -> buildBuyResources();
            case 1 -> buildBuyDevCard();
            case 2 -> buildStartProduction();
            case 3 -> buildDiscardLeadCard();
            case 4 -> buildPlayLeadCard();
            case 5 -> buildMoveResources();
            case 11 -> new EndTurn();
            default -> null;
        };
    }

    private Action buildBuyResources() {
        output.print("Would you like to buy resources from a column or a row? [column/row]\n>");
        String selection = readInputString();
        if (selection.equalsIgnoreCase("back")) return null;
        MarketSelection marketSelection;
        while (true) {
            try {
                marketSelection = MarketSelection.valueOf(selection.toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                output.print("Please insert a valid input, or type 'back' to quit:\n>");
                selection = readInputString();
                if (selection.equalsIgnoreCase("back")) return null;
            }
        }
        Integer source;
        int whiteMarbles;
        Integer firstType;
        Integer secondType;
        List<String> marbleToReceive;
        List<String> resToReceive = new ArrayList<>();
        List<LeaderEffect> leaderEffects = new ArrayList<>();
        List<ResourcePosition> result;
        if (marketSelection == MarketSelection.ROW) {
            do {
                output.print("Which row would you like to buy resources from? [1/2/3]\n>");
                source = readInputInt();
                if (source == null) return null;
                source--;
                if (source < 0 || source > 2) output.print("Please select a number between 1 and 3, or type 'back' to quit:\n>");
            } while (source < 0 || source > 2);
            whiteMarbles = (int) Arrays.stream(cli.getClientView().getMarket()[source])
                    .filter(s -> s.equalsIgnoreCase("WHITE")).count();
            marbleToReceive = Arrays.stream(cli.getClientView().getMarket()[source])
                    .filter(s -> !s.equalsIgnoreCase("WHITE")).collect(Collectors.toList());
        } else {
            do {
                output.print("Which column would you like to buy resources from? [1/2/3/4]\n>");
                source = readInputInt();
                if (source == null) return null;
                source--;
                if (source < 0 || source > 3) output.print("Please select a number between 1 and 4, or type 'back' to quit:\n>");
            } while (source < 0 || source > 3);
            whiteMarbles = (int) Arrays.stream(getColumn(cli.getClientView().getMarket(), source))
                    .filter(s -> s.equalsIgnoreCase("WHITE")).count();
            marbleToReceive = Arrays.stream(getColumn(cli.getClientView().getMarket(), source))
                    .filter(s -> !s.equalsIgnoreCase("WHITE")).collect(Collectors.toList());
        }
        for (String s : marbleToReceive) {
            switch (s.toUpperCase()) {
                case "GREY" -> resToReceive.add("STONE");
                case "YELLOW" -> resToReceive.add("COIN");
                case "BLUE" -> resToReceive.add("SHIELD");
                case "PURPLE" -> resToReceive.add("SERVANT");
                case "RED" -> resToReceive.add("FAITHPOINT");
            }
        }
        result = cli.askForLocation(resToReceive, true, true, false);
        if(result == null) return null;
        int marbleLeaderNumber = (int) cli.getClientView().getOwnGameBoard().getPlayedCards()
                .stream().filter(p -> p.getType().equalsIgnoreCase("MARBLE")).count();
        if (marbleLeaderNumber == 2) {
            List<String> res = new ArrayList<>();
            for (LeadCardInfo l : cli.getClientView().getOwnGameBoard().getPlayedCards())
                res.add(l.getResource());
            output.println("You have two marble leaders, respectively of "
                    + res.get(0) + " and " + res.get(1) + " resource type, and " +
                    whiteMarbles + "white marbles.");
            do {
                output.print("How many of them would you like to convert to " + res.get(0) + "s?\n>");
                firstType = readInputInt();
                if (firstType == null) return null;
                output.print("How many of them would you like to convert to " + res.get(0) + "s?\n>");
                secondType = readInputInt();
                if (secondType == null) return null;
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
            List<ResourcePosition> rp1 = cli.askForLocation(s1, true, true, false);
            if(rp1 == null) return null;
            List<ResourcePosition> rp2 = cli.askForLocation(s2, true, true, false);
            if(rp2 == null) return null;
            leaderEffects.add(new MarbleEffect(firstType, Resource.valueOf(res.get(0).toUpperCase()), rp1));
            leaderEffects.add(new MarbleEffect(secondType, Resource.valueOf(res.get(1).toUpperCase()), rp2));
        } else if (marbleLeaderNumber == 1) {
            List<String> s1 = new ArrayList<>();
            List<ResourcePosition> rp1;
            Resource res = Resource.valueOf(cli.getClientView().getOwnGameBoard().getPlayedCards()
                    .stream().filter(p -> p.getType().equalsIgnoreCase("MARBLE"))
                    .collect(Collectors.toList()).get(0).getResource());
            for (int i = 0; i < whiteMarbles; i++)
                s1.add(res.toString());
            rp1 = cli.askForLocation(s1, true, true, false);
            if(rp1 == null) return null;
            leaderEffects.add(new MarbleEffect(whiteMarbles, res, rp1));
        }
        return new BuyResources(leaderEffects, source + 1, marketSelection, result);
    }

    private Action buildBuyDevCard() {
        Colour colour;
        Integer lev;
        Integer slot;
        List<ResourcePosition> res;
        List<LeaderEffect> leaders = new ArrayList<>();
        while (true) {
            while (true) {
                output.print("What colour is the card you would like to buy? [Green/Blue/Yellow/Purple]\n>");
                String col = readInputString();
                if (col.equalsIgnoreCase("back")) return null;
                col = col.toUpperCase();
                try {
                    colour = Colour.valueOf(col);
                    break;
                } catch (IllegalArgumentException e) {
                    output.println("Please insert a valid colour, or type 'back' to quit.");
                }
            }
            do {
                output.print("What level is the card you would like to buy? [1/2/3]\n>");
                lev = readInputInt();
                if (lev == null) return null;
                lev--;
                if (lev < 0 || lev > 2) output.print("Please insert a valid number, or type 'back' to quit.");
            } while (lev < 0 || lev > 2);
            if (cli.getClientView().getDevDecks()[lev][colour.ordinal()] != null) break;
            else
                output.println("The selected deck is empty, please select another one.");
        }
        while (true) {
            output.print("In which slot would you like to put your card? [1/2/3]\n>");
            slot = readInputInt();
            if (slot == null) return null;
            slot--;
            if (slot >= 0 && slot <= 2 && checkDevSpaceSlot(slot, lev + 1)) break;
            else if (slot < 0 || slot > 2) output.println("Please insert a valid number, or type 'back' to quit.");
            else output.println("The selected slot cannot host your card, please choose another one, or type 'back' to quit.");
        }
        List<String> req = cli.getClientView().getDevDecks()[lev][colour.ordinal()].getResourceRequirements();
        for (LeadCardInfo l : cli.getClientView().getOwnGameBoard().getPlayedCards()) {
            if (l.getType().equalsIgnoreCase("Discount")) {
                for (String s : req) {
                    if (s.equalsIgnoreCase(l.getResource())) {
                        output.print("You have a leader card which could grant you this card for one less "
                                + l.getResource().toLowerCase() + ", would you like to use this discount? [Y/N]\n>");
                        boolean discount;
                        while (true) {
                            String answer = readInputString();
                            if (answer.equalsIgnoreCase("back")) return null;
                            if (answer.equalsIgnoreCase("Y")) {
                                discount = true;
                                break;
                            } else if (answer.equalsIgnoreCase("N")) {
                                discount = false;
                                break;
                            } else output.print("Please insert a valid input, or type 'back' to quit.\n>");
                        }
                        if (discount) {
                            req.remove(s);
                            leaders.add(new DiscountEffect(Resource.valueOf(s.toUpperCase())));
                        }
                        break;
                    }
                }
            }
        }
        output.println("Your card has the following requirements:\n" + cli.buildResourceString(req));
        res = cli.askForLocation(req, false, false, false);
        if(res == null) return null;
        return new BuyDevCard(lev + 1, colour, DevSpaceSlot.values()[slot], res, leaders);
    }

    private boolean checkDevSpaceSlot(int slot, int lev) {
        return ((cli.getClientView().getOwnGameBoard().getDevSpace().get(slot).isEmpty() && lev == 1) ||
                (!cli.getClientView().getOwnGameBoard().getDevSpace().get(slot).isEmpty() &&
                        cli.getClientView().getOwnGameBoard().getDevSpace().get(slot).get(0).getLevel() == lev - 1));
    }

    private Action buildStartProduction() {
        List<Integer> slots = new ArrayList<>();
        List<ResourcePosition> inp = new ArrayList<>();
        List<ResourcePosition> out = new ArrayList<>();
        List<LeaderEffect> leaderEffects = new ArrayList<>();
        boolean request = true;
        Integer slot;
        GameBoardInfo g = cli.getClientView().getOwnGameBoard();
        boolean hasDevCards = false;
        for (int i = 0; i < 3; i++) {
            if (!g.getDevSpace().get(i).isEmpty())
                hasDevCards = true;
        }
        if (hasDevCards) {
            Boolean b1 = askYesNo("Would you like to activate a development card? [yes/no]\n>");
            if (b1 == null) return null;
            if (b1) {
                while (request) {                                                                                                //getting all the devCards the user wants to activate
                    output.print("Select the slot in the development space:\n>");
                    while (true) {
                        slot = readInputInt();
                        if (slot == null) return null;
                        if (slot >= 1 && slot <= 3 && !cli.getClientView().getOwnGameBoard().getDevSpace().get(slot - 1).isEmpty())
                            break;
                        output.print("Please select a number from 1 to 3 and make sure that the selected slot is not empty, or type 'back' to quit.\n>");
                    }
                    slots.add(slot - 1);
                    DevCardInfo d = cli.getClientView().getOwnGameBoard().getDevSpace().get(slot - 1).get(0);
                    List <ResourcePosition> prodInp = cli.askForLocation(d.getProductionInput(), false, false, false);
                    if(prodInp == null) return null;
                    inp.addAll(prodInp);
                    List<ResourcePosition> bR = new ArrayList<>();
                    for (String output : d.getProductionOutput())
                        bR.add(new ResourcePosition(Resource.valueOf(output.toUpperCase()), Place.CHEST, null));
                    out.addAll(bR);
                    Boolean b2 = askYesNo("Would you like to select another development card? [yes/no]\n>");
                    if (b2 == null) return null;
                    else request = b2;
                }
            }
        }
        Boolean b3 = askYesNo("Would you like to start the board production? [yes/no]\n>");
        if (b3 == null) return null;
        if (b3) {
            List<String> boardInput = new ArrayList<>();
            String boardOutput = "";
            for (int i = 0; i < 3; i++) {
                if (i < 2)                                                                                              //input of boardProduction
                    output.print("Choose the resource number " + (i + 1) + " that you want to use: " +
                            "[Coin, Stone, Servant, Shield]\n>");
                else {                                                                                                  //output of BoardProduction
                    List <ResourcePosition> boardInp = cli.askForLocation(boardInput, false, false, false);
                    if(boardInp == null) return null;
                    inp.addAll(boardInp);
                    output.print("Choose the resource number " + (i + 1) + " that you want to generate: " +
                            "[Coin, Stone, Servant, Shield]\n>");
                }
                String res;
                while (true) {
                    res = readInputString();
                    if (res.equalsIgnoreCase("back")) return null;
                    if (!res.equalsIgnoreCase("Coin") && !res.equalsIgnoreCase("Stone") &&
                            !res.equalsIgnoreCase("Servant") && !res.equalsIgnoreCase("Shield"))
                        output.print("Please select a resource between [Coin / Stone / Servant / Shield]\n>");
                    else break;
                }
                if (i < 2)                                                                                               //input of boardProduction
                    boardInput.add(res);
                else                                                                                                    //output of BoardProduction
                    boardOutput = res;
            }
            out.add(new ResourcePosition(Resource.valueOf(boardOutput.toUpperCase()), Place.CHEST, null));
        }
        if (cli.getClientView().getOwnGameBoard().getPlayedCards().stream().anyMatch(l -> l.getType().equals("Product"))) {                                          //productLeader
            Boolean b4 = askYesNo("Would you like to use a product leader card?\n>");
            if (b4 == null) return null;
            if (b4) {
                List<Integer> indexLead = new ArrayList<>();
                List<String> resOut = new ArrayList<>();
                String s = "";
                for (int i = 0; i < cli.getClientView().getOwnGameBoard().getPlayedCards().size(); i++) {
                    Integer n;
                    output.print("Select the index of the product leader card on your game board: [1 / 2]\n>");
                    while (true) {
                        n = readInputInt();
                        if (n == null) return null;
                        if (n > cli.getClientView().getOwnGameBoard().getPlayedCards().size())
                            output.print("There is no played leader card of index " + n + ". Please try again, or type 'back' to quit:\n>");
                        else if (!cli.getClientView().getOwnGameBoard().getPlayedCards().get(n - 1).getType().equalsIgnoreCase("Product"))
                            output.print("The selected leader card is not a product leader. Please try again, or type 'back' to quit: \n>");
                        else {
                            indexLead.add(n);
                            break;
                        }
                    }
                    output.print("Select the resource you want to generate or type 'back' to quit: [Coin / Stone / Servant / Shield] \n>");
                    while (true) {
                        s = readInputString();
                        if (s.equalsIgnoreCase("back")) return null;
                        if (!s.equalsIgnoreCase("Coin") && !s.equalsIgnoreCase("Stone") &&
                                !s.equalsIgnoreCase("Servant") && !s.equalsIgnoreCase("Shield"))
                            output.print("Please select a resource between [Coin / Stone / Servant / Shield]:\n>");
                        else {
                            resOut.add(s);
                            break;
                        }
                    }
                    Boolean b5 = askYesNo("Would you like to select another product leader card?\n>");
                    if (b5 == null) return null;
                    if (i == 0 && !b5)
                        break;
                }
                int count = 0;
                for (int n : indexLead) {                                                                               //construction of leader effects
                    List<String> i = new ArrayList<>();
                    i.add(cli.getClientView().getOwnGameBoard().getPlayedCards().get(n - 1).getResource());
                    List<ResourcePosition> inputLead = cli.askForLocation(i, false, false, false);
                    if(inputLead == null) return null;
                    ResourcePosition outputLead = new ResourcePosition(Resource.valueOf(resOut.get(count).toUpperCase()), Place.CHEST, null);
                    leaderEffects.add(new ProductionEffect(inputLead.get(0), outputLead));
                    count ++;
                }
            }
        }
        if(inp.isEmpty() && out.isEmpty()) return null;
        return new StartProduction(slots, inp, out, leaderEffects);
    }

    private Boolean askYesNo(String question) {
        output.print(question);
        while (true) {
            String s = readInputString();
            if(s.equalsIgnoreCase("back")) return null;
            else if (s.equalsIgnoreCase("no")) {
                return false;
            } else if (s.equalsIgnoreCase("yes"))
                return true;
            else
                output.print("Please type yes or no:\n>");
        }
    }

    private Action buildDiscardLeadCard() {
        output.print("Which leader card do you want do discard? [1/2]\n>");
        Integer index = leadCardSelection();
        return new DiscardLeadCard(index);
    }

    private Integer leadCardSelection(){
        Integer index = readInputInt();
        if (index == null) return null;
        while (index < 1 || index > 2) {
            output.println("Please select a valid number [1/2], or type 'back' to quit\n>");
            index = readInputInt();
            if (index == null) return null;
        }
        while (index > cli.getClientView().getHand().size()) {
            output.print("Your hand does not contain that leader card. Please select a valid number, or type 'back' to quit.\n>");
            index = readInputInt();
            if (index == null) return null;
        }
        return index;
    }

    private Action buildPlayLeadCard() {
        output.print("Which leader card do you want do play? [1/2]\n>");
        Integer index = leadCardSelection();
        if(index == null) return null;
        return new PlayLeadCard(index);
    }

    private Action buildMoveResources() {
        output.print("Please select the source shelf. [1/2/3/4/5] (4 and 5 represent the depot leaders)\n>");
        Integer source = readInputInt();
        if (source == null) return null;
        while (source < 1 || source > 5) {
            output.print("Please select a valid source shelf [1/2/3/4/5] (4 and 5 represent the depot leaders), or type 'back' to quit\n>");
            source = readInputInt();
            if (source == null) return null;
        }
        output.print("Please select the amount of resources you want to move.\n>");
        Integer quantity = readInputInt();
        if (quantity == null) return null;
        while (quantity < 1 || quantity > 2) {
            output.print("Please select a valid amount of resources.\n>");
            quantity = readInputInt();
            if (quantity == null) return null;
        }
        output.print("Please select the destination shelf [1/2/3/4/5] (4 and 5 represent the depot leaders), or type 'back' to quit\n>");
        Integer dest = readInputInt();
        if (dest == null) return null;
        while (dest == source || dest < 1 || dest > 5) {
            if (dest == source)
                output.print("Destination shelf must be different from source shelf.\n>");
            else
                output.print("Please select a valid destination shelf. [1/2/3/4/5] (4 and 5 represent the depot leaders)\n>");
            dest = readInputInt();
            if (dest == null) return null;
        }
        return new MoveResources(NumOfShelf.values()[source - 1], NumOfShelf.values()[dest - 1], quantity);
    }

    private String readInputString() {
        String inputString;
        do {
            try {
                inputString = input.nextLine();
                break;
            } catch (InputMismatchException e) {
                output.print("Please insert a valid input.\n>");
            }
        }while(true);
        return inputString;
    }

    private Integer readInputInt() {
        String line;
        Integer inputInt;
        do {
            try {
                line = input.nextLine();
                if (line.equalsIgnoreCase("back")) return null;
                inputInt = Integer.parseInt(line);
                break;
            } catch (InputMismatchException | NumberFormatException e) {
                output.print("Please insert a valid input, or type 'back' to quit.\n>");
            }
        }while(true);
        return inputInt;
    }

    public String[] getColumn(String[][] array, int index) {
        String[] column = new String[3];
        for (int i = 0; i < column.length; i++) {
            column[i] = array[i][index];
        }
        return column;
    }
}
