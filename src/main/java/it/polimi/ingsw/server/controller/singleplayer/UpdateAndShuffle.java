package it.polimi.ingsw.server.controller.singleplayer;

import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.model.gameboard.Itinerary;

import java.util.Collections;

public class UpdateAndShuffle extends SoloActionToken {

    public UpdateAndShuffle(SinglePlayerController controller) {
        super(controller);
    }

    @Override
    public UserAction doSoloAction() {
        Itinerary i = controller.getGame().getPlayers().get(0).getBoard().getItinerary();
        i.updatePosition(0, 1, i.toNotify(i.getBlackCrossPosition(), 1));
        Collections.shuffle(controller.getTokens());
        return UserAction.UPDATE_AND_SHUFFLE;
    }
}
