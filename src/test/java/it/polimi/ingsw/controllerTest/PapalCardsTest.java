package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.controller.multiplayer.MultiPlayerController;
import it.polimi.ingsw.server.controller.singleplayer.SinglePlayerController;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.gameboard.CardStatus;
import it.polimi.ingsw.server.model.gameboard.Itinerary;
import it.polimi.ingsw.server.serverNetwork.Server;
import org.junit.Test;

import static org.junit.Assert.*;

public class PapalCardsTest {

    @Test
    public void TurnedAndDiscardedPapalCardTest(){
        GameController gameController = new MultiPlayerController(new Server());
        Game game = gameController.getGame();
        Player me =  new Player("Gianluca", game);
        Player you =  new Player("Andrea", game);
        game.addPlayer(me);
        game.addPlayer(you);
        Itinerary myItinerary = me.getBoard().getItinerary();
        Itinerary yourItinerary = you.getBoard().getItinerary();

        myItinerary.updatePosition(3, null, false);
        yourItinerary.updatePosition(6, null, false);

        assertEquals(3, myItinerary.getPosition());
        assertEquals(6,yourItinerary.getPosition());

        yourItinerary.updatePosition(2, null, false);
        gameController.checkAllPapalReports();

        assertSame(myItinerary.getCardStatus()[0], CardStatus.DISCARDED);
        assertSame(myItinerary.getCardStatus()[1], CardStatus.FACE_DOWN);
        assertSame(myItinerary.getCardStatus()[2], CardStatus.FACE_DOWN);

        assertSame(yourItinerary.getCardStatus()[0], CardStatus.FACE_UP);
        assertSame(yourItinerary.getCardStatus()[1], CardStatus.FACE_DOWN);
        assertSame(yourItinerary.getCardStatus()[2], CardStatus.FACE_DOWN);
    }

    @Test
    public void TurnedAndTurnedPapalCardTest(){
        GameController gameController = new MultiPlayerController(new Server());
        Game game = gameController.getGame();
        Player me =  new Player("Gianluca", game);
        Player you =  new Player("Andrea", game);
        game.addPlayer(me);
        game.addPlayer(you);
        Itinerary myItinerary = me.getBoard().getItinerary();
        Itinerary yourItinerary = you.getBoard().getItinerary();

        myItinerary.updatePosition(5, null, false);
        yourItinerary.updatePosition(7, null, false);

        assertEquals(5, myItinerary.getPosition());
        assertEquals(7,yourItinerary.getPosition());

        yourItinerary.updatePosition(2, null, false);
        gameController.checkAllPapalReports();

        assertSame(myItinerary.getCardStatus()[0], CardStatus.FACE_UP);
        assertSame(myItinerary.getCardStatus()[1], CardStatus.FACE_DOWN);
        assertSame(myItinerary.getCardStatus()[2], CardStatus.FACE_DOWN);

        assertSame(yourItinerary.getCardStatus()[0], CardStatus.FACE_UP);
        assertSame(yourItinerary.getCardStatus()[1], CardStatus.FACE_DOWN);
        assertSame(yourItinerary.getCardStatus()[2], CardStatus.FACE_DOWN);
    }

    @Test
    public void notTurnedAndNotTurnedPapalCardTest(){
        GameController gameController = new MultiPlayerController(new Server());
        Game game = gameController.getGame();
        Player me =  new Player("Gianluca", game);
        Player you =  new Player("Andrea", game);
        game.addPlayer(me);
        game.addPlayer(you);
        Itinerary myItinerary = me.getBoard().getItinerary();
        Itinerary yourItinerary = you.getBoard().getItinerary();

        myItinerary.updatePosition(3, null, false);
        yourItinerary.updatePosition(6, null, false);

        assertEquals(3, myItinerary.getPosition());
        assertEquals(6,yourItinerary.getPosition());

        yourItinerary.updatePosition(1, null, false);
        gameController.checkAllPapalReports();

        assertSame(myItinerary.getCardStatus()[0], CardStatus.FACE_DOWN);
        assertSame(myItinerary.getCardStatus()[1], CardStatus.FACE_DOWN);
        assertSame(myItinerary.getCardStatus()[2], CardStatus.FACE_DOWN);

        assertSame(yourItinerary.getCardStatus()[0], CardStatus.FACE_DOWN);
        assertSame(yourItinerary.getCardStatus()[1], CardStatus.FACE_DOWN);
        assertSame(yourItinerary.getCardStatus()[2], CardStatus.FACE_DOWN);
    }

    @Test
    public void singlePlayerDiscardedTest(){
        GameController gameController = new SinglePlayerController(new Server());
        Game game = gameController.getGame();
        Player me =  new Player("Gianluca", game);
        game.addPlayer(me);
        Itinerary myItinerary = me.getBoard().getItinerary();

        myItinerary.setBlackCrossPosition(0);
        myItinerary.updatePosition(3, 6, false);


        assertEquals(3, myItinerary.getPosition());
        assertEquals(6,(int) myItinerary.getBlackCrossPosition());

        myItinerary.updatePosition(0, 2, false);
        gameController.checkAllPapalReports();

        assertSame(myItinerary.getCardStatus()[0], CardStatus.DISCARDED);
        assertSame(myItinerary.getCardStatus()[1], CardStatus.FACE_DOWN);
        assertSame(myItinerary.getCardStatus()[2], CardStatus.FACE_DOWN);

    }

    @Test
    public void singlePlayerTurnedTest1(){
        GameController gameController = new SinglePlayerController(new Server());
        Game game = gameController.getGame();
        Player me =  new Player("Gianluca", game);
        game.addPlayer(me);
        Itinerary myItinerary = me.getBoard().getItinerary();

        myItinerary.setBlackCrossPosition(0);
        myItinerary.updatePosition(5, 6, false);


        assertEquals(5, myItinerary.getPosition());
        assertEquals(6,(int) myItinerary.getBlackCrossPosition());

        myItinerary.updatePosition(0, 2, false);
        gameController.checkAllPapalReports();

        assertSame(myItinerary.getCardStatus()[0], CardStatus.FACE_UP);
        assertSame(myItinerary.getCardStatus()[1], CardStatus.FACE_DOWN);
        assertSame(myItinerary.getCardStatus()[2], CardStatus.FACE_DOWN);

    }

    @Test
    public void singlePlayerTurnedTest2(){
        GameController gameController = new SinglePlayerController(new Server());
        Game game = gameController.getGame();
        Player me =  new Player("Gianluca", game);
        game.addPlayer(me);
        Itinerary myItinerary = me.getBoard().getItinerary();

        myItinerary.setBlackCrossPosition(0);
        myItinerary.updatePosition(5, 6, false);


        assertEquals(5, myItinerary.getPosition());
        assertEquals(6,(int) myItinerary.getBlackCrossPosition());

        myItinerary.updatePosition(3, null, false);
        gameController.checkAllPapalReports();

        assertSame(myItinerary.getCardStatus()[0], CardStatus.FACE_UP);
        assertSame(myItinerary.getCardStatus()[1], CardStatus.FACE_DOWN);
        assertSame(myItinerary.getCardStatus()[2], CardStatus.FACE_DOWN);

    }

    @Test
    public void singlePlayerNotTurnedTest(){
        GameController gameController = new SinglePlayerController(new Server());
        Game game = gameController.getGame();
        Player me =  new Player("Gianluca", game);
        game.addPlayer(me);
        Itinerary myItinerary = me.getBoard().getItinerary();

        myItinerary.setBlackCrossPosition(0);
        myItinerary.updatePosition(5, 6, false);


        assertEquals(5, myItinerary.getPosition());
        assertEquals(6,(int) myItinerary.getBlackCrossPosition());

        myItinerary.updatePosition(2, 1, false);
        gameController.checkAllPapalReports();

        assertSame(myItinerary.getCardStatus()[0], CardStatus.FACE_DOWN);
        assertSame(myItinerary.getCardStatus()[1], CardStatus.FACE_DOWN);
        assertSame(myItinerary.getCardStatus()[2], CardStatus.FACE_DOWN);

    }

}
