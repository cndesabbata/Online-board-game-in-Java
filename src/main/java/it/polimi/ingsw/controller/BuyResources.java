package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.MarketSelection;
import it.polimi.ingsw.model.NumOfShelf;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.ResourceQuantity;

import java.util.ArrayList;

public class BuyResources implements Action {
    private int position;
    private MarketSelection marketSelection;
    private ArrayList<ResourceQuantity> gainedRes;
    private ArrayList<ResourceQuantity> extraRes;                                                                       //it represents the resource gained thanks to the effect of a Marble Leader
    private ArrayList<NumOfShelf> outputShelves;
    private ArrayList<LeaderEffect> leaderEffects;

    @Override
    public boolean doAction(Player player) {
        return false;
    }

    @Override
    public void checkAction(Player player){

    }
}
