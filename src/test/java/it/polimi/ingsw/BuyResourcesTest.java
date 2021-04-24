package it.polimi.ingsw;

import static org.junit.Assert.assertTrue;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.BuyResources;
import it.polimi.ingsw.controller.Place;
import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BuyResourcesTest {
    @Test
    public void BuyResourceTest() {
        Game game = new Game();
        GameBoard board = new GameBoard();
        String nickname = "Andrea";
        Player player = new Player(nickname, 1, game);
        List<ResourcePosition> boughtResources = new ArrayList<>();
        Marble[][] disposition = {{Marble.PURPLE, Marble.BLUE, Marble.PURPLE, Marble.GREY},
                {Marble.WHITE, Marble.RED, Marble.WHITE, Marble.WHITE},
                {Marble.WHITE, Marble.GREY, Marble.YELLOW, Marble.YELLOW}};
        game.getMarket().setFakeDisposition(disposition);
        game.getMarket().setFakeExternal(Marble.BLUE);
        boughtResources.add(new ResourcePosition(1, Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        boughtResources.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.TWO));
        boughtResources.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        Action action = new BuyResources(new ArrayList<>(), 3, MarketSelection.ROW, boughtResources);
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 4; j++) {
                System.out.print(disposition[i][j] + ", ");
            }
            System.out.print("\n");
        }
        System.out.println(game.getMarket().getExternal());
        try {
            action.checkAction(player);
            action.doAction(player);
        } catch (WrongActionException e){
            e.printStackTrace();
        }
        Marble[][] newDisposition = game.getMarket().getDisposition();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 4; j++) {
                System.out.print(newDisposition[i][j] + ", ");
            }
            System.out.print("\n");
        }
        for(ResourceQuantity resourceQuantity : player.getBoard().getWarehouse().getWarehouse()){
            System.out.println("Resource: " + resourceQuantity.getResource() + " Quantity: " + resourceQuantity.getQuantity());
        }
        assertTrue(true);
    }
}
