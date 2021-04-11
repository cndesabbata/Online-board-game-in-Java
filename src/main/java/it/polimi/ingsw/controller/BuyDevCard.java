package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.DevCard;
import it.polimi.ingsw.model.Player;

public class BuyDevCard implements Action {
    private Colour colour;
    private int level;
    private int slot;

    public BuyDevCard(Colour colour, int level, int slot) {
        this.colour = colour;
        this.level = level;
        this.slot = slot;
    }

    @Override
    public boolean doAction(Player player, boolean actionDone) {
        if (actionDone) player.setActionAlreadyDone(true);
        DevCard boughtCard = player.getGame().drawDevCard(colour, level);
        try {
            switch (slot){
                case 1: player.getBoard().getDevSpace().addFirstDeck(boughtCard); break;
                case 2: player.getBoard().getDevSpace().addSecondDeck(boughtCard); break;
                case 3: player.getBoard().getDevSpace().addThirdDeck(boughtCard); break;
            }
        } catch (WrongPlacementException e) {
            e.printStackTrace();
        }
        return true;
    }
}
