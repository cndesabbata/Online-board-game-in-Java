package it.polimi.ingsw.modelTest;

import static org.junit.Assert.assertTrue;

import it.polimi.ingsw.controller.Place;
import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WarehouseTest {

    @Test
    public void testIncrementResource() {
        Warehouse warehouse = new Warehouse(3);
        List<ResourcePosition> inputRes = new ArrayList<>();
        inputRes.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.ONE));
        inputRes.add(new ResourcePosition(1, Resource.SERVANT, Place.WAREHOUSE, NumOfShelf.THREE));
        inputRes.add(new ResourcePosition(1, Resource.SERVANT, Place.WAREHOUSE, NumOfShelf.THREE));
        inputRes.add(new ResourcePosition(1, Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        try {
            warehouse.checkIncrement(inputRes);
        }
        catch (WrongActionException e) { System.out.println("Non si può proseguire");
        }
        warehouse.incrementResource(inputRes);
        assertTrue(warehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.COIN && warehouse.getShelf(NumOfShelf.ONE).getQuantity() == 1);
        assertTrue(warehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.SHIELD && warehouse.getShelf(NumOfShelf.TWO).getQuantity() == 1);
        assertTrue(warehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.SERVANT && warehouse.getShelf(NumOfShelf.THREE).getQuantity() == 2);
    }

    public void testCheckIncrement() {
    }

    public void testDecrementResource() {
    }

    public void testCheckDecrement() {
    }

    public void testMoveResource() {
    }

    public void testCheckMove() {
    }

    public void testAddDepot() {
    }

    public void testCheckDepot() {
    }

}
