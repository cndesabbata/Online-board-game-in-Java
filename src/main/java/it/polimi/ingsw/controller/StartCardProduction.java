package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

public class StartProduction implements Action {
    private int[] outputRes;
    private int[] inputResChest;
    private int[] inputResWharehouse;

    public StartProduction (int[] outputRes, int[] inputResChest, int[] inputResWharehouse){
        System.arraycopy(outputRes, 0, this.outputRes, 0, outputRes.length);
        System.arraycopy(inputResChest, 0, this.inputResChest, 0, inputResChest.length );
        System.arraycopy(inputResWharehouse, 0, this.inputResWharehouse, 0, inputResWharehouse.length);
    }

    @Override
    public void doAction(Player player) {
        Chest chest = player.getBoard().getChest();
        chest.setNumOfCoins(chest.getNumOfCoins() - inputResChest[0]);
        chest.setNumOfStones(chest.getNumOfStones() - inputResChest[1]);
        chest.setNumOfServants(chest.getNumOfServants() - inputResChest[2]);
        chest.setNumOfShields(chest.getNumOfShields() - inputResChest[3]);
        Wharehouse wharehouse = player.getBoard().getWharehouse();

    }
}
