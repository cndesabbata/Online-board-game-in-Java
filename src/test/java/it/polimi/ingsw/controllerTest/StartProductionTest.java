package it.polimi.ingsw.controllerTest;
import it.polimi.ingsw.server.controller.*;
import it.polimi.ingsw.server.controller.leaders.DepotEffect;
import it.polimi.ingsw.server.controller.leaders.LeaderEffect;
import it.polimi.ingsw.server.controller.leaders.ProductionEffect;
import it.polimi.ingsw.messages.actions.StartProduction;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.gameboard.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import java.util.ArrayList;
import java.util.List;


public class StartProductionTest {

    @Test
    public void testStartProductionBoard(){
        Game game = new Game();

        List <ResourcePosition> inputClient = new ArrayList<>();
        inputClient.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        inputClient.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        List <ResourcePosition> outputClient = new ArrayList<>();
        outputClient.add(new ResourcePosition(Resource.COIN, Place.CHEST, null));

        List <LeaderEffect> leaderEffects = new ArrayList<>();
        Action production = new StartProduction(inputClient, outputClient, leaderEffects);

        Player me = new Player("Gianluca", game);
        game.addPlayer(me);

        Warehouse myWarehouse = me.getBoard().getWarehouse();
        Chest myChest = me.getBoard().getChest();
        List <ResourcePosition> resInWarehouse = new ArrayList<>();
        resInWarehouse.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        resInWarehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        resInWarehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        myWarehouse.incrementResource(resInWarehouse);
        List <ResourcePosition> resInChest = new ArrayList<>();
        resInChest.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.SERVANT, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.SERVANT, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        myChest.incrementResource(resInChest);

        assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
        assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 1);
        assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 3);
        assertEquals(2, myChest.getAvailability(Resource.SHIELD));
        assertEquals(2, myChest.getAvailability(Resource.STONE));
        assertEquals(0, myChest.getAvailability(Resource.COIN));
        assertEquals(2, myChest.getAvailability(Resource.SERVANT));

        try {
            production.checkAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 3);
            assertEquals(2, myChest.getAvailability(Resource.SHIELD));
            assertEquals(2, myChest.getAvailability(Resource.STONE));
            assertEquals(0, myChest.getAvailability(Resource.COIN));
            assertEquals(2, myChest.getAvailability(Resource.SERVANT));


            production.doAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 0);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 0);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 3);
            assertEquals(2, myChest.getAvailability(Resource.SHIELD));
            assertEquals(2, myChest.getAvailability(Resource.STONE));
            assertEquals(1, myChest.getAvailability(Resource.COIN));
            assertEquals(2, myChest.getAvailability(Resource.SERVANT));

        }
        catch (WrongActionException e){
            System.out.println(e.getMessage());
            assertTrue(false);
        }

    }

    @Test
    public void testStartProductionBoardDevCard(){
        Game game = new Game();

        List <ResourceQuantity> requirements = new ArrayList<>();
        requirements.add(new ResourceQuantity(3, Resource.SHIELD));
        requirements.add(new ResourceQuantity(2, Resource.SERVANT));
        List <ResourceQuantity> input = new ArrayList<>();
        input.add(new ResourceQuantity(1, Resource.SHIELD));
        input.add(new ResourceQuantity(1, Resource.COIN));
        List <ResourceQuantity> output = new ArrayList<>();
        output.add(new ResourceQuantity(3, Resource.STONE));
        DevCard devCard = new DevCard(requirements, 6,1,Colour.GREEN, input, output);

        List <ResourcePosition> inputClient = new ArrayList<>();
        inputClient.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        inputClient.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        inputClient.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        inputClient.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        List <ResourcePosition> outputClient = new ArrayList<>();
        outputClient.add(new ResourcePosition(Resource.COIN, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));


        List <LeaderEffect> leaderEffects = new ArrayList<>();
        List <Integer> slots = new ArrayList<>();
        slots.add(1);
        Action production = new StartProduction(slots, inputClient, outputClient, leaderEffects);

        Player me = new Player("Gianluca", game);
        game.addPlayer(me);
        me.getBoard().getDevSpace().addCard(devCard, DevSpaceSlot.TWO);

        Warehouse myWarehouse = me.getBoard().getWarehouse();
        Chest myChest = me.getBoard().getChest();
        List <ResourcePosition> resInWarehouse = new ArrayList<>();
        resInWarehouse.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        resInWarehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        resInWarehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        myWarehouse.incrementResource(resInWarehouse);
        List <ResourcePosition> resInChest = new ArrayList<>();
        resInChest.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.SERVANT, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.SERVANT, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        myChest.incrementResource(resInChest);

        assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
        assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 1);
        assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 3);
        assertEquals(2, myChest.getAvailability(Resource.SHIELD));
        assertEquals(2, myChest.getAvailability(Resource.STONE));
        assertEquals(0, myChest.getAvailability(Resource.COIN));
        assertEquals(2, myChest.getAvailability(Resource.SERVANT));

        try {
            production.checkAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 3);
            assertEquals(2, myChest.getAvailability(Resource.SHIELD));
            assertEquals(2, myChest.getAvailability(Resource.STONE));
            assertEquals(0, myChest.getAvailability(Resource.COIN));
            assertEquals(2, myChest.getAvailability(Resource.SERVANT));


            production.doAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 0);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 2);
            assertEquals(0, myChest.getAvailability(Resource.SHIELD));
            assertEquals(5, myChest.getAvailability(Resource.STONE));
            assertEquals(1, myChest.getAvailability(Resource.COIN));
            assertEquals(2, myChest.getAvailability(Resource.SERVANT));

        }
        catch (WrongActionException e){
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    public void testStartProductionBoardTwoDevCards(){
        Game game = new Game();

        List <ResourceQuantity> requirements = new ArrayList<>();
        requirements.add(new ResourceQuantity(3, Resource.SHIELD));
        requirements.add(new ResourceQuantity(2, Resource.SERVANT));
        List <ResourceQuantity> input = new ArrayList<>();
        input.add(new ResourceQuantity(1, Resource.SHIELD));
        input.add(new ResourceQuantity(1, Resource.COIN));
        List <ResourceQuantity> output = new ArrayList<>();
        output.add(new ResourceQuantity(3, Resource.STONE));
        DevCard devCard = new DevCard(requirements, 6,1,Colour.GREEN, input, output);

        List <ResourceQuantity> requirements2 = new ArrayList<>();
        requirements2.add(new ResourceQuantity(3, Resource.SHIELD));
        requirements2.add(new ResourceQuantity(2, Resource.SERVANT));
        List <ResourceQuantity> input2 = new ArrayList<>();
        input2.add(new ResourceQuantity(2, Resource.COIN));
        List <ResourceQuantity> output2 = new ArrayList<>();
        output2.add(new ResourceQuantity(2, Resource.SHIELD));
        output2.add(new ResourceQuantity(1, Resource.STONE));
        DevCard devCard2 = new DevCard(requirements2, 5,1,Colour.PURPLE, input2, output2);

        List <ResourcePosition> inputClient = new ArrayList<>();
        inputClient.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        inputClient.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        inputClient.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        inputClient.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        inputClient.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        inputClient.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        List <ResourcePosition> outputClient = new ArrayList<>();
        outputClient.add(new ResourcePosition(Resource.COIN, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));

        List <LeaderEffect> leaderEffects = new ArrayList<>();
        List <Integer> slots = new ArrayList<>();
        slots.add(1);
        slots.add(2);
        Action production = new StartProduction(slots, inputClient, outputClient, leaderEffects);

        Player me = new Player("Gianluca", game);
        game.addPlayer(me);
        me.getBoard().getDevSpace().addCard(devCard, DevSpaceSlot.TWO);
        me.getBoard().getDevSpace().addCard(devCard2, DevSpaceSlot.THREE);

        Warehouse myWarehouse = me.getBoard().getWarehouse();
        Chest myChest = me.getBoard().getChest();
        List <ResourcePosition> resInWarehouse = new ArrayList<>();
        resInWarehouse.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        resInWarehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        resInWarehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        myWarehouse.incrementResource(resInWarehouse);
        List <ResourcePosition> resInChest = new ArrayList<>();
        resInChest.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.SERVANT, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.SERVANT, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        myChest.incrementResource(resInChest);

        assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
        assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 1);
        assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 3);
        assertEquals(2, myChest.getAvailability(Resource.SHIELD));
        assertEquals(2, myChest.getAvailability(Resource.STONE));
        assertEquals(0, myChest.getAvailability(Resource.COIN));
        assertEquals(2, myChest.getAvailability(Resource.SERVANT));

        try {
            production.checkAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 3);
            assertEquals(2, myChest.getAvailability(Resource.SHIELD));
            assertEquals(2, myChest.getAvailability(Resource.STONE));
            assertEquals(0, myChest.getAvailability(Resource.COIN));
            assertEquals(2, myChest.getAvailability(Resource.SERVANT));


            production.doAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 0);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 0);
            assertEquals(2, myChest.getAvailability(Resource.SHIELD));
            assertEquals(6, myChest.getAvailability(Resource.STONE));
            assertEquals(1, myChest.getAvailability(Resource.COIN));
            assertEquals(2, myChest.getAvailability(Resource.SERVANT));

        }
        catch (WrongActionException e){
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    public void testStartProductionBoardTwoDevCardsProdLeader(){
        Game game = new Game();

        List <ResourceQuantity> requirements = new ArrayList<>();
        requirements.add(new ResourceQuantity(3, Resource.SHIELD));
        requirements.add(new ResourceQuantity(2, Resource.SERVANT));
        List <ResourceQuantity> input = new ArrayList<>();
        input.add(new ResourceQuantity(1, Resource.SHIELD));
        input.add(new ResourceQuantity(1, Resource.COIN));
        List <ResourceQuantity> output = new ArrayList<>();
        output.add(new ResourceQuantity(3, Resource.STONE));
        DevCard devCard = new DevCard(requirements, 6,1,Colour.GREEN, input, output);

        List <ResourceQuantity> requirements2 = new ArrayList<>();
        requirements2.add(new ResourceQuantity(3, Resource.SHIELD));
        requirements2.add(new ResourceQuantity(2, Resource.SERVANT));
        List <ResourceQuantity> input2 = new ArrayList<>();
        input2.add(new ResourceQuantity(2, Resource.COIN));
        List <ResourceQuantity> output2 = new ArrayList<>();
        output2.add(new ResourceQuantity(2, Resource.SHIELD));
        output2.add(new ResourceQuantity(1, Resource.STONE));
        DevCard devCard2 = new DevCard(requirements2, 5,1,Colour.PURPLE, input2, output2);

        List<DevCard> leadProdReq = new ArrayList<>();
        leadProdReq.add(devCard);
        LeaderCard prodLead = new LeaderCard(4, leadProdReq, Resource.SERVANT, LeaderType.PRODUCT);
        prodLead.setPlayed(true);

        List <ResourcePosition> inputClient = new ArrayList<>();
        inputClient.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        inputClient.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        inputClient.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        inputClient.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        inputClient.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        inputClient.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        List <ResourcePosition> outputClient = new ArrayList<>();
        outputClient.add(new ResourcePosition(Resource.COIN, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));

        List <LeaderEffect> leaderEffects = new ArrayList<>();
        leaderEffects.add(new ProductionEffect(new ResourcePosition(Resource.SERVANT, Place.WAREHOUSE, NumOfShelf.ONE),
                                               new ResourcePosition(Resource.COIN, Place.CHEST, null)));
        List <Integer> slots = new ArrayList<>();
        slots.add(1);
        slots.add(2);
        Action production = new StartProduction(slots, inputClient, outputClient, leaderEffects);

        Player me = new Player("Gianluca", game);
        game.addPlayer(me);
        me.getHandLeaderCards().add(prodLead);
        me.getBoard().getDevSpace().addCard(devCard, DevSpaceSlot.TWO);
        me.getBoard().getDevSpace().addCard(devCard2, DevSpaceSlot.THREE);

        Warehouse myWarehouse = me.getBoard().getWarehouse();
        Chest myChest = me.getBoard().getChest();
        List <ResourcePosition> resInWarehouse = new ArrayList<>();
        resInWarehouse.add(new ResourcePosition(Resource.SERVANT, Place.WAREHOUSE, NumOfShelf.ONE));
        resInWarehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        resInWarehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        myWarehouse.incrementResource(resInWarehouse);
        List <ResourcePosition> resInChest = new ArrayList<>();
        resInChest.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.SERVANT, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.SERVANT, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        myChest.incrementResource(resInChest);

        assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.SERVANT && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
        assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 1);
        assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 3);
        assertEquals(2, myChest.getAvailability(Resource.SHIELD));
        assertEquals(2, myChest.getAvailability(Resource.STONE));
        assertEquals(0, myChest.getAvailability(Resource.COIN));
        assertEquals(2, myChest.getAvailability(Resource.SERVANT));

        try {
            production.checkAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.SERVANT && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 3);
            assertEquals(2, myChest.getAvailability(Resource.SHIELD));
            assertEquals(2, myChest.getAvailability(Resource.STONE));
            assertEquals(0, myChest.getAvailability(Resource.COIN));
            assertEquals(2, myChest.getAvailability(Resource.SERVANT));
            assertEquals(1, me.getBoard().getItinerary().getPosition());

            production.doAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 0);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 0);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 0);
            assertEquals(3, myChest.getAvailability(Resource.SHIELD));
            assertEquals(5, myChest.getAvailability(Resource.STONE));
            assertEquals(2, myChest.getAvailability(Resource.COIN));
            assertEquals(2, myChest.getAvailability(Resource.SERVANT));
            assertEquals(1, me.getBoard().getItinerary().getPosition());

        }
        catch (WrongActionException e){
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    public void testStartProductionBoardTwoDevCardsDepotProdLeader(){
        Game game = new Game();

        List <ResourceQuantity> requirements = new ArrayList<>();
        requirements.add(new ResourceQuantity(3, Resource.SHIELD));
        requirements.add(new ResourceQuantity(2, Resource.SERVANT));
        List <ResourceQuantity> input = new ArrayList<>();
        input.add(new ResourceQuantity(1, Resource.SHIELD));
        input.add(new ResourceQuantity(1, Resource.STONE));
        List <ResourceQuantity> output = new ArrayList<>();
        output.add(new ResourceQuantity(3, Resource.STONE));
        DevCard devCard = new DevCard(requirements, 6,1,Colour.GREEN, input, output);

        List <ResourceQuantity> requirements2 = new ArrayList<>();
        requirements2.add(new ResourceQuantity(3, Resource.SHIELD));
        requirements2.add(new ResourceQuantity(2, Resource.SERVANT));
        List <ResourceQuantity> input2 = new ArrayList<>();
        input2.add(new ResourceQuantity(2, Resource.STONE));
        List <ResourceQuantity> output2 = new ArrayList<>();
        output2.add(new ResourceQuantity(2, Resource.SHIELD));
        output2.add(new ResourceQuantity(1, Resource.STONE));
        DevCard devCard2 = new DevCard(requirements2, 5,1,Colour.PURPLE, input2, output2);

        List<DevCard> leadProdReq = new ArrayList<>();
        leadProdReq.add(devCard);
        LeaderCard prodLead = new LeaderCard(4, leadProdReq, Resource.COIN, LeaderType.PRODUCT);
        prodLead.setPlayed(true);

        List<DevCard> leadDepotReq = new ArrayList<>();
        leadDepotReq.add(devCard);
        LeaderCard depotLead = new LeaderCard(3, leadDepotReq, Resource.COIN, LeaderType.DEPOT);
        depotLead.setPlayed(true);

        List <ResourcePosition> inputClient = new ArrayList<>();
        inputClient.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        inputClient.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        inputClient.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        inputClient.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        inputClient.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        inputClient.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        List <ResourcePosition> outputClient = new ArrayList<>();
        outputClient.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.SERVANT, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        outputClient.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));

        List <Integer> slots = new ArrayList<>();
        slots.add(0);
        slots.add(1);
        List <LeaderEffect> leaderEffects = new ArrayList<>();
        leaderEffects.add(new ProductionEffect(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE),
                                               new ResourcePosition(Resource.COIN, Place.CHEST, null)));
        leaderEffects.add(new DepotEffect(Resource.COIN));

        Action production = new StartProduction(slots, inputClient, outputClient, leaderEffects);

        Player me = new Player("Gianluca", game);
        me.getHandLeaderCards().add(prodLead);
        me.getHandLeaderCards().add(depotLead);
        game.addPlayer(me);
        me.getBoard().getDevSpace().addCard(devCard, DevSpaceSlot.ONE);
        me.getBoard().getDevSpace().addCard(devCard2, DevSpaceSlot.TWO);
        List <ResourcePosition> resInWarehouse = new ArrayList<>();
        resInWarehouse.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        resInWarehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        resInWarehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        me.getBoard().getWarehouse().incrementResource(resInWarehouse);
        List <ResourcePosition> resInChest = new ArrayList<>();
        resInChest.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.SERVANT, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.SERVANT, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        me.getBoard().getChest().incrementResource(resInChest);

        Warehouse myWarehouse = me.getBoard().getWarehouse();
        Chest myChest = me.getBoard().getChest();
        Itinerary myItinerary = me.getBoard().getItinerary();
        assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
        assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 1);
        assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 3);
        assertEquals(2, myChest.getAvailability(Resource.SHIELD));
        assertEquals(2, myChest.getAvailability(Resource.STONE));
        assertEquals(0, myChest.getAvailability(Resource.COIN));
        assertEquals(2, myChest.getAvailability(Resource.SERVANT));
        assertTrue(myItinerary.getPosition() == 0);

        try {
            production.checkAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 3);
            assertTrue(myWarehouse.getShelf(NumOfShelf.DEPOT_ONE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.DEPOT_ONE).getQuantity() == 0);
            assertEquals(2, myChest.getAvailability(Resource.SHIELD));
            assertEquals(2, myChest.getAvailability(Resource.STONE));
            assertEquals(0, myChest.getAvailability(Resource.COIN));
            assertEquals(2, myChest.getAvailability(Resource.SERVANT));
            assertEquals(1, myItinerary.getPosition());


            production.doAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 0);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 0);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 0);
            assertTrue(myWarehouse.getShelf(NumOfShelf.DEPOT_ONE).getResource() == Resource.COIN && myWarehouse.getShelf(NumOfShelf.DEPOT_ONE).getQuantity() == 0);
            assertEquals(4, myChest.getAvailability(Resource.SHIELD));
            assertEquals(4, myChest.getAvailability(Resource.STONE));
            assertEquals(1, myChest.getAvailability(Resource.COIN));
            assertEquals(3, myChest.getAvailability(Resource.SERVANT));
            assertEquals(1, myItinerary.getPosition());

        }
        catch (WrongActionException e){
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }
}
