package it.polimi.ingsw.server.controller.leaders;

import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.gameboard.NumOfShelf;
import it.polimi.ingsw.server.model.gameboard.Warehouse;

import java.util.List;

public class DepotEffect implements LeaderEffect {
    private final Resource resource;

    public DepotEffect(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void doLeaderEffect(Player player, Action action) throws WrongActionException {
        if(!(player.hasPlayedLeaderCard(LeaderType.DEPOT, resource)))
            throw new WrongActionException("The user does not have the played Depot Leader Card.");
        Warehouse warehouse = player.getBoard().getWarehouse();
        if (warehouse.getWarehouse().size() < NumOfShelf.values().length) {                                             //there is place for a new depot
            if (warehouse.checkDepot(resource))                                                                         //controls if there is already that depot in the Warehouse
                warehouse.addDepot(resource);
        }
    }

}
