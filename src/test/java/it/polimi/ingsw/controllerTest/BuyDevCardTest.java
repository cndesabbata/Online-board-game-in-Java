package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.messages.actions.BuyDevCard;
import it.polimi.ingsw.server.controller.Place;
import it.polimi.ingsw.server.controller.leaders.DepotEffect;
import it.polimi.ingsw.server.controller.leaders.DiscountEffect;
import it.polimi.ingsw.server.controller.leaders.LeaderEffect;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.gameboard.Chest;
import it.polimi.ingsw.server.model.gameboard.DevSpaceSlot;
import it.polimi.ingsw.server.model.gameboard.NumOfShelf;
import it.polimi.ingsw.server.model.gameboard.Warehouse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class BuyDevCardTest {

    @Test
    public void buyDevCard1Test() {
        Game game = new Game();
        Player me = new Player("Gianluca", game);
        for (DevDeck dd : game.getDevDecks())
            orderDeck(dd);

        /*for(int i = 0; i < 4; i++){
            game.getDevDecks()[1].drawCard();
        }*/

        List<ResourcePosition> inputClient = new ArrayList<>();
        inputClient.add(new ResourcePosition(1, Resource.STONE, Place.CHEST, null));
        inputClient.add(new ResourcePosition(1, Resource.STONE, Place.CHEST, null));

        Warehouse myWarehouse = me.getBoard().getWarehouse();
        Chest myChest = me.getBoard().getChest();
        List<ResourcePosition> resInWarehouse = new ArrayList<>();
        resInWarehouse.add(new ResourcePosition(1, Resource.SERVANT, Place.WAREHOUSE, NumOfShelf.ONE));
        resInWarehouse.add(new ResourcePosition(1, Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        resInWarehouse.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        myWarehouse.incrementResource(resInWarehouse);
        List<ResourcePosition> resInChest = new ArrayList<>();
        resInChest.add(new ResourcePosition(2, Resource.SHIELD, Place.CHEST, null));
        resInChest.add(new ResourcePosition(2, Resource.SERVANT, Place.CHEST, null));
        resInChest.add(new ResourcePosition(2, Resource.STONE, Place.CHEST, null));
        myChest.incrementResource(resInChest);

        List<LeaderEffect> leaderEffects = new ArrayList<>();

        Action buyDevCard = new BuyDevCard(1, Colour.YELLOW, DevSpaceSlot.ONE, inputClient, leaderEffects);

        try {
            buyDevCard.checkAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.SERVANT && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 3);
            assertEquals(2, myChest.getAvailability(Resource.SHIELD));
            assertEquals(2, myChest.getAvailability(Resource.STONE));
            assertEquals(0, myChest.getAvailability(Resource.COIN));
            assertEquals(2, myChest.getAvailability(Resource.SERVANT));

            buyDevCard.doAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.SERVANT && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 3);
            assertEquals(2, myChest.getAvailability(Resource.SHIELD));
            assertEquals(0, myChest.getAvailability(Resource.STONE));
            assertEquals(0, myChest.getAvailability(Resource.COIN));
            assertEquals(2, myChest.getAvailability(Resource.SERVANT));
        } catch (WrongActionException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void buyDevCard2Test() {
        Game game = new Game();
        Player me = new Player("Gianluca", game);
        for (DevDeck dd : game.getDevDecks())
            orderDeck(dd);

        /*for(int i = 0; i < 4; i++){
            game.getDevDecks()[1].drawCard();
        }*/

        assertTrue(me.getBoard().getDevSpace().checkPlace(1, DevSpaceSlot.ONE));
        me.getBoard().getDevSpace().addCard(game.drawDevCard(Colour.BLUE, 1), DevSpaceSlot.ONE);

        List<ResourcePosition> inputClient = new ArrayList<>();
        inputClient.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        inputClient.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        inputClient.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        inputClient.add(new ResourcePosition(1, Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        inputClient.add(new ResourcePosition(1, Resource.STONE, Place.CHEST, null));
        inputClient.add(new ResourcePosition(1, Resource.STONE, Place.CHEST, null));

        Warehouse myWarehouse = me.getBoard().getWarehouse();
        Chest myChest = me.getBoard().getChest();
        List<ResourcePosition> resInWarehouse = new ArrayList<>();
        resInWarehouse.add(new ResourcePosition(1, Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        resInWarehouse.add(new ResourcePosition(1, Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        resInWarehouse.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        myWarehouse.incrementResource(resInWarehouse);
        List<ResourcePosition> resInChest = new ArrayList<>();
        resInChest.add(new ResourcePosition(2, Resource.SHIELD, Place.CHEST, null));
        resInChest.add(new ResourcePosition(2, Resource.SERVANT, Place.CHEST, null));
        resInChest.add(new ResourcePosition(2, Resource.STONE, Place.CHEST, null));
        resInChest.add(new ResourcePosition(2, Resource.COIN, Place.CHEST, null));

        myChest.incrementResource(resInChest);

        List<LeaderEffect> leaderEffects = new ArrayList<>();

        game.drawDevCard(Colour.BLUE, 2);
        game.drawDevCard(Colour.BLUE, 2);
        game.drawDevCard(Colour.BLUE, 2);

        Action buyDevCard = new BuyDevCard(2, Colour.BLUE, DevSpaceSlot.ONE, inputClient, leaderEffects);

        try {
            buyDevCard.checkAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 3);
            assertEquals(2, myChest.getAvailability(Resource.SHIELD));
            assertEquals(2, myChest.getAvailability(Resource.STONE));
            assertEquals(2, myChest.getAvailability(Resource.COIN));
            assertEquals(2, myChest.getAvailability(Resource.SERVANT));

            buyDevCard.doAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 0);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 0);
            assertEquals(2, myChest.getAvailability(Resource.SHIELD));
            assertEquals(0, myChest.getAvailability(Resource.STONE));
            assertEquals(2, myChest.getAvailability(Resource.COIN));
            assertEquals(2, myChest.getAvailability(Resource.SERVANT));
        } catch (WrongActionException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void buyDevCard3Test() {
        Game game = new Game();
        Player me = new Player("Gianluca", game);
        for (DevDeck dd : game.getDevDecks())
            orderDeck(dd);

        /*for(int i = 0; i < 4; i++){
            game.getDevDecks()[1].drawCard();
        }*/

        assertTrue(me.getBoard().getDevSpace().checkPlace(1, DevSpaceSlot.ONE));
        me.getBoard().getDevSpace().addCard(game.drawDevCard(Colour.BLUE, 1), DevSpaceSlot.ONE);
        assertTrue(me.getBoard().getDevSpace().checkPlace(2, DevSpaceSlot.ONE));
        me.getBoard().getDevSpace().addCard(game.drawDevCard(Colour.YELLOW, 2), DevSpaceSlot.ONE);

        List<DevCard> leadDepotReq = new ArrayList<>();
        LeaderCard depotLead = new LeaderCard(3, leadDepotReq, Resource.STONE, LeaderType.DEPOT);
        depotLead.setPlayed(true);
        me.getHandLeaderCards().add(depotLead);

        LeaderCard depotLead1 = new LeaderCard(3, leadDepotReq, Resource.SERVANT, LeaderType.DEPOT);
        depotLead1.setPlayed(true);
        me.getHandLeaderCards().add(depotLead1);

        List<ResourcePosition> inputClient = new ArrayList<>();
        inputClient.add(new ResourcePosition(1, Resource.STONE, Place.WAREHOUSE, NumOfShelf.DEPOT_ONE));
        inputClient.add(new ResourcePosition(1, Resource.STONE, Place.WAREHOUSE, NumOfShelf.DEPOT_ONE));
        inputClient.add(new ResourcePosition(1, Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        inputClient.add(new ResourcePosition(1, Resource.STONE, Place.CHEST, null));
        inputClient.add(new ResourcePosition(1, Resource.SERVANT, Place.WAREHOUSE, NumOfShelf.DEPOT_TWO));
        inputClient.add(new ResourcePosition(1, Resource.SERVANT, Place.CHEST, null));
        inputClient.add(new ResourcePosition(1, Resource.SERVANT, Place.CHEST, null));
        inputClient.add(new ResourcePosition(1, Resource.SERVANT, Place.CHEST, null));

        Warehouse myWarehouse = me.getBoard().getWarehouse();
        Chest myChest = me.getBoard().getChest();
        myWarehouse.addDepot(Resource.STONE);
        myWarehouse.addDepot(Resource.SERVANT);
        List<ResourcePosition> resInWarehouse = new ArrayList<>();
        resInWarehouse.add(new ResourcePosition(1, Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        resInWarehouse.add(new ResourcePosition(1, Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        resInWarehouse.add(new ResourcePosition(1, Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        resInWarehouse.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(1, Resource.STONE, Place.WAREHOUSE, NumOfShelf.DEPOT_ONE));
        resInWarehouse.add(new ResourcePosition(1, Resource.STONE, Place.WAREHOUSE, NumOfShelf.DEPOT_ONE));
        resInWarehouse.add(new ResourcePosition(1, Resource.SERVANT, Place.WAREHOUSE, NumOfShelf.DEPOT_TWO));
        myWarehouse.incrementResource(resInWarehouse);
        List<ResourcePosition> resInChest = new ArrayList<>();
        resInChest.add(new ResourcePosition(4, Resource.SHIELD, Place.CHEST, null));
        resInChest.add(new ResourcePosition(4, Resource.SERVANT, Place.CHEST, null));
        resInChest.add(new ResourcePosition(4, Resource.STONE, Place.CHEST, null));
        resInChest.add(new ResourcePosition(4, Resource.COIN, Place.CHEST, null));

        myChest.incrementResource(resInChest);

        List<LeaderEffect> leaderEffects = new ArrayList<>();
        leaderEffects.add(new DepotEffect(Resource.STONE));
        leaderEffects.add(new DepotEffect(Resource.SERVANT));

        game.drawDevCard(Colour.YELLOW, 3);
        game.drawDevCard(Colour.YELLOW, 3);
        game.drawDevCard(Colour.YELLOW, 3);

        Action buyDevCard = new BuyDevCard(3, Colour.YELLOW, DevSpaceSlot.ONE, inputClient, leaderEffects);

        try {
            buyDevCard.checkAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 2);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 3);
            assertTrue(myWarehouse.getShelf(NumOfShelf.DEPOT_ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.DEPOT_ONE).getQuantity() == 2);
            assertTrue(myWarehouse.getShelf(NumOfShelf.DEPOT_TWO).getResource() == Resource.SERVANT && myWarehouse.getShelf(NumOfShelf.DEPOT_TWO).getQuantity() == 1);
            assertEquals(4, myChest.getAvailability(Resource.SHIELD));
            assertEquals(4, myChest.getAvailability(Resource.STONE));
            assertEquals(4, myChest.getAvailability(Resource.COIN));
            assertEquals(4, myChest.getAvailability(Resource.SERVANT));

            buyDevCard.doAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 0);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 2);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 3);
            assertTrue(myWarehouse.getShelf(NumOfShelf.DEPOT_ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.DEPOT_ONE).getQuantity() == 0);
            assertTrue(myWarehouse.getShelf(NumOfShelf.DEPOT_TWO).getResource() == Resource.SERVANT && myWarehouse.getShelf(NumOfShelf.DEPOT_TWO).getQuantity() == 0);
            assertEquals(4, myChest.getAvailability(Resource.SHIELD));
            assertEquals(3, myChest.getAvailability(Resource.STONE));
            assertEquals(4, myChest.getAvailability(Resource.COIN));
            assertEquals(1, myChest.getAvailability(Resource.SERVANT));
        } catch (WrongActionException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void buyDevCard3TestDiscountDepotLead() {
        Game game = new Game();
        Player me = new Player("Gianluca", game);
        for (DevDeck dd : game.getDevDecks())
            orderDeck(dd);

        assertTrue(me.getBoard().getDevSpace().checkPlace(1, DevSpaceSlot.ONE));
        me.getBoard().getDevSpace().addCard(game.drawDevCard(Colour.BLUE, 1), DevSpaceSlot.ONE);
        assertTrue(me.getBoard().getDevSpace().checkPlace(2, DevSpaceSlot.ONE));
        me.getBoard().getDevSpace().addCard(game.drawDevCard(Colour.YELLOW, 2), DevSpaceSlot.ONE);

        List<DevCard> leadProdReq = new ArrayList<>();
        LeaderCard discountLead = new LeaderCard(4, leadProdReq, Resource.STONE, LeaderType.DISCOUNT);
        discountLead.setPlayed(true);
        me.getHandLeaderCards().add(discountLead);

        LeaderCard depotLead = new LeaderCard(4, leadProdReq, Resource.STONE, LeaderType.DEPOT);
        depotLead.setPlayed(true);
        me.getHandLeaderCards().add(depotLead);

        List<ResourcePosition> inputClient = new ArrayList<>();
        inputClient.add(new ResourcePosition(1, Resource.STONE, Place.WAREHOUSE, NumOfShelf.DEPOT_ONE));
        inputClient.add(new ResourcePosition(1, Resource.STONE, Place.WAREHOUSE, NumOfShelf.DEPOT_ONE));
        inputClient.add(new ResourcePosition(1, Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        inputClient.add(new ResourcePosition(1, Resource.STONE, Place.CHEST, null));
        inputClient.add(new ResourcePosition(1, Resource.STONE, Place.CHEST, null));

        Warehouse myWarehouse = me.getBoard().getWarehouse();
        Chest myChest = me.getBoard().getChest();
        myWarehouse.addDepot(Resource.STONE);
        List<ResourcePosition> resInWarehouse = new ArrayList<>();
        resInWarehouse.add(new ResourcePosition(1, Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        resInWarehouse.add(new ResourcePosition(1, Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        resInWarehouse.add(new ResourcePosition(1, Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        resInWarehouse.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(1, Resource.STONE, Place.WAREHOUSE, NumOfShelf.DEPOT_ONE));
        resInWarehouse.add(new ResourcePosition(1, Resource.STONE, Place.WAREHOUSE, NumOfShelf.DEPOT_ONE));
        myWarehouse.incrementResource(resInWarehouse);
        List<ResourcePosition> resInChest = new ArrayList<>();
        resInChest.add(new ResourcePosition(4, Resource.SHIELD, Place.CHEST, null));
        resInChest.add(new ResourcePosition(4, Resource.SERVANT, Place.CHEST, null));
        resInChest.add(new ResourcePosition(4, Resource.STONE, Place.CHEST, null));
        resInChest.add(new ResourcePosition(4, Resource.COIN, Place.CHEST, null));

        myChest.incrementResource(resInChest);

        List<LeaderEffect> leaderEffects = new ArrayList<>();
        leaderEffects.add(new DiscountEffect(Resource.STONE));
        leaderEffects.add(new DepotEffect(Resource.STONE));

        //game.drawDevCard(Colour.YELLOW, 3);
        //game.drawDevCard(Colour.YELLOW, 3);
        //game.drawDevCard(Colour.YELLOW, 3);

        Action buyDevCard = new BuyDevCard(3, Colour.YELLOW, DevSpaceSlot.ONE, inputClient, leaderEffects);

        try {
            buyDevCard.checkAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 2);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 3);
            assertTrue(myWarehouse.getShelf(NumOfShelf.DEPOT_ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.DEPOT_ONE).getQuantity() == 2);
            assertEquals(4, myChest.getAvailability(Resource.SHIELD));
            assertEquals(4, myChest.getAvailability(Resource.STONE));
            assertEquals(4, myChest.getAvailability(Resource.COIN));
            assertEquals(4, myChest.getAvailability(Resource.SERVANT));

            buyDevCard.doAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 0);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 2);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 3);
            assertTrue(myWarehouse.getShelf(NumOfShelf.DEPOT_ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.DEPOT_ONE).getQuantity() == 0);
            assertEquals(4, myChest.getAvailability(Resource.SHIELD));
            assertEquals(2, myChest.getAvailability(Resource.STONE));
            assertEquals(4, myChest.getAvailability(Resource.COIN));
            assertEquals(4, myChest.getAvailability(Resource.SERVANT));
        } catch (WrongActionException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    private void orderDeck(DevDeck dd) {
        DevCard[] compare = new DevCard[4];
        int level = dd.getLevel();
        List <DevCard> devCards = dd.getCards();
        for(DevCard dc : devCards) {
            switch (dc.getVictoryPoints() - (level - 1) * 4) {
                case 1:
                    compare[0] = dc;
                    break;
                case 2:
                    compare[1] = dc;
                    break;
                case 3:
                    compare[2] = dc;
                    break;
                case 4:
                    compare[3] = dc;
                    break;
            }
        }                                                                                                               //compare contains the pointer to the elements in devCards according to their victory points.
        for(int i = 0; i < compare.length; i++){
            devCards.remove(compare[i]);
            devCards.add(i, compare[i]);
        }
    }

}

