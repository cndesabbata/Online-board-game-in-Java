package it.polimi.ingsw.messages.actions;

import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.controller.leaders.LeaderEffect;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.gameboard.Chest;
import it.polimi.ingsw.server.model.gameboard.DevSpace;
import it.polimi.ingsw.server.model.gameboard.Warehouse;

import java.util.ArrayList;
import java.util.List;

public class StartProduction implements Action {
    private final List<Integer> chosenCards;
    private final List<ResourcePosition> outputRes;
    private final List<ResourcePosition> inputRes;
    private final List<LeaderEffect> leaderEffects;
    private UserAction type;
    private List <DevCard> devCards;


    public StartProduction(List<Integer> chosenCards, List<ResourcePosition> inputRes,
                           List<ResourcePosition> outputRes, List<LeaderEffect> leaderEffects) {
        this.chosenCards = new ArrayList<>(chosenCards);
        this.outputRes = new ArrayList<>(outputRes);
        this.inputRes = new ArrayList<>(inputRes);
        this.leaderEffects = leaderEffects;
        this.type = UserAction.START_PRODUCTION;
        this.devCards = null;
    }

    public StartProduction(List<ResourcePosition> inputRes, List<ResourcePosition> outputRes,
                           List<LeaderEffect> leaderEffects) {
        this.chosenCards = null;
        this.devCards = null;
        this.outputRes = new ArrayList<>(outputRes);
        this.inputRes = new ArrayList<>(inputRes);
        this.leaderEffects = leaderEffects;
    }

    @Override
    public UserAction getType() {
        return type;
    }

    /*execute the action, knowing that is correct and feasible*/
    @Override
    public boolean doAction(Player player) {
        if(outputRes.stream().anyMatch(Rp -> Rp.getResource() == Resource.FAITHPOINT)){
            int faithpoints = outputRes.stream().filter(Rp -> Rp.getResource() == Resource.FAITHPOINT)
                    .map(ResourcePosition::getQuantity).reduce(0, Integer::sum);
            player.getBoard().getItinerary().updatePosition(faithpoints);
            outputRes.removeIf(Rp -> Rp.getResource() == Resource.FAITHPOINT);
        }
        player.getBoard().expendResources(inputRes);
        player.getBoard().getChest().incrementResource(outputRes);
        return true;
    }

    private void devCardsConstructor(Player player) throws WrongActionException{
        DevSpace devSpace = player.getBoard().getDevSpace();
        devCards = new ArrayList<>();
        List<Integer> counter = new ArrayList<>();
        for(int i = 0; i < devSpace.getCards().size(); i++) {
            counter.add(0);
        }
        for(int i : chosenCards){
            if(i < 0 || i > 2)
                throw new WrongActionException("The selected slot does not exist. ");
            if(devSpace.getCards().get(i).size() == 0)
                throw new WrongActionException("The selected slot is empty.");
            counter.set(i, counter.get(i) + 1);
            if(counter.stream().anyMatch(I -> I > 1))
                throw new WrongActionException("The same development card cannot be chosen twice. ");
            devCards.add(devSpace.getCards().get(i).get(0));
        }
    }

    /*controls if the Action is correct and also resolve any leaderEffect*/
    @Override
    public void checkAction(Player player) throws WrongActionException {
        if (player.isExclusiveActionDone())
            throw new WrongActionException("The player has already done an exclusive action this turn. ");
        if (chosenCards != null)                                                                                        //in case there is a devCard played
            devCardsConstructor(player);
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
        outputRes.add(new ResourcePosition(extra.getResource(), extra.getPlace(), extra.getShelf()));
    }

    /*used by ProductionEffect to add an extra ResourcePosition in input*/
    public void addInputRes(ResourcePosition extra) {                                                                   //set a shallow copy
        inputRes.add(new ResourcePosition(extra.getResource(), extra.getPlace(), extra.getShelf()));
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
            for (ResourceQuantity Rq : totalDevInput) {
                    if (inputRes.stream().filter(Rp -> Rp.getResource() == Rq.getResource()).map(ResourceQuantity::getQuantity).
                            reduce(0, Integer::sum) < Rq.getQuantity())
                        throw new WrongActionException("The input resources specified by the user are different from the ones required by the Development Card. ");
                }
                for (ResourceQuantity Rq : totalDevOutput) {
                    if (outputRes.stream().filter(Rp -> Rp.getResource() == Rq.getResource()).map(ResourceQuantity::getQuantity).
                            reduce(0, Integer::sum) < Rq.getQuantity())
                        throw new WrongActionException("The output resources specified by the user are different from the ones generated by the Development Card. ");
                }
            int clientInputQuantity = inputRes.stream().map(ResourcePosition::getQuantity).reduce(0, Integer::sum);  //number of resources in inputRes
            int clientOutputQuantity = outputRes.stream().map(ResourcePosition::getQuantity).reduce(0, Integer::sum);  //number of resources in outputRes
            int devOutputQuantity = totalDevOutput.stream().map(ResourceQuantity::getQuantity).reduce(0, Integer::sum);                                                                                  //total number of resources in (considering all cards) devCard.getProductionOutput
            int devInputQuantity = totalDevInput.stream().map(ResourceQuantity::getQuantity).reduce(0, Integer::sum);                                                                                       //total number of resources in (considering all cards) devCard.getProductionInput
            if ((clientInputQuantity > devInputQuantity || clientOutputQuantity > devOutputQuantity) &&
                    (clientInputQuantity != 2 + devInputQuantity || clientOutputQuantity != 1 + devOutputQuantity))     //case of devCardProduction and boardProduction but the number of specified resources for the boardProduction is incorrect
                throw new WrongActionException("The number of input / output resources for the board production is incorrect. ");
        }
        else
            if(inputRes.stream().map(ResourcePosition::getQuantity).reduce(0, Integer::sum) != 2 ||
                    outputRes.stream().map(ResourcePosition::getQuantity).reduce(0, Integer::sum) != 1)
                throw new WrongActionException("The number of input / output resources for the board production is incorrect. ");
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
