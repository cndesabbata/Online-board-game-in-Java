package it.polimi.ingsw.server.controller.singleplayer;

import it.polimi.ingsw.server.controller.UserAction;

public class UpdateItinerary extends SoloActionToken{
    public UpdateItinerary(SinglePlayerController controller) {
        super(controller);
    }

    @Override
    public UserAction doSoloAction() {
        controller.getGame().getPlayers().get(0).getBoard().getItinerary().updateBlackCross(2);
        return UserAction.UPDATE_ITINERARY;
    }
}
