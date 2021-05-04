package it.polimi.ingsw.server.controller.singleplayer;

import it.polimi.ingsw.server.model.Colour;

public class DiscardDevCard extends SoloActionToken{
    private Colour colour;

    public DiscardDevCard(SinglePlayerController controller, Colour colour) {
        super(controller);
        this.colour = colour;
    }

    @Override
    public LorenzoAction doSoloAction() {
        for (int i = 0; i < 2; i++) {
            if (controller.getGame().drawDevCard(colour, 1) == null) {
                if (controller.getGame().drawDevCard(colour, 2) == null) {
                    controller.getGame().drawDevCard(colour, 3);
                }
            }
        }
        return LorenzoAction.DISCARD_DEV_CARD;
    }
}
