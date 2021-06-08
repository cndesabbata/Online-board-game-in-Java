package it.polimi.ingsw.server.controller.singleplayer;

import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.gameboard.Itinerary;

public class UpdateItinerary extends SoloActionToken{
    public UpdateItinerary(SinglePlayerController controller) {
        super(controller);
    }

    @Override
    public UserAction doSoloAction() {
        Itinerary i = controller.getGame().getPlayers().get(0).getBoard().getItinerary();
        i.updatePosition(0,2, i.toNotify(i.getBlackCrossPosition(), 2));
        return UserAction.UPDATE_ITINERARY;
    }
}
