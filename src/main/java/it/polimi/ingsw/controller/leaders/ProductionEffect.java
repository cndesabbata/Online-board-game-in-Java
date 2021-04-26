package it.polimi.ingsw.controller.leaders;
import it.polimi.ingsw.controller.leaders.LeaderEffect;
import it.polimi.ingsw.controller.messages.actions.StartProduction;
import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.controller.messages.actions.Action;
import it.polimi.ingsw.model.*;

import java.util.List;

public class ProductionEffect implements LeaderEffect {
    private final ResourcePosition inputRes;
    private final ResourcePosition outputRes;

    public ProductionEffect(ResourcePosition inputRes, ResourcePosition outputRes) {
        this.inputRes = inputRes;
        this.outputRes = outputRes;
    }

    @Override
    public void doLeaderEffect(Player player, Action action) throws WrongActionException {
        if (action instanceof StartProduction){
            List<LeaderCard> playerCards = player.getHandLeaderCards();
            boolean check = false;
            for(LeaderCard Lc : playerCards){
                if (Lc.getResource() == inputRes.getResource() && Lc.getType() == LeaderType.PRODUCT && Lc.isPlayed()) {
                    check = true;
                    break;
                }
            }
            if(!check) throw new WrongActionException("The player does not have the played leadCard");
            else {
                ((StartProduction) action).addInputRes(inputRes);
                ((StartProduction) action).addOutputRes(outputRes);
                player.getBoard().getItinerary().updatePosition(1);
            }
        }
    }
}
