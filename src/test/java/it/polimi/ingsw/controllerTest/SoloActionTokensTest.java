package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.controller.multiplayer.MultiPlayerController;
import it.polimi.ingsw.server.controller.singleplayer.*;
import it.polimi.ingsw.server.model.Colour;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.serverNetwork.Server;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SoloActionTokensTest {

    @Test
    public void UpdateAndShuffleTest(){
        SinglePlayerController gameController = new SinglePlayerController(new Server());
        Game game = gameController.getGame();
        Player me = new Player("Gianluca", game);
        game.addPlayer(me);
        me.getBoard().getItinerary().setBlackCrossPosition(0);
        SoloActionToken LorenzoAction = new UpdateAndShuffle(gameController);

        assertEquals(0, (int) me.getBoard().getItinerary().getBlackCrossPosition());

        LorenzoAction.doSoloAction();

        assertEquals(1, (int) me.getBoard().getItinerary().getBlackCrossPosition());

    }

    @Test
    public void UpdateItineraryTest(){
        SinglePlayerController gameController = new SinglePlayerController(new Server());
        Game game = gameController.getGame();
        Player me = new Player("Gianluca", game);
        game.addPlayer(me);
        me.getBoard().getItinerary().setBlackCrossPosition(0);
        SoloActionToken LorenzoAction = new UpdateItinerary(gameController);

        assertEquals(0, (int) me.getBoard().getItinerary().getBlackCrossPosition());

        LorenzoAction.doSoloAction();

        assertEquals(2, (int) me.getBoard().getItinerary().getBlackCrossPosition());

    }

    @Test
    public void DiscardDevCard1LTest(){
        SinglePlayerController gameController = new SinglePlayerController(new Server());
        Game game = gameController.getGame();
        Player me = new Player("Gianluca", game);
        game.addPlayer(me);
        me.getBoard().getItinerary().setBlackCrossPosition(0);
        for(Colour c : Colour.values()) {
            SoloActionToken LorenzoAction = new DiscardDevCard(gameController, c);

            assertEquals(4, game.getDevDecks()[c.ordinal()].getCards().size());

            LorenzoAction.doSoloAction();

            assertEquals(2, game.getDevDecks()[c.ordinal()].getCards().size());
        }

    }

    @Test
    public void DiscardDevCard2LTest(){
        SinglePlayerController gameController = new SinglePlayerController(new Server());
        Game game = gameController.getGame();
        Player me = new Player("Gianluca", game);
        game.addPlayer(me);
        me.getBoard().getItinerary().setBlackCrossPosition(0);
        for(Colour c : Colour.values()){
            for(int i = 0; i < 4; i++)
                game.drawDevCard(c, 1);
        }
        for(Colour c : Colour.values()) {
            SoloActionToken LorenzoAction = new DiscardDevCard(gameController, c);

            assertEquals(4, game.getDevDecks()[4 + c.ordinal()].getCards().size());

            LorenzoAction.doSoloAction();

            assertEquals(2, game.getDevDecks()[4 + c.ordinal()].getCards().size());
        }

    }

    @Test
    public void DiscardDevCard3LTest(){
        SinglePlayerController gameController = new SinglePlayerController(new Server());
        Game game = gameController.getGame();
        Player me = new Player("Gianluca", game);
        game.addPlayer(me);
        me.getBoard().getItinerary().setBlackCrossPosition(0);
        for(int j = 1; j < 3; j++) {
            for (Colour c : Colour.values()) {
                for (int i = 0; i < 4; i++)
                    game.drawDevCard(c, j);
            }
        }
        for(Colour c : Colour.values()) {
            SoloActionToken LorenzoAction = new DiscardDevCard(gameController, c);

            assertEquals(4, game.getDevDecks()[8 + c.ordinal()].getCards().size());

            LorenzoAction.doSoloAction();

            assertEquals(2, game.getDevDecks()[8 + c.ordinal()].getCards().size());
        }

    }

    @Test
    public void DiscardOneDevCardTest(){
        SinglePlayerController gameController = new SinglePlayerController(new Server());
        Game game = gameController.getGame();
        Player me = new Player("Gianluca", game);
        game.addPlayer(me);
        me.getBoard().getItinerary().setBlackCrossPosition(0);
        for(int i = 0; i < 3; i++)
            game.drawDevCard(Colour.GREEN, 1);

        SoloActionToken LorenzoAction = new DiscardDevCard(gameController, Colour.GREEN);

        assertEquals(1, game.getDevDecks()[Colour.GREEN.ordinal()].getCards().size());

        LorenzoAction.doSoloAction();

        assertEquals(0, game.getDevDecks()[Colour.GREEN.ordinal()].getCards().size());


    }

}
