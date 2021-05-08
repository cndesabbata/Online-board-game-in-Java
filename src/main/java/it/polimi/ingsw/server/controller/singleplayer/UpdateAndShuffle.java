package it.polimi.ingsw.server.controller.singleplayer;

import it.polimi.ingsw.server.controller.UserAction;

import java.util.Collections;

public class UpdateAndShuffle extends SoloActionToken {

    public UpdateAndShuffle(SinglePlayerController controller) {
        super(controller);
    }

    @Override
    public UserAction doSoloAction() {
        controller.getGame().getPlayers().get(0).getBoard().getItinerary().updateBlackCross(1);
        Collections.shuffle(controller.getTokens());
        return UserAction.UPDATE_AND_SHUFFLE;
    }
}
