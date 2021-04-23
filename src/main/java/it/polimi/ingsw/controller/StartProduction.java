package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class StartProduction implements Action {
    private final DevCard devCard;
    private final List<ResourcePosition> outputRes;
    private final List<ResourcePosition> inputRes;
    private final List<LeaderEffect> leaderEffects;


    public StartProduction(DevCard devCard, List<ResourcePosition> outputRes,
                           List<ResourcePosition> inputRes, List<LeaderEffect> leaderEffects) {
        this.devCard = devCard;
        this.outputRes = new ArrayList<>(outputRes);
        this.inputRes = new ArrayList<>(inputRes);
        this.leaderEffects = leaderEffects;
    }

    public StartProduction(List<ResourcePosition> outputRes, List<ResourcePosition> inputRes,
                           List<LeaderEffect> leaderEffects) {
        this.devCard = null;
        this.outputRes = new ArrayList<>(outputRes);
        this.inputRes = new ArrayList<>(inputRes);
        this.leaderEffects = leaderEffects;
    }

    /*execute the action, knowing that is correct and feasible*/
    @Override
    public boolean doAction(Player player) {
        Chest chest = player.getBoard().getChest();
        Warehouse warehouse = player.getBoard().getWarehouse();
        warehouse.decrementResource(inputRes);
        chest.decrementResource(inputRes);
        chest.incrementResource(outputRes);
        return true;
    }

    /*controls if the Action is correct and also resolve any leaderEffect*/
    @Override
    public void checkAction(Player player) throws WrongActionException {
        if (player.isActionDone())
            throw new WrongActionException("The player has already done an exclusive action this turn");
        else if (devCard != null) {                                                                                           //in case there is a devCard played
            DevSpace devSpace = player.getBoard().getDevSpace();
            if (!devSpace.checkUpperCard(devCard))
                throw new WrongActionException("The player does not have the selected devCard");
        }
        checkDevCardInput(devCard);
        checkDevCardOutput(devCard);
        for (LeaderEffect leaderEffect : leaderEffects) {                                                           //before checking if the player has all the resources we must merge the extra resources with the input / output                                                                             //if a player card was played successfully, we need to merge the extra in/out resources.
            leaderEffect.doLeaderEffect(player, this);
        }
        Chest chest = player.getBoard().getChest();
        Warehouse warehouse = player.getBoard().getWarehouse();
        warehouse.checkDecrement(inputRes);
        chest.checkDecrement(inputRes);
        chest.checkIncrement(outputRes);
    }

    /*used by ProductionEffect to add an extra ResourcePosition in output*/
    public void addOutputRes(ResourcePosition extra) {                                                             //set a shallow copy
        outputRes.add(new ResourcePosition(extra.getQuantity(), extra.getResource(), extra.getPlace(), extra.getShelf()));
    }

    /*used by ProductionEffect to add an extra ResourcePosition in input*/
    public void addInputRes(ResourcePosition extra) {                                                                   //set a shallow copy
        inputRes.add(new ResourcePosition(extra.getQuantity(), extra.getResource(), extra.getPlace(), extra.getShelf()));
    }

    /*controls if inputRes corresponds to the input requested by the devCard*/
    private void checkDevCardInput(DevCard devCard) throws WrongActionException {
        List<ResourceQuantity> compareInput = new ArrayList<>();
        for (ResourcePosition Rp : inputRes) {
            compareInput.add(new ResourceQuantity(inputRes.stream().filter(Rq -> Rq.getResource() == Rp.getResource()).
                    map(ResourceQuantity::getQuantity).reduce(0, Integer::sum), Rp.getResource()));              //at the end of the for loop compareInput is a list of ResourceQuantity without repetition of resources
        }
        for (ResourceQuantity Rq : devCard.getProductionInput()) {
            int i;
            for (i = 0; i < inputRes.size(); i++) {
                if (inputRes.get(i).getResource() == Rq.getResource())
                    break;
            }
            compareInput.get(i).setQuantity(compareInput.get(i).getQuantity() - Rq.getQuantity());
        }
        if (compareInput.stream().anyMatch(Rq -> Rq.getQuantity() < 0))                                                 //it controls if there are some nodes of compareInput with a negative quantity, hence indicating that the inputRes are less than the ones required by the devCard
            throw new WrongActionException("The input resources are less than the ones required by the devCard.");
        else if (compareInput.stream().anyMatch(Rq -> Rq.getQuantity() > 0) &&                                          //if I do not have one of the following situation: - All nodes in compareInput have quantity = 0;
                compareInput.stream().map(ResourceQuantity::getQuantity).reduce(0, Integer::sum) != 2)                  //- The total quantity of resources is 2;
            throw new WrongActionException("The input resources are more than the ones required by the devCard.");      //then inputRes has more resources than the one required by the devCard.
    }

    /*controls if outputRes corresponds to the output generated by the devCard*/
    private void checkDevCardOutput(DevCard devCard) throws WrongActionException {
        List<ResourceQuantity> compareOutput = new ArrayList<>();
        for (ResourcePosition Rp : outputRes) {
            compareOutput.add(new ResourceQuantity(outputRes.stream().filter(Rq -> Rq.getResource() == Rp.getResource()).
                    map(ResourceQuantity::getQuantity).reduce(0, Integer::sum), Rp.getResource()));              //at the end of the for loop compareOutput is a list of ResourceQuantity without repetition of resources
        }
        for (ResourceQuantity Rq : devCard.getProductionOutput()) {
            int i;
            for (i = 0; i < outputRes.size(); i++) {
                if (outputRes.get(i).getResource() == Rq.getResource())
                    break;
            }
            compareOutput.get(i).setQuantity(compareOutput.get(i).getQuantity() - Rq.getQuantity());
        }
        if (compareOutput.stream().anyMatch(Rq -> Rq.getQuantity() < 0))                                                //it controls if there are some nodes of compareOutput with a negative quantity, hence indicating that the outputRes are less than the ones generated by the devCard
            throw new WrongActionException("The output resources are less than the ones generated by the devCard.");
        else if (compareOutput.stream().anyMatch(Rq -> Rq.getQuantity() > 0) &&                                         //if I do not have one of the following situation: - All nodes in compareInput have quantity = 0;
                compareOutput.stream().map(ResourceQuantity::getQuantity).reduce(0, Integer::sum) != 1)          //- The total quantity of resources is 1;
            throw new WrongActionException("The output resources are more than the ones generated by the devCard.");    //then inputRes has more resources than the one required by the devCard.
    }
}
