package it.polimi.ingsw.server.controller.singleplayer;

import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.gameboard.Itinerary;

/**
 * Class UpdateItinerary represents one the action performed by Lorenzo il Magnifico
 * in a single player game, which consist in advancing on the itinerary.
 *
 */
public class UpdateItinerary extends SoloActionToken{

    /**
     * Default constructor.
     *
     * @param controller the controller managing the game
     */
    public UpdateItinerary(SinglePlayerController controller) {
        super(controller);
    }

    /**
     * Increases the black cross position by 2.
     *
     * @return the type of the action performed by Lorenzo il Magnifico
     */
    @Override
    public UserAction doSoloAction() {
        Itinerary i = controller.getGame().getPlayers().get(0).getBoard().getItinerary();
        i.updatePosition(0,2, i.toNotify(i.getBlackCrossPosition(), 2));
        return UserAction.UPDATE_ITINERARY;
    }
}
