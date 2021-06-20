package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.controller.multiplayer.MultiPlayerController;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.gameboard.CardStatus;
import it.polimi.ingsw.server.model.gameboard.Itinerary;
import it.polimi.ingsw.server.serverNetwork.Server;
import org.junit.Test;

import static org.junit.Assert.*;

public class PapalCardsTest {

    @Test
    public void TurnedAndDiscardedPapalCard(){
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
    public void TurnedAndTurnedPapalCard(){
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
    public void notTurnedAndNotTurnedPapalCard(){
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
    
}
