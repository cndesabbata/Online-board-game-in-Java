package it.polimi.ingsw.controllerTest;

import static org.junit.Assert.assertTrue;

import it.polimi.ingsw.server.controller.*;
import it.polimi.ingsw.server.controller.leaders.LeaderEffect;
import it.polimi.ingsw.server.controller.leaders.MarbleEffect;
import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.messages.actions.BuyResources;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.gameboard.NumOfShelf;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BuyResourcesTest {
    @Test
    public void buyResourceFirstTest() {
        Game game = new Game();
        String nickname = "Andrea";
        Player player = new Player(nickname, game);
        List<ResourcePosition> boughtResources = new ArrayList<>();
        Marble[][] disposition = {{Marble.PURPLE, Marble.BLUE, Marble.PURPLE, Marble.GREY},
                {Marble.WHITE, Marble.RED, Marble.WHITE, Marble.WHITE},
                {Marble.WHITE, Marble.GREY, Marble.YELLOW, Marble.YELLOW}};
        game.getMarket().setFakeDisposition(disposition);
        game.getMarket().setFakeExternal(Marble.BLUE);
        boughtResources.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        boughtResources.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        boughtResources.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        Action action = new BuyResources(new ArrayList<>(), 3, MarketSelection.ROW, boughtResources);
        try {
            action.checkAction(player);
            action.doAction(player);
        } catch (WrongActionException e){
            e.printStackTrace();
        }
        Marble[][] newDisposition = game.getMarket().getDisposition();
        Marble[][] expDisposition = {{Marble.PURPLE, Marble.BLUE, Marble.PURPLE, Marble.GREY},
                {Marble.WHITE, Marble.RED, Marble.WHITE, Marble.WHITE},
                {Marble.GREY, Marble.YELLOW, Marble.YELLOW, Marble.BLUE}};
        List<ResourceQuantity> expWarehouse = new ArrayList<>();
        expWarehouse.add(new ResourceQuantity(1, Resource.STONE));
        expWarehouse.add(new ResourceQuantity(0, Resource.EMPTY));
        expWarehouse.add(new ResourceQuantity(2, Resource.COIN));
        assertTrue(expectedDisposition(newDisposition, expDisposition, game.getMarket().getExternal(), Marble.WHITE));
        assertTrue(expectedWarehouse(player.getBoard().getWarehouse().getWarehouse(), expWarehouse));
    }

    @Test
    public void buyResourceOneLeaderTest() {
        Game game = new Game();
        String nickname = "Andrea";
        Player player = new Player(nickname, game);
        List<ResourcePosition> boughtResources = new ArrayList<>();
        List<ResourcePosition> extraRes = new ArrayList<>();
        List<LeaderEffect> leaderEffects = new ArrayList<>();
        Marble[][] disposition = {{Marble.PURPLE, Marble.BLUE, Marble.PURPLE, Marble.GREY},
                {Marble.WHITE, Marble.RED, Marble.WHITE, Marble.WHITE},
                {Marble.WHITE, Marble.GREY, Marble.YELLOW, Marble.YELLOW}};
        game.getMarket().setFakeDisposition(disposition);
        game.getMarket().setFakeExternal(Marble.BLUE);
        extraRes.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.TWO));
        LeaderCard marbleCard = new LeaderCard(3, null, Resource.STONE, LeaderType.MARBLE);
        marbleCard.setPlayed(true);
        player.addLeaderCard(marbleCard);
        LeaderEffect leaderEffect = new MarbleEffect(1, Resource.STONE, extraRes);
        leaderEffects.add(leaderEffect);
        boughtResources.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.TWO));
        boughtResources.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        boughtResources.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        Action action = new BuyResources(leaderEffects, 3, MarketSelection.ROW, boughtResources);
        try {
            action.checkAction(player);
            action.doAction(player);
        } catch (WrongActionException e){
            e.printStackTrace();
        }
        Marble[][] newDisposition = game.getMarket().getDisposition();
        Marble[][] expDisposition = {{Marble.PURPLE, Marble.BLUE, Marble.PURPLE, Marble.GREY},
                {Marble.WHITE, Marble.RED, Marble.WHITE, Marble.WHITE},
                {Marble.GREY, Marble.YELLOW, Marble.YELLOW, Marble.BLUE}};
        List<ResourceQuantity> expWarehouse = new ArrayList<>();
        expWarehouse.add(new ResourceQuantity(0, Resource.EMPTY));
        expWarehouse.add(new ResourceQuantity(2, Resource.STONE));
        expWarehouse.add(new ResourceQuantity(2, Resource.COIN));
        assertTrue(expectedDisposition(newDisposition, expDisposition, game.getMarket().getExternal(), Marble.WHITE));
        assertTrue(expectedWarehouse(player.getBoard().getWarehouse().getWarehouse(), expWarehouse));
    }

    @Test
    public void buyResourceTwoLeaderTest() {
        Game game = new Game();
        String nickname = "Andrea";
        Player player = new Player(nickname, game);
        List<ResourcePosition> boughtResources = new ArrayList<>();
        List<ResourcePosition> extraRes = new ArrayList<>();
        List<LeaderEffect> leaderEffects = new ArrayList<>();
        Marble[][] disposition = {{Marble.PURPLE, Marble.BLUE, Marble.PURPLE, Marble.GREY},
                {Marble.WHITE, Marble.RED, Marble.WHITE, Marble.WHITE},
                {Marble.WHITE, Marble.GREY, Marble.YELLOW, Marble.YELLOW}};
        game.getMarket().setFakeDisposition(disposition);
        game.getMarket().setFakeExternal(Marble.BLUE);
        extraRes.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.TWO));
        LeaderCard marbleCard1 = new LeaderCard(3, null, Resource.STONE, LeaderType.MARBLE);
        LeaderCard marbleCard2 = new LeaderCard(3, null, Resource.SERVANT, LeaderType.MARBLE);
        marbleCard1.setPlayed(true);
        marbleCard2.setPlayed(true);
        player.addLeaderCard(marbleCard1);
        player.addLeaderCard(marbleCard2);
        LeaderEffect leaderEffect1 = new MarbleEffect(1, Resource.STONE, extraRes);
        LeaderEffect leaderEffect2 = new MarbleEffect(0, Resource.SERVANT, new ArrayList<>());
        leaderEffects.add(leaderEffect1);
        leaderEffects.add(leaderEffect2);
        boughtResources.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.TWO));
        boughtResources.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        boughtResources.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        Action action = new BuyResources(leaderEffects, 3, MarketSelection.ROW, boughtResources);
        try {
            action.checkAction(player);
            action.doAction(player);
        } catch (WrongActionException e){
            e.printStackTrace();
        }
        Marble[][] newDisposition = game.getMarket().getDisposition();
        Marble[][] expDisposition = {{Marble.PURPLE, Marble.BLUE, Marble.PURPLE, Marble.GREY},
                {Marble.WHITE, Marble.RED, Marble.WHITE, Marble.WHITE},
                {Marble.GREY, Marble.YELLOW, Marble.YELLOW, Marble.BLUE}};
        List<ResourceQuantity> expWarehouse = new ArrayList<>();
        expWarehouse.add(new ResourceQuantity(0, Resource.EMPTY));
        expWarehouse.add(new ResourceQuantity(2, Resource.STONE));
        expWarehouse.add(new ResourceQuantity(2, Resource.COIN));
        assertTrue(expectedDisposition(newDisposition, expDisposition, game.getMarket().getExternal(), Marble.WHITE));
        assertTrue(expectedWarehouse(player.getBoard().getWarehouse().getWarehouse(), expWarehouse));
    }

    @Test
    public  void  buyResourcesExceptionFirstTest(){
        Game game = new Game();
        String nickname = "Andrea";
        Player player = new Player(nickname, game);
        List<ResourcePosition> boughtResources = new ArrayList<>();
        Marble[][] disposition = {{Marble.PURPLE, Marble.BLUE, Marble.PURPLE, Marble.GREY},
                {Marble.WHITE, Marble.RED, Marble.WHITE, Marble.WHITE},
                {Marble.WHITE, Marble.GREY, Marble.YELLOW, Marble.YELLOW}};
        game.getMarket().setFakeDisposition(disposition);
        game.getMarket().setFakeExternal(Marble.BLUE);
        boughtResources.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        boughtResources.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        boughtResources.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.TWO));
        Action action = new BuyResources(new ArrayList<>(), 3, MarketSelection.ROW, boughtResources);
        try {
            action.checkAction(player);
            Assert.fail();
        } catch (WrongActionException e) {

        }
    }



    @Test
    public  void  buyResourcesExceptionSecondTest(){
        Game game = new Game();
        String nickname = "Andrea";
        Player player = new Player(nickname, game);
        List<ResourcePosition> boughtResources = new ArrayList<>();
        Marble[][] disposition = {{Marble.PURPLE, Marble.BLUE, Marble.PURPLE, Marble.GREY},
                {Marble.WHITE, Marble.RED, Marble.WHITE, Marble.WHITE},
                {Marble.WHITE, Marble.GREY, Marble.YELLOW, Marble.YELLOW}};
        game.getMarket().setFakeDisposition(disposition);
        game.getMarket().setFakeExternal(Marble.BLUE);
        boughtResources.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.THREE));
        boughtResources.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.ONE));
        boughtResources.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.ONE));
        Action action = new BuyResources(new ArrayList<>(), 3, MarketSelection.ROW, boughtResources);
        try {
            action.checkAction(player);
            Assert.fail();
        } catch (WrongActionException e) {

        }
    }

    public boolean expectedDisposition(Marble[][] disposition1, Marble[][] disposition2, Marble external1, Marble external2) {
        if(external1 != external2)
            return false;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 4; j++) {
                if(disposition1[i][j] != disposition2[i][j])
                    return false;
            }
        }
        return true;
    }

    public boolean expectedWarehouse(List<ResourceQuantity> warehouse1, List<ResourceQuantity> warehouse2) {
        if(warehouse1.size() != warehouse2.size())
            return false;
        for(int i = 0; i < warehouse1.size(); i++) {
            if(warehouse1.get(i).getResource() != warehouse2.get(i).getResource() ||
            warehouse1.get(i).getQuantity() != warehouse2.get(i).getQuantity())
                return false;
        }
        return true;
    }
}
