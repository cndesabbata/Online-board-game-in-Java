package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.gameboard.NumOfShelf;
import it.polimi.ingsw.server.Server;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;


public class EndGameTest {
    @Test
    public void FirstEndgameTest() {
        GameController gameController = new GameController(new Server());
        Game game = gameController.getGame();
        Player player1 = new Player("Andri", game);
        game.addPlayer(player1);
        Player player2 = new Player("Gianlu", game);
        game.addPlayer(player2);
        Player player3 = new Player("Desa", game);
        game.addPlayer(player3);
        player1.getBoard().getItinerary().updatePosition(17);
        List<ResourcePosition> player1Warehouse = new ArrayList<>();
        player1Warehouse.add(new ResourcePosition(1, Resource.SERVANT, Place.WAREHOUSE, NumOfShelf.ONE));
        player1Warehouse.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.TWO));
        player1Warehouse.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.TWO));
        player1Warehouse.add(new ResourcePosition(1, Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.THREE));
        player1Warehouse.add(new ResourcePosition(1, Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.THREE));
        player1.getBoard().getWarehouse().incrementResource(player1Warehouse);
        List<ResourcePosition> player1Chest = new ArrayList<>();
        player1Chest.add(new ResourcePosition(3, Resource.STONE, Place.CHEST));
        player1Chest.add(new ResourcePosition(2, Resource.SHIELD, Place.CHEST));
        player1.getBoard().getChest().incrementResource(player1Chest);
        LeaderCard leaderCard11 = new LeaderCard(3, null, Resource.SERVANT, LeaderType.DEPOT);
        leaderCard11.setPlayed(true);
        player1.addFakeLeaderCard(leaderCard11);
        LeaderCard leaderCard12 = new LeaderCard(2, null, Resource.SHIELD, LeaderType.DISCOUNT);
        leaderCard12.setPlayed(true);
        player1.addFakeLeaderCard(leaderCard12);
        player1.getBoard().getItinerary().setCardStatus(CardStatus.FACE_UP, 0);
        player1.getBoard().getItinerary().setCardStatus(CardStatus.FACE_UP, 1);
        player1.getBoard().getItinerary().setCardStatus(CardStatus.DISCARDED, 2);
        List<List<DevCard>> player1Ds= player1.getBoard().getDevSpace().getCards();
        player1Ds.get(0).add(new DevCard(null, 1, 1, Colour.GREEN, null, null));
        player1Ds.get(0).add(new DevCard(null, 6, 2, Colour.PURPLE, null, null));
        player1Ds.get(1).add(new DevCard(null, 4, 1, Colour.PURPLE, null, null));
        player1Ds.get(1).add(new DevCard(null, 5, 2, Colour.GREEN, null, null));
        player1Ds.get(1).add(new DevCard(null, 12, 3, Colour.YELLOW, null, null));
        player1Ds.get(2).add(new DevCard(null, 3, 1, Colour.PURPLE, null, null));
        gameController.endGame();
        assertTrue(game.getWinners().size() == 1 && game.getWinners().get(0).getNickname().equals("Andri"));
    }
}
