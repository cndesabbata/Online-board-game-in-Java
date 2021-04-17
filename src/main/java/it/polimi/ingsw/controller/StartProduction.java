package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;

public class StartProduction implements Action{
    private DevCard devCard = null;
    private ArrayList<ResourcePosition> outputRes;
    private ArrayList<ResourcePosition> inputRes;
    private ResourcePosition extraOutputRes;
    private ResourcePosition extraInputRes;


    public StartProduction (DevCard devCard,ArrayList<ResourcePosition> outputRes, ArrayList<ResourcePosition> inputRes){
        this.devCard = devCard;
        this.outputRes = new ArrayList<>(outputRes);
        this.inputRes = new ArrayList<>(inputRes);
    }

    public StartProduction (ArrayList<ResourcePosition> outputRes, ArrayList<ResourcePosition> inputRes){
        this.outputRes = new ArrayList<>(outputRes);
        this.inputRes = new ArrayList<>(inputRes);
    }

    @Override
    public boolean doAction(Player player) {
        if(extraOutputRes != null && extraInputRes != null) {                                                                                 //if a player card was played successfully, we need to merge the extra in/out resources.
            outputRes.add(extraOutputRes);
            inputRes.add(extraInputRes);
        }
        Chest chest = player.getBoard().getChest();
        Warehouse warehouse = player.getBoard().getWharehouse();
        warehouse.decrementResource(inputRes);
        chest.decrementResource(inputRes);
        chest.incrementResource(outputRes);
        return true;
    }

    public void checkAction(Player player) throws WrongActionException{
        if(devCard != null) {                                                                                           //in case there is a devCard played
            DevSpace devSpace = player.getBoard().getDevSpace();
            if (!devSpace.checkCard(devCard)) throw new WrongActionException("The player does not have the selected devCard");
        }
        checkDevCardInput(devCard);
        checkDevCardOutput(devCard);
        Chest chest = player.getBoard().getChest();
        Warehouse warehouse = player.getBoard().getWharehouse();
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
        ArrayList<ResourceQuantity> compareInput = new ArrayList<>();
        for (ResourcePosition Rp : inputRes) {
            compareInput.add(new ResourceQuantity(Rp.getQuantity(), Rp.getResource()));
        }
        for (int i = 0; i < compareInput.size(); i++) {
            for (int j = i + 1; j < compareInput.size(); j++) {
                ResourceQuantity Rqi = compareInput.get(i);
                ResourceQuantity Rqj = compareInput.get(j);
                if (Rqi.getResource() == Rqj.getResource()) {
                    Rqi.setQuantity(Rqi.getQuantity() + Rqj.getQuantity());
                    Rqj.setQuantity(0);
                }
            }
        }
        int i;
        compareInput.removeIf(Rq -> Rq.getQuantity() == 0);                                                             ////compareInput is a list of ResourceQuantity representing outputRes

        for (ResourceQuantity Rq : devCard.getProductionInput()) {
            for (i = 0; i < compareInput.size(); i++) {
                if (compareInput.get(i).getResource() == Rq.getResource()) break;
            }
            compareInput.get(i).setQuantity(compareInput.get(i).getQuantity() - Rq.getQuantity());
        }
        int count0 = 0, count1 = 0;
        for (i = 0; i < compareInput.size(); i++) {
            if (compareInput.get(i).getQuantity() == 0) count0++;
            else if (compareInput.get(i).getQuantity() == 1) count1++;
            else if (compareInput.get(i).getQuantity() < 0)
                throw new WrongActionException("The required resources for the devCard are more than the input resources given by the Client");
        }
        if (!(count0 == compareInput.size() || (count0 == compareInput.size() - 1 && count1 == 1)))
            throw new WrongActionException("The input resources given by the Client are more than the required resources for the devCard");
    }

    private void checkDevCardOutput(DevCard devCard) throws WrongActionException {
        ArrayList<ResourceQuantity> compareOutput = new ArrayList<>();
        for (ResourcePosition Rp : outputRes) {
            compareOutput.add(new ResourceQuantity(Rp.getQuantity(), Rp.getResource()));
        }
        for (int i = 0; i < compareOutput.size(); i++) {
            for (int j = i + 1; j < compareOutput.size(); j++) {
                ResourceQuantity Rqi = compareOutput.get(i);
                ResourceQuantity Rqj = compareOutput.get(j);
                if (Rqi.getResource() == Rqj.getResource()) {
                    Rqi.setQuantity(Rqi.getQuantity() + Rqj.getQuantity());
                    Rqj.setQuantity(0);
                }
            }
        }
        int i;
        compareOutput.removeIf(Rq -> Rq.getQuantity() == 0);                                                            //compareOutput is a list of ResourceQuantity representing outputRes

        for (ResourceQuantity Rq : devCard.getProductionOutput()) {
            for (i = 0; i < compareOutput.size(); i++) {
                if (compareOutput.get(i).getResource() == Rq.getResource()) break;
            }
            compareOutput.get(i).setQuantity(compareOutput.get(i).getQuantity() - Rq.getQuantity());
        }
        int count0 = 0, count1 = 0;
        for (i = 0; i < compareOutput.size(); i++) {
            if (compareOutput.get(i).getQuantity() == 0) count0++;
            else if (compareOutput.get(i).getQuantity() == 1) count1++;
            else if (compareOutput.get(i).getQuantity() < 0)
                throw new WrongActionException("The output resources from the devCard are more than the output resources given by the Client");
        }
        if (!(count0 == compareOutput.size() || (count0 == compareOutput.size() - 1 && count1 == 1)))
            throw new WrongActionException("The output resources given by the Client are more than the output resources from the devCard");
    }
}
