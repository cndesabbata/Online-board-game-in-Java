package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.messages.actions.MoveResources;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.controller.*;
import it.polimi.ingsw.server.model.gameboard.NumOfShelf;
import it.polimi.ingsw.server.model.gameboard.Warehouse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MoveResourcesTest {

    @Test
    public void moveResTest1(){
        Game game = new Game();
        Player me = new Player("Gianluca", game);

        Warehouse myWarehouse = me.getBoard().getWarehouse();
        List <ResourcePosition> resInWarehouse = new ArrayList<>();
        resInWarehouse.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        resInWarehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        resInWarehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        myWarehouse.incrementResource(resInWarehouse);

        Action move = new MoveResources(NumOfShelf.TWO, NumOfShelf.THREE, 2);

        try{
            move.checkAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 2);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 0);
            move.doAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 0);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 2);
        }
        catch (WrongActionException e){
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void moveResTest2(){
        Game game = new Game();
        Player me = new Player("Gianluca", game);

        Warehouse myWarehouse = me.getBoard().getWarehouse();
        List <ResourcePosition> resInWarehouse = new ArrayList<>();
        resInWarehouse.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        resInWarehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        resInWarehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        myWarehouse.incrementResource(resInWarehouse);

        Action move = new MoveResources(NumOfShelf.ONE, NumOfShelf.THREE, 1);

        try{
            move.checkAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 2);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 0);
            move.doAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 0);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 2);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 1);
        }
        catch (WrongActionException e){
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    public void moveResTest4(){
        Game game = new Game();
        Player me = new Player("Gianluca", game);

        Warehouse myWarehouse = me.getBoard().getWarehouse();
        List <ResourcePosition> resInWarehouse = new ArrayList<>();
        resInWarehouse.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        resInWarehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.THREE));
        myWarehouse.incrementResource(resInWarehouse);

        Action move = new MoveResources(NumOfShelf.THREE, NumOfShelf.TWO, 1);

        try{
            move.checkAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 2);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 0);
            move.doAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 0);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 2);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 1);
        }
        catch (WrongActionException e){
            System.out.println(e.getMessage());
            assertTrue(true);
        }
    }

    @Test
    public void moveResTest3(){
        Game game = new Game();
        Player me = new Player("Gianluca", game);

        Warehouse myWarehouse = me.getBoard().getWarehouse();
        List <ResourcePosition> resInWarehouse = new ArrayList<>();
        resInWarehouse.add(new ResourcePosition(Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        resInWarehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.THREE));
        myWarehouse.incrementResource(resInWarehouse);

        Action move = new MoveResources(NumOfShelf.THREE, NumOfShelf.TWO, 3);

        try{
            move.checkAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 2);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 0);
            move.doAction(me);
            assertTrue(myWarehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.EMPTY && myWarehouse.getShelf(NumOfShelf.ONE).getQuantity() == 0);
            assertTrue(myWarehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && myWarehouse.getShelf(NumOfShelf.TWO).getQuantity() == 2);
            assertTrue(myWarehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.STONE && myWarehouse.getShelf(NumOfShelf.THREE).getQuantity() == 1);
        }
        catch (WrongActionException e){
            System.out.println(e.getMessage());
            assertTrue(true);
        }
    }
}
