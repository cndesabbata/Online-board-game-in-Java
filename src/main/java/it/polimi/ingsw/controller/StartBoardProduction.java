package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.*;

public class StartBoardProduction implements Action {
    private Resource firstInputRes;
    private boolean isFirstResourceFromChest;
    private Resource secondInputRes;
    private boolean isSecondResourceFromChest;
    private Resource outputResource;
    private Player player;

    public StartBoardProduction(Resource firstInputRes, boolean isFirstResourceFromChest, Resource secondInputRes,
                                boolean isSecondResourceFromChest, Resource outputResource, Player player) {
        this.firstInputRes = firstInputRes;
        this.isFirstResourceFromChest = isFirstResourceFromChest;
        this.secondInputRes = secondInputRes;
        this.isSecondResourceFromChest = isSecondResourceFromChest;
        this.outputResource = outputResource;
        this.player = player;
    }

    @Override
    public void doAction(Player player){
        if (isFirstResourceFromChest){
            Chest chest = player.getBoard().getChest();
            switch (firstInputRes){
                case COIN: chest.setNumOfCoins(chest.getNumOfCoins()-1); break;
                case STONE: chest.setNumOfStones(chest.getNumOfStones()-1); break;
                case SERVANT: chest.setNumOfServants(chest.getNumOfServants()-1); break;
                case SHIELD: chest.setNumOfShields(chest.getNumOfShields()-1); break;
            }
        }
        else {

        }
        if (isSecondResourceFromChest){

        }
        else {

        }

    }
}
