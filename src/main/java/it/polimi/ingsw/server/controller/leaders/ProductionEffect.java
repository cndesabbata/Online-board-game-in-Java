package it.polimi.ingsw.server.controller.leaders;

import it.polimi.ingsw.messages.actions.StartProduction;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.server.model.*;

public class ProductionEffect implements LeaderEffect {
    private final ResourcePosition inputRes;
    private final ResourcePosition outputRes;

    public ProductionEffect(ResourcePosition inputRes, ResourcePosition outputRes) {
        this.inputRes = inputRes;
        this.outputRes = outputRes;
    }

    @Override
    public void doLeaderEffect(Player player, Action action) throws WrongActionException {
        if (action instanceof StartProduction) {
            if(!(player.hasPlayedLeaderCard(LeaderType.PRODUCT, inputRes.getResource())))
                throw new WrongActionException("The user does not have the played Product Leader Card.");
            ((StartProduction) action).addInputRes(inputRes);
            ((StartProduction) action).addOutputRes(outputRes);
            player.getBoard().getItinerary().updatePosition(1, null,
                    player.getBoard().getItinerary().toNotify(player.getBoard().getItinerary().getPosition(), 1));
        }
    }
}
