package it.polimi.ingsw.controller.messages.actions;

import it.polimi.ingsw.controller.leaders.LeaderEffect;
import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.gameboard.Chest;
import it.polimi.ingsw.model.gameboard.DevSpace;
import it.polimi.ingsw.model.gameboard.Warehouse;

import java.util.ArrayList;
import java.util.List;

public class StartProduction implements Action {
    private final List<DevCard> devCards;
    private final List<ResourcePosition> outputRes;
    private final List<ResourcePosition> inputRes;
    private final List<LeaderEffect> leaderEffects;


    public StartProduction(List<DevCard> devCards, List<ResourcePosition> inputRes,
                           List<ResourcePosition> outputRes, List<LeaderEffect> leaderEffects) {
        this.devCards = new ArrayList<>(devCards);
        this.outputRes = new ArrayList<>(outputRes);
        this.inputRes = new ArrayList<>(inputRes);
        this.leaderEffects = leaderEffects;
    }

    public StartProduction(List<ResourcePosition> inputRes, List<ResourcePosition> outputRes,
                           List<LeaderEffect> leaderEffects) {
        this.devCards = null;
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
        else if (devCards != null) {                                                                                     //in case there is a devCard played
            DevSpace devSpace = player.getBoard().getDevSpace();
            for(DevCard devCard : devCards) {
                if (!devSpace.checkUpperCard(devCard))
                    throw new WrongActionException("The player does not have one of the selected devCard");
            }
        }
        checkInputOutput(devCards);
        for (LeaderEffect leaderEffect : leaderEffects) {                                                               //before checking if the player has all the resources we must merge the extra resources with the input / output                                                                             //if a player card was played successfully, we need to merge the extra in/out resources.
            leaderEffect.doLeaderEffect(player, this);
        }
        Chest chest = player.getBoard().getChest();
        Warehouse warehouse = player.getBoard().getWarehouse();
        warehouse.checkDecrement(inputRes);
        chest.checkDecrement(inputRes);
        chest.checkIncrement(outputRes);
    }

    /*used by ProductionEffect to add an extra ResourcePosition in output*/
    public void addOutputRes(ResourcePosition extra) {
        outputRes.add(new ResourcePosition(extra.getQuantity(), extra.getResource(), extra.getPlace(), extra.getShelf()));
    }

    /*used by ProductionEffect to add an extra ResourcePosition in input*/
    public void addInputRes(ResourcePosition extra) {                                                                   //set a shallow copy
        inputRes.add(new ResourcePosition(extra.getQuantity(), extra.getResource(), extra.getPlace(), extra.getShelf()));
    }

    /*controls if input and output correspond to the input and output of the devCard or of the boardProduction (or both)*/
    private void checkInputOutput(List<DevCard> devCards) throws WrongActionException {
        if(devCards != null) {
            List <ResourceQuantity> totalDevInput = new ArrayList<>();                                                  //represents the total input resources required by all the devCards
            List <ResourceQuantity> totalDevOutput = new ArrayList<>();                                                 //represents the total output resources required by all the devCards
            for(DevCard dc : devCards) {
                totalDevCardResources(totalDevInput, dc.getProductionInput());
                totalDevCardResources(totalDevOutput, dc.getProductionOutput());
            }
            totalDevInput.forEach(Rq -> System.out.println(Rq.getResource()+ " " +Rq.getQuantity()));
            System.out.println("\n");
            totalDevOutput.forEach(Rq -> System.out.println(Rq.getResource()+ " " +Rq.getQuantity()));
            for (ResourceQuantity Rq : totalDevInput) {
                    if (inputRes.stream().filter(Rp -> Rp.getResource() == Rq.getResource()).map(ResourceQuantity::getQuantity).
                            reduce(0, Integer::sum) < Rq.getQuantity())
                        throw new WrongActionException("The input resources specified from the user are different from the ones required by the devCard.");
                }
                for (ResourceQuantity Rq : totalDevOutput) {
                    if (outputRes.stream().filter(Rp -> Rp.getResource() == Rq.getResource()).map(ResourceQuantity::getQuantity).
                            reduce(0, Integer::sum) < Rq.getQuantity())
                        throw new WrongActionException("The output resources specified from the user are different from the ones generated by the devCard.");
                }
            int clientInputQuantity = inputRes.stream().map(ResourcePosition::getQuantity).reduce(0, Integer::sum);  //number of resources in inputRes
            int clientOutputQuantity = outputRes.stream().map(ResourcePosition::getQuantity).reduce(0, Integer::sum);  //number of resources in outputRes
            int devOutputQuantity = totalDevOutput.stream().map(ResourceQuantity::getQuantity).reduce(0, Integer::sum);                                                                                  //total number of resources in (considering all cards) devCard.getProductionOutput
            int devInputQuantity = totalDevInput.stream().map(ResourceQuantity::getQuantity).reduce(0, Integer::sum);                                                                                       //total number of resources in (considering all cards) devCard.getProductionInput
            if ((clientInputQuantity > devInputQuantity || clientOutputQuantity > devOutputQuantity) &&
                    (clientInputQuantity != 2 + devInputQuantity || clientOutputQuantity != 1 + devOutputQuantity))     //case of devCardProduction and boardProduction but the number of specified resources for the boardProduction is incorrect
                throw new WrongActionException("The number of input / output resources for the board production is incorrect.");
        }
        else
            if(inputRes.size() != 2 || outputRes.size() != 1)
                throw new WrongActionException("The number of input / output resources for the board production is incorrect.");
    }

    /*modify totalDev, adding the nodes of production, or updating the nodes of totalDev if they already exist*/
    private void totalDevCardResources(List <ResourceQuantity> totalDev, List<ResourceQuantity> production){
        for (ResourceQuantity rqi : production) {
            int j;
            for (j = 0; j < totalDev.size(); j++) {
                ResourceQuantity rqj = totalDev.get(j);
                if (rqj.getResource() == rqi.getResource()) {
                    totalDev.set(j, new ResourceQuantity(rqj.getQuantity() + rqi.getQuantity(), rqj.getResource()));
                    break;
                }
            }
            if (j >= totalDev.size())
                totalDev.add(j, new ResourceQuantity(rqi.getQuantity(), rqi.getResource()));
        }
    }
}
