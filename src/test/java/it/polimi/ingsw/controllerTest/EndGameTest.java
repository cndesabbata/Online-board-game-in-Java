package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.server.controller.multiplayer.MultiPlayerController;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.controller.*;
import it.polimi.ingsw.server.model.gameboard.CardStatus;
import it.polimi.ingsw.server.model.gameboard.NumOfShelf;
import it.polimi.ingsw.server.serverNetwork.Server;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;


public class EndGameTest {
    @Test
    public void FirstEndgameTest() {
        GameController gameController = new MultiPlayerController(new Server());
        Game game = gameController.getGame();
        Player player1 = new Player("Andri", game);
        game.addPlayer(player1);
        Player player2 = new Player("Gianlu", game);
        game.addPlayer(player2);
        Player player3 = new Player("Desa", game);
        game.addPlayer(player3);
        player1.getBoard().getItinerary().updatePosition(17);
        List<ResourcePosition> player1Warehouse = new ArrayList<>();
        player1Warehouse.add(new ResourcePosition(Resource.SERVANT, Place.WAREHOUSE, NumOfShelf.ONE));
        player1Warehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.TWO));
        player1Warehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.TWO));
        player1Warehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.THREE));
        player1Warehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.THREE));
        player1.getBoard().getWarehouse().incrementResource(player1Warehouse);
        List<ResourcePosition> player1Chest = new ArrayList<>();
        player1Chest.add(new ResourcePosition(3, Resource.STONE, Place.CHEST));
        player1Chest.add(new ResourcePosition(2, Resource.SHIELD, Place.CHEST));
        player1.getBoard().getChest().incrementResource(player1Chest);
        LeaderCard leaderCard11 = new LeaderCard(3, null, Resource.SERVANT, LeaderType.DEPOT, null);
        player1.getPlayedLeaderCards().add(leaderCard11);
        LeaderCard leaderCard12 = new LeaderCard(2, null, Resource.SHIELD, LeaderType.DISCOUNT, null);
        player1.getPlayedLeaderCards().add(leaderCard12);
        player1.getBoard().getItinerary().setCardStatus(CardStatus.FACE_UP, 0);
        player1.getBoard().getItinerary().setCardStatus(CardStatus.FACE_UP, 1);
        player1.getBoard().getItinerary().setCardStatus(CardStatus.DISCARDED, 2);
        List<List<DevCard>> player1Ds= player1.getBoard().getDevSpace().getCards();
        player1Ds.get(0).add(new DevCard(null, 1, 1, Colour.GREEN, null, null, null));
        player1Ds.get(0).add(new DevCard(null, 6, 2, Colour.PURPLE, null, null, null));
        player1Ds.get(1).add(new DevCard(null, 4, 1, Colour.PURPLE, null, null, null));
        player1Ds.get(1).add(new DevCard(null, 5, 2, Colour.GREEN, null, null, null));
        player1Ds.get(1).add(new DevCard(null, 12, 3, Colour.YELLOW, null, null, null));
        player1Ds.get(2).add(new DevCard(null, 3, 1, Colour.PURPLE, null, null, null));
        gameController.endGame();
        assertTrue(game.getWinners().size() == 1 && game.getWinners().get(0).getNickname().equals("Andri"));
    }

    @Test
    public void DrawEndGameTest() {
        GameController gameController = new MultiPlayerController(new Server());
        Game game = gameController.getGame();
        Player player1 = new Player("Andri", game);
        game.addPlayer(player1);
        Player player2 = new Player("Desa", game);
        game.addPlayer(player2);
        Player player3 = new Player("Gianlu", game);
        game.addPlayer(player3);
        player1.getBoard().getItinerary().updatePosition(17);
        player2.getBoard().getItinerary().updatePosition(17);
        List<ResourcePosition> player1Warehouse = new ArrayList<>();
        player1Warehouse.add(new ResourcePosition(Resource.SERVANT, Place.WAREHOUSE, NumOfShelf.ONE));
        player1Warehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.TWO));
        player1Warehouse.add(new ResourcePosition(Resource.COIN, Place.WAREHOUSE, NumOfShelf.TWO));
        player1Warehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.THREE));
        player1Warehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.THREE));
        player1.getBoard().getWarehouse().incrementResource(player1Warehouse);
        player2.getBoard().getWarehouse().incrementResource(player1Warehouse);
        List<ResourcePosition> player1Chest = new ArrayList<>();
        player1Chest.add(new ResourcePosition(3, Resource.STONE, Place.CHEST));
        player1Chest.add(new ResourcePosition(2, Resource.SHIELD, Place.CHEST));
        player1.getBoard().getChest().incrementResource(player1Chest);
        player2.getBoard().getChest().incrementResource(player1Chest);
        LeaderCard leaderCard11 = new LeaderCard(3, null, Resource.SERVANT, LeaderType.DEPOT, null);
        player1.getPlayedLeaderCards().add(leaderCard11);
        player2.getPlayedLeaderCards().add(leaderCard11);
        LeaderCard leaderCard12 = new LeaderCard(2, null, Resource.SHIELD, LeaderType.DISCOUNT, null);
        player1.getPlayedLeaderCards().add(leaderCard12);
        player2.getPlayedLeaderCards().add(leaderCard12);
        player1.getBoard().getItinerary().setCardStatus(CardStatus.FACE_UP, 0);
        player1.getBoard().getItinerary().setCardStatus(CardStatus.FACE_UP, 1);
        player1.getBoard().getItinerary().setCardStatus(CardStatus.DISCARDED, 2);
        player2.getBoard().getItinerary().setCardStatus(CardStatus.FACE_UP, 0);
        player2.getBoard().getItinerary().setCardStatus(CardStatus.FACE_UP, 1);
        player2.getBoard().getItinerary().setCardStatus(CardStatus.DISCARDED, 2);
        List<List<DevCard>> player1Ds= player1.getBoard().getDevSpace().getCards();
        List<List<DevCard>> player2Ds= player2.getBoard().getDevSpace().getCards();
        player1Ds.get(0).add(new DevCard(null, 1, 1, Colour.GREEN, null, null, null));
        player1Ds.get(0).add(new DevCard(null, 6, 2, Colour.PURPLE, null, null, null));
        player1Ds.get(1).add(new DevCard(null, 4, 1, Colour.PURPLE, null, null, null));
        player1Ds.get(1).add(new DevCard(null, 5, 2, Colour.GREEN, null, null, null));
        player1Ds.get(1).add(new DevCard(null, 12, 3, Colour.YELLOW, null, null, null));
        player1Ds.get(2).add(new DevCard(null, 3, 1, Colour.PURPLE, null, null, null));
        player2Ds.get(0).add(new DevCard(null, 1, 1, Colour.GREEN, null, null, null));
        player2Ds.get(0).add(new DevCard(null, 6, 2, Colour.PURPLE, null, null, null));
        player2Ds.get(1).add(new DevCard(null, 4, 1, Colour.PURPLE, null, null, null));
        player2Ds.get(1).add(new DevCard(null, 5, 2, Colour.GREEN, null, null, null));
        player2Ds.get(1).add(new DevCard(null, 12, 3, Colour.YELLOW, null, null, null));
        player2Ds.get(2).add(new DevCard(null, 3, 1, Colour.PURPLE, null, null, null));
        gameController.endGame();
        assertTrue(game.getWinners().size() == 2 && game.getWinners().get(0).getNickname().equals("Andri") && game.getWinners().get(1).getNickname().equals("Desa"));
    }
}
