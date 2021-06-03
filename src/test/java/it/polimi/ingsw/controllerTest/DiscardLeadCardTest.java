package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.messages.actions.DiscardLeadCard;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DiscardLeadCardTest {

    @Test
    public void discardLeadCardDevCardReqTest(){
        Game game = new Game();

        List<ResourceQuantity> requirements = new ArrayList<>();
        requirements.add(new ResourceQuantity(3, Resource.SHIELD));
        requirements.add(new ResourceQuantity(2, Resource.SERVANT));
        List <ResourceQuantity> input = new ArrayList<>();
        input.add(new ResourceQuantity(1, Resource.SHIELD));
        input.add(new ResourceQuantity(1, Resource.STONE));
        List <ResourceQuantity> output = new ArrayList<>();
        output.add(new ResourceQuantity(3, Resource.STONE));
        DevCard devCard = new DevCard(requirements, 6,1, Colour.GREEN, input, output, null);

        List<DevCard> leadProdReq = new ArrayList<>();
        leadProdReq.add(devCard);
        LeaderCard prodLead = new LeaderCard(4, leadProdReq, Resource.COIN, LeaderType.PRODUCT, null);

        Player me = new Player("Gianluca", game);
        me.getHandLeaderCards().add(prodLead);
        game.addPlayer(me);

        Action discardLeadCard = new DiscardLeadCard(1);

        try {
            discardLeadCard.checkAction(me);
            assertTrue(me.getHandLeaderCards().contains(prodLead));
            discardLeadCard.doAction(me);
            assertFalse(me.getHandLeaderCards().contains(prodLead));
        }
        catch (WrongActionException e){
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void discardLeadCardResReqTest(){
        Game game = new Game();

        List<ResourceQuantity> requirements = new ArrayList<>();
        requirements.add(new ResourceQuantity(3, Resource.SHIELD));
        requirements.add(new ResourceQuantity(2, Resource.SERVANT));
        LeaderCard prodLead = new LeaderCard(requirements, 4, Resource.COIN, LeaderType.PRODUCT, null);

        Player me = new Player("Gianluca", game);
        me.getHandLeaderCards().add(prodLead);
        game.addPlayer(me);

        Action discardLeadCard = new DiscardLeadCard(1);

        try {
            discardLeadCard.checkAction(me);
            assertTrue(me.getHandLeaderCards().contains(prodLead));
            discardLeadCard.doAction(me);
            assertFalse(me.getHandLeaderCards().contains(prodLead));
        }
        catch (WrongActionException e){
            System.out.println(e.getMessage());
            fail();
        }
    }


    @Test
    public void discardPlayedLeadCardResReqTest(){
        Game game = new Game();

        List<ResourceQuantity> requirements = new ArrayList<>();
        requirements.add(new ResourceQuantity(3, Resource.SHIELD));
        requirements.add(new ResourceQuantity(2, Resource.SERVANT));
        LeaderCard prodLead = new LeaderCard(requirements, 4, Resource.COIN, LeaderType.PRODUCT, null);

        Player me = new Player("Gianluca", game);
        me.getPlayedLeaderCards().add(prodLead);
        game.addPlayer(me);

        assertTrue(me.hasPlayedLeaderCard(LeaderType.PRODUCT, Resource.COIN));

        Action discardLeadCard = new DiscardLeadCard(1);

        try {
            discardLeadCard.checkAction(me);
            assertTrue(me.getHandLeaderCards().contains(prodLead));
            discardLeadCard.doAction(me);
            assertFalse(me.getHandLeaderCards().contains(prodLead));
        }
        catch (WrongActionException e){
            System.out.println(e.getMessage());
            assertTrue(true);
        }
    }

    @Test
    public void discardDoubleLeadCardResReqTest(){
        Game game = new Game();

        List<ResourceQuantity> requirements = new ArrayList<>();
        requirements.add(new ResourceQuantity(3, Resource.SHIELD));
        requirements.add(new ResourceQuantity(2, Resource.SERVANT));
        LeaderCard prodLead = new LeaderCard(requirements, 4, Resource.COIN, LeaderType.PRODUCT, null);

        List<ResourceQuantity> requirements1 = new ArrayList<>();
        requirements1.add(new ResourceQuantity(3, Resource.SHIELD));
        requirements1.add(new ResourceQuantity(2, Resource.SERVANT));
        LeaderCard depotLead = new LeaderCard(requirements1, 4, Resource.COIN, LeaderType.DEPOT, null);

        Player me = new Player("Gianluca", game);
        me.getHandLeaderCards().add(prodLead);
        me.getHandLeaderCards().add(depotLead);
        game.addPlayer(me);

        Action discardLeadCard = new DiscardLeadCard(1);

        try {
            discardLeadCard.checkAction(me);
            assertTrue(me.getHandLeaderCards().contains(prodLead));
            assertTrue(me.getHandLeaderCards().contains(depotLead));
            discardLeadCard.doAction(me);
            assertFalse(me.getHandLeaderCards().contains(prodLead));
            assertTrue(me.getHandLeaderCards().contains(depotLead));
        }
        catch (WrongActionException e){
            System.out.println(e.getMessage());
            fail();
        }
    }
}
