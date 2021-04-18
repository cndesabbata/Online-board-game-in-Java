package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;

public class ProductionEffect implements LeaderEffect{
    private ResourcePosition inputRes;
    private ResourcePosition outputRes;

    public ProductionEffect(ResourcePosition inputRes, ResourcePosition outputRes) {
        this.inputRes = inputRes;
        this.outputRes = outputRes;
    }

    @Override
    public void doLeaderEffect(Player player, Action action) throws WrongActionException {
        if (action instanceof StartProduction){
            ArrayList <LeaderCard> playerCards = player.getHandLeaderCards();
            boolean check = false;
            for(LeaderCard Lc : playerCards){
                if(Lc.getResource() == inputRes.getResource() && Lc.getType() == LeaderType.PRODUCT)
                    check = true;
            }
            if(!check) throw new WrongActionException("The player does not have the played leadCard");
            else {
                ((StartProduction) action).setExtraInputRes(inputRes);
                ((StartProduction) action).setExtraOutputRes(outputRes);
                player.getBoard().getItinerary().updatePosition(1);
            }
        }
    }
}
