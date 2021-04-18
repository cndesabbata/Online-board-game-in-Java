package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.MarketSelection;
import it.polimi.ingsw.model.NumOfShelf;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.ResourceQuantity;

import java.util.ArrayList;

public class BuyResources extends Action {
    private int position;
    private MarketSelection marketSelection;
    private ArrayList<ResourceQuantity> gainedRes;
    private ArrayList<ResourceQuantity> extraRes;                                                                       //it represents the resource gained thanks to the effect of a Marble Leader
    private ArrayList<NumOfShelf> outputShelves;
    private ArrayList<LeaderEffect> leaderEffects;

    public BuyResources(ArrayList<LeaderEffect> leaderEffects, int position, MarketSelection marketSelection,
                        ArrayList<ResourceQuantity> gainedRes, ArrayList<ResourceQuantity> extraRes,
                        ArrayList<NumOfShelf> outputShelves, ArrayList<LeaderEffect> leaderEffects1) {
        super(leaderEffects);
        this.position = position;
        this.marketSelection = marketSelection;
        this.gainedRes = gainedRes;
        this.extraRes = extraRes;
        this.outputShelves = outputShelves;
        this.leaderEffects = leaderEffects1;
    }

    public BuyResources(ArrayList<LeaderEffect> leaderEffects) {
        super(leaderEffects);
    }

    @Override
    public boolean doAction(Player player) {
        return false;
    }

    @Override
    public void checkAction(Player player){

    }
}
