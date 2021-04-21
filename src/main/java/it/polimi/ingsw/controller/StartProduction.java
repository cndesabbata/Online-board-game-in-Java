package it.polimi.ingsw.controller;
import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class StartProduction extends Action{
    private final DevCard devCard;
    private final List<ResourcePosition> outputRes;
    private final List<ResourcePosition> inputRes;
    private ResourcePosition extraOutputRes;
    private ResourcePosition extraInputRes;


    public StartProduction (DevCard devCard, List<ResourcePosition> outputRes,
                            List<ResourcePosition> inputRes, List <LeaderEffect> leaderEffects){
        super(leaderEffects);
        this.devCard = devCard;
        this.outputRes = new ArrayList<>(outputRes);
        this.inputRes = new ArrayList<>(inputRes);
        this.extraInputRes = null;
        this.extraOutputRes = null;
    }

    public StartProduction (List<ResourcePosition> outputRes, List<ResourcePosition> inputRes,
                            List <LeaderEffect> leaderEffects){
        super(leaderEffects);
        this.devCard = null;
        this.outputRes = new ArrayList<>(outputRes);
        this.inputRes = new ArrayList<>(inputRes);
        this.extraInputRes = null;
        this.extraOutputRes = null;
    }

    @Override
    public boolean doAction(Player player) {
        Chest chest = player.getBoard().getChest();
        Warehouse warehouse = player.getBoard().getWarehouse();
        warehouse.decrementResource(inputRes);
        chest.decrementResource(inputRes);
        chest.incrementResource(outputRes);
        return true;
    }

    public void checkAction(Player player) throws WrongActionException {
        if(player.isActionDone())
            throw new WrongActionException("The player has already done an exclusive action this turn");
        else if(devCard != null) {                                                                                           //in case there is a devCard played
            DevSpace devSpace = player.getBoard().getDevSpace();
            if (!devSpace.checkUpperCard(devCard))
                throw new WrongActionException("The player does not have the selected devCard");
        }
        checkDevCardInput(devCard);
        checkDevCardOutput(devCard);
        if(extraOutputRes != null && extraInputRes != null) {                                                           //before checking if the player has all the resources we must merge the extra resources with the input / output                                                                             //if a player card was played successfully, we need to merge the extra in/out resources.
            outputRes.add(extraOutputRes);
            inputRes.add(extraInputRes);
        }
        Chest chest = player.getBoard().getChest();
        Warehouse warehouse = player.getBoard().getWarehouse();
        warehouse.checkDecrement(inputRes);
        chest.checkDecrement(inputRes);
        chest.checkIncrement(outputRes);
    }

    public void setExtraOutputRes(ResourcePosition extra) {                                                             //set a shallow copy
        this.extraOutputRes = new ResourcePosition(extra.getQuantity(), extra.getResource(), extra.getPlace(), extra.getShelf());
    }

    public void setExtraInputRes(ResourcePosition extra) {                                                              //set a shallow copy
        this.extraInputRes = new ResourcePosition(extra.getQuantity(), extra.getResource(), extra.getPlace(), extra.getShelf());
    }

    private void checkDevCardInput(DevCard devCard) throws WrongActionException {
        List<ResourceQuantity> compareInput = new ArrayList<>();
        for (ResourcePosition Rp : inputRes) {
            for (int i = Rp.getQuantity(); i > 0; i--) {
                compareInput.add(new ResourceQuantity(1, Rp.getResource()));
            }
        }
        for (ResourceQuantity Rq : devCard.getProductionInput()) {
            for (int i = Rq.getQuantity(); i > 0; i--) {
                int j;
                for (j = 0; j < compareInput.size(); j++) {                                                               //this loop searches in compareInput a ResourceQuantity that has the same resource as Rq
                    if (compareInput.get(j).getResource() == Rq.getResource())
                        break;
                }
                try {
                    compareInput.remove(j);
                } catch (IndexOutOfBoundsException e) {                                                                    //in case the ResourceQuantity in compareInput that has the same resource of Rq is not found
                    throw new WrongActionException("The resources required by the devCard are more than the input ones");
                }
            }
        }
        if (compareInput.size() != 0 && compareInput.size() != 2)
            throw new WrongActionException("The input resources are more than the ones required by the devCard");
    }

    private void checkDevCardOutput(DevCard devCard) throws WrongActionException {
        List<ResourceQuantity> compareOutput = new ArrayList<>();
        for (ResourcePosition Rp : outputRes) {
            for (int i = Rp.getQuantity(); i > 0; i--) {
                compareOutput.add(new ResourceQuantity(1, Rp.getResource()));
            }
        }
        for (ResourceQuantity Rq : devCard.getProductionOutput()) {
            for (int i = Rq.getQuantity(); i > 0; i--) {
                int j;
                for (j = 0; j < compareOutput.size(); j++) {                                                            //this loop searches in compareOutput a ResourceQuantity that has the same resource as Rq
                    if (compareOutput.get(j).getResource() == Rq.getResource())
                        break;
                }
                try {
                    compareOutput.remove(j);
                } catch (IndexOutOfBoundsException e) {                                                                 //in case the ResourceQuantity in compareOutput that has the same resource of Rq is not found
                    throw new WrongActionException("The resources produced by the devCard are more than the output ones");
                }
            }
        }
        if (compareOutput.size() != 0 && compareOutput.size() != 1)
            throw new WrongActionException("The output resources are more than the ones produced by the devCard");
    }
}
