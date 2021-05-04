package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.server.controller.Place;
import it.polimi.ingsw.messages.actions.*;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.gameboard.DevSpaceSlot;
import it.polimi.ingsw.server.model.gameboard.NumOfShelf;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PlayLeadCardTest {

    @Test
    public void playLeadCardDevCardReqTest(){
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
        input2.add(new ResourceQuantity(1, Resource.SHIELD));
        input2.add(new ResourceQuantity(1, Resource.STONE));
        List <ResourceQuantity> output2 = new ArrayList<>();
        output2.add(new ResourceQuantity(3, Resource.STONE));
        DevCard devCard2 = new DevCard(requirements, 6,1, Colour.PURPLE, input2, output2);

        List<DevCard> leadProdReq = new ArrayList<>();
        leadProdReq.add(devCard);
        LeaderCard prodLead = new LeaderCard(4, leadProdReq, Resource.COIN, LeaderType.PRODUCT);
        prodLead.setPlayed(false);

        Player me = new Player("Gianluca", game);
        me.getHandLeaderCards().add(prodLead);
        game.addPlayer(me);
        me.getBoard().getDevSpace().addCard(devCard, DevSpaceSlot.ONE);

        Action action = new PlayLeadCard(1);
        try {
            action.checkAction(me);
            action.doAction(me);
            assertTrue(me.hasPlayedLeaderCard(LeaderType.PRODUCT, Resource.COIN));
        } catch (WrongActionException e)
        {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    public void playLeadCardResReq(){
        Game game = new Game();

        List<ResourceQuantity> leadRes = new ArrayList<>();
        leadRes.add(new ResourceQuantity(2, Resource.COIN));
        leadRes.add(new ResourceQuantity(2, Resource.STONE));
        LeaderCard prodLead = new LeaderCard(leadRes, 4, Resource.COIN, LeaderType.PRODUCT);
        prodLead.setPlayed(false);

        Player me = new Player("Gianluca", game);
        me.getHandLeaderCards().add(prodLead);
        game.addPlayer(me);
        Action action = new PlayLeadCard(1);

        List <ResourcePosition> resInWarehouse = new ArrayList<>();
        resInWarehouse.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        resInWarehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        resInWarehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        me.getBoard().getWarehouse().incrementResource(resInWarehouse);
        List <ResourcePosition> resInChest = new ArrayList<>();
        resInChest.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.SHIELD, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.SERVANT, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.SERVANT, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        resInChest.add(new ResourcePosition(Resource.STONE, Place.CHEST, null));
        me.getBoard().getChest().incrementResource(resInChest);

        try {
            action.checkAction(me);
            assertFalse(me.hasPlayedLeaderCard(LeaderType.PRODUCT, Resource.COIN));
            action.doAction(me);
            assertTrue(me.hasPlayedLeaderCard(LeaderType.PRODUCT, Resource.COIN));
            assertTrue(me.getBoard().getWarehouse().getShelf(NumOfShelf.ONE).getResource() == Resource.STONE && me.getBoard().getWarehouse().getShelf(NumOfShelf.ONE).getQuantity() == 1);
            assertTrue(me.getBoard().getWarehouse().getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && me.getBoard().getWarehouse().getShelf(NumOfShelf.TWO).getQuantity() == 1);
            assertTrue(me.getBoard().getWarehouse().getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && me.getBoard().getWarehouse().getShelf(NumOfShelf.THREE).getQuantity() == 3);
            assertEquals(2, me.getBoard().getChest().getAvailability(Resource.STONE));
            assertEquals(2, me.getBoard().getChest().getAvailability(Resource.SHIELD));
            assertEquals(2, me.getBoard().getChest().getAvailability(Resource.SERVANT));
            assertEquals(0, me.getBoard().getChest().getAvailability(Resource.COIN));

        } catch (WrongActionException e)
        {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }
}
