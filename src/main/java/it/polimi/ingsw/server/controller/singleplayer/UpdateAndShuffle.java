package it.polimi.ingsw.server.controller.singleplayer;

import java.util.Collections;

public class UpdateAndShuffle extends SoloActionToken {

    public UpdateAndShuffle(SinglePlayerController controller) {
        super(controller);
    }

    @Override
    public void doSoloAction() {
        controller.getGame().getPlayers().get(0).getBoard().getItinerary().updateBlackCross(1);
        Collections.shuffle(controller.getTokens());
    }
}
