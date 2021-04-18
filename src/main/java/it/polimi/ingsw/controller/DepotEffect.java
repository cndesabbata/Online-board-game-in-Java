package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.NumOfShelf;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.Warehouse;

public class DepotEffect implements LeaderEffect{
    private Resource resource;
    @Override
    public void doLeaderEffect(Player player, Action action) {
        Warehouse warehouse = player.getBoard().getWarehouse();
        if(warehouse.getWarehouse().size() < NumOfShelf.values().length){                                               //there is place for a new depot
            if(warehouse.checkDepot(resource))                                                                          //controls if there is already that depot in the Warehouse
                warehouse.addDepot(resource);
        }
    }
}
