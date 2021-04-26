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
        warehouse.addDepot(Resource.COIN);
        warehouse.addDepot(Resource.SHIELD);
        List<ResourcePosition> resInWarehouse = new ArrayList<>();
        resInWarehouse.add(new ResourcePosition(1,Resource.COIN, Place.WAREHOUSE, NumOfShelf.TWO));
        warehouse.incrementResource(resInWarehouse);
        List<ResourcePosition> inputRes = new ArrayList<>();
        inputRes.add(new ResourcePosition(1, Resource.STONE, Place.TRASH_CAN, null));
        inputRes.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.DEPOT_ONE));
        inputRes.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.DEPOT_ONE));
        inputRes.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.TWO));
        inputRes.add(new ResourcePosition(1, Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.DEPOT_TWO));
        try {
            warehouse.checkIncrement(inputRes);
            warehouse.incrementResource(inputRes);
            assertTrue(warehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.EMPTY && warehouse.getShelf(NumOfShelf.ONE).getQuantity() == 0);
            assertTrue(warehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.COIN && warehouse.getShelf(NumOfShelf.TWO).getQuantity() == 2);
            assertTrue(warehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.EMPTY && warehouse.getShelf(NumOfShelf.THREE).getQuantity() == 0);
            assertTrue(warehouse.getShelf(NumOfShelf.DEPOT_ONE).getResource() == Resource.COIN && warehouse.getShelf(NumOfShelf.DEPOT_ONE).getQuantity() == 2);
            assertTrue(warehouse.getShelf(NumOfShelf.DEPOT_TWO).getResource() == Resource.SHIELD && warehouse.getShelf(NumOfShelf.DEPOT_TWO).getQuantity() == 1);

        }
        catch (WrongActionException e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    public void testDecrementResource() {
        Warehouse warehouse = new Warehouse(3);
        warehouse.addDepot(Resource.COIN);
        warehouse.addDepot(Resource.SHIELD);
        List<ResourcePosition> resInWarehouse = new ArrayList<>();
        resInWarehouse.add(new ResourcePosition(1,Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        resInWarehouse.add(new ResourcePosition(1,Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        resInWarehouse.add(new ResourcePosition(1,Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(1,Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(1,Resource.COIN, Place.WAREHOUSE, NumOfShelf.DEPOT_ONE));
        resInWarehouse.add(new ResourcePosition(1,Resource.COIN, Place.WAREHOUSE, NumOfShelf.DEPOT_ONE));
        resInWarehouse.add(new ResourcePosition(1,Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.DEPOT_TWO));
        try {
            warehouse.checkIncrement(resInWarehouse);
        }
        catch (WrongActionException e) {
            System.out.println(e.getMessage());
        }
        warehouse.incrementResource(resInWarehouse);
        List<ResourcePosition> inputRes = new ArrayList<>();
        inputRes.add(new ResourcePosition(1, Resource.STONE, Place.CHEST, null));
        inputRes.add(new ResourcePosition(1, Resource.COIN, Place.CHEST, null));
        inputRes.add(new ResourcePosition(1, Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.TWO));
        inputRes.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        inputRes.add(new ResourcePosition(1, Resource.COIN, Place.WAREHOUSE, NumOfShelf.DEPOT_ONE));
        inputRes.add(new ResourcePosition(1, Resource.STONE, Place.WAREHOUSE, NumOfShelf.ONE));
        inputRes.add(new ResourcePosition(1, Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.DEPOT_TWO));
        try {
            warehouse.checkDecrement(inputRes);
            warehouse.decrementResource(inputRes);
            assertTrue(warehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.EMPTY && warehouse.getShelf(NumOfShelf.ONE).getQuantity() == 0);
            assertTrue(warehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.EMPTY && warehouse.getShelf(NumOfShelf.TWO).getQuantity() == 0);
            assertTrue(warehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.COIN && warehouse.getShelf(NumOfShelf.THREE).getQuantity() == 1);
            assertTrue(warehouse.getShelf(NumOfShelf.DEPOT_ONE).getResource() == Resource.COIN && warehouse.getShelf(NumOfShelf.DEPOT_ONE).getQuantity() == 1);
            assertTrue(warehouse.getShelf(NumOfShelf.DEPOT_TWO).getResource() == Resource.SHIELD && warehouse.getShelf(NumOfShelf.DEPOT_TWO).getQuantity() == 0);
        }
        catch (WrongActionException e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }


    @Test
    public void testMoveResource() {
        Warehouse warehouse = new Warehouse(3);
        warehouse.addDepot(Resource.SHIELD);
        warehouse.addDepot(Resource.STONE);
        List<ResourcePosition> resInWarehouse = new ArrayList<>();
        //resInWarehouse.add(new ResourcePosition(1,Resource.STONE, Place.WAREHOUSE, NumOfShelf.TWO));
        resInWarehouse.add(new ResourcePosition(1,Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.ONE));
        resInWarehouse.add(new ResourcePosition(1,Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(1,Resource.COIN, Place.WAREHOUSE, NumOfShelf.THREE));
        resInWarehouse.add(new ResourcePosition(1,Resource.SHIELD, Place.WAREHOUSE, NumOfShelf.DEPOT_ONE));
        resInWarehouse.add(new ResourcePosition(1,Resource.STONE, Place.WAREHOUSE, NumOfShelf.DEPOT_TWO));
        warehouse.incrementResource(resInWarehouse);
        try{
            warehouse.checkMove(NumOfShelf.THREE, NumOfShelf.TWO, 2);
            warehouse.moveResource(NumOfShelf.THREE, NumOfShelf.TWO, 2);
            warehouse.checkMove(NumOfShelf.ONE, NumOfShelf.DEPOT_ONE, 1);
            warehouse.moveResource(NumOfShelf.ONE, NumOfShelf.DEPOT_ONE, 1);
            warehouse.checkMove(NumOfShelf.DEPOT_ONE, NumOfShelf.THREE, 2);
            warehouse.moveResource(NumOfShelf.DEPOT_ONE, NumOfShelf.THREE, 2);
            assertTrue(warehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.EMPTY && warehouse.getShelf(NumOfShelf.ONE).getQuantity() == 0);
            assertTrue(warehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.COIN && warehouse.getShelf(NumOfShelf.TWO).getQuantity() == 2);
            assertTrue(warehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.SHIELD && warehouse.getShelf(NumOfShelf.THREE).getQuantity() == 2);
            assertTrue(warehouse.getShelf(NumOfShelf.DEPOT_ONE).getResource() == Resource.SHIELD && warehouse.getShelf(NumOfShelf.DEPOT_ONE).getQuantity() == 0);
            assertTrue(warehouse.getShelf(NumOfShelf.DEPOT_TWO).getResource() == Resource.STONE && warehouse.getShelf(NumOfShelf.DEPOT_TWO).getQuantity() == 1);

        }
        catch (WrongActionException e){
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    public void testAddDepot() {
        Warehouse warehouse = new Warehouse(3);
        if(warehouse.checkDepot(Resource.COIN))
            warehouse.addDepot(Resource.COIN);
        if(warehouse.checkDepot(Resource.STONE))
            warehouse.addDepot(Resource.STONE);
        assertTrue(warehouse.getShelf(NumOfShelf.ONE).getResource() == Resource.EMPTY && warehouse.getShelf(NumOfShelf.ONE).getQuantity() == 0);
        assertTrue(warehouse.getShelf(NumOfShelf.TWO).getResource() == Resource.EMPTY && warehouse.getShelf(NumOfShelf.TWO).getQuantity() == 0);
        assertTrue(warehouse.getShelf(NumOfShelf.THREE).getResource() == Resource.EMPTY && warehouse.getShelf(NumOfShelf.THREE).getQuantity() == 0);
        assertTrue(warehouse.getShelf(NumOfShelf.DEPOT_ONE).getResource() == Resource.COIN && warehouse.getShelf(NumOfShelf.DEPOT_ONE).getQuantity() == 0);
        assertTrue(warehouse.getShelf(NumOfShelf.DEPOT_TWO).getResource() == Resource.STONE && warehouse.getShelf(NumOfShelf.DEPOT_TWO).getQuantity() == 0);
        assertTrue(warehouse.getWarehouse().size() == 5);
    }
}
