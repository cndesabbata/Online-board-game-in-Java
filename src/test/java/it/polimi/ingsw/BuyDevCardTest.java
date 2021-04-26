package it.polimi.ingsw;

import static org.junit.Assert.assertTrue;

import it.polimi.ingsw.controller.BuyDevCard;
import it.polimi.ingsw.controller.Place;
import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BuyDevCardTest {
    @Test
    public void DevCardTest (){
        Game game = new Game();
        GameBoard board = new GameBoard();
        Player player = new Player("Nickname", 1, game);
        Colour colour = randomEnum(Colour.class);
        System.out.println("Colour: "+ colour);
        int level = 1;
        DevSpaceSlot slot = randomEnum(DevSpaceSlot.class);
        int index = (level-1) * Colour.values().length + colour.ordinal();
        printDevDecks(game);
        printSlots(player.getBoard());
        Place place;
        NumOfShelf shelf;
        List<ResourcePosition> req = new ArrayList<>();
        for (ResourceQuantity res : game.getDevDecks()[index].peepRequirements()) {
            for (int i = 0; i < res.getQuantity(); i++){
                req.add(new ResourcePosition(1, res.getResource(), Place.CHEST));
            }
        }
        player.getBoard().getChest().incrementResource(req);
        printChest(player.getBoard());
        BuyDevCard action = new BuyDevCard(level, colour, slot, req, new ArrayList<>());
        try {
            action.checkAction(player);
        }
        catch (WrongActionException e){
            System.out.println(e.getMessage());
        }
        action.doAction(player);
        printDevDecks(game);
        printSlots(player.getBoard());
        printChest(player.getBoard());
        assertTrue(true);
    }

    private void printSlots(GameBoard gameBoard){
        int i = 1;
        for (List<DevCard> l : gameBoard.getDevSpace().getCards()){
            System.out.println("Slot number " + i + ":");
            if (!l.isEmpty()) printCard(l.get(0));
            else System.out.println("Empty\n");
            i++;
        }
    }

    private void printChest(GameBoard gameBoard){
        System.out.println("CHEST: ");
        for (ResourceQuantity res : gameBoard.getChest().getChest()){
            System.out.println(res.getResource() +"S: " +res.getQuantity());
        }
    }

    private void printDevDecks(Game game){
        DevDeck deck;
        for (Colour c : Colour.values()){
            for (int level = 1; level < 4; level++){
                deck = game.getDevDecks()[(level-1) * Colour.values().length + c.ordinal()];
                printCard(deck.getFirstCard());
                break;
            }
        }
    }

    private void printCard(DevCard card){

        for (int i = 0; i < 50; i++) System.out.printf("-");
        if (card != null) {
            System.out.println("\nLevel: " + card.getLevel());
            System.out.println("Colour: " + card.getColour());
            System.out.println("Victory Points: " + card.getVictoryPoints());
            System.out.println("Requirements: ");
            for (ResourceQuantity req : card.getResourceRequirements()) {
                System.out.printf("> " + req.getQuantity() + " " + req.getResource() + "S ");
            }
            System.out.println("\nInput: ");
            for (ResourceQuantity input : card.getProductionInput()) {
                System.out.printf("> " + input.getQuantity() + " " + input.getResource() + "S ");
            }
            System.out.println("\nOutput: ");
            for (ResourceQuantity output : card.getProductionOutput()) {
                System.out.printf("> " + output.getQuantity() + " " + output.getResource() + "S ");
            }
        }
        else System.out.println("Empty deck");
        System.out.print("\n");
        for (int i = 0; i < 50; i++) System.out.printf("-");
        System.out.println("\n");
    }

    private <T extends Enum<?>> T randomEnum(Class<T> myEnum){
        Random random = new Random();
        int x = new Random().nextInt(myEnum.getEnumConstants().length);
        return myEnum.getEnumConstants()[x];
    }
}
