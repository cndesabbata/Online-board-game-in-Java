package it.polimi.ingsw.controller.singleplayer;

import it.polimi.ingsw.model.Colour;

public class DiscardDevCard extends SoloActionToken{
    private Colour colour;

    public DiscardDevCard(SinglePlayerController controller, Colour colour) {
        super(controller);
        this.colour = colour;
    }

    @Override
    public void doSoloAction() {
        if (controller.getGame().drawDevCard(colour, 1) == null){
            if (controller.getGame().drawDevCard(colour, 2) == null){
                controller.getGame().drawDevCard(colour, 3);
            }
        }
    }
}
