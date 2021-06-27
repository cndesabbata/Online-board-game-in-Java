package it.polimi.ingsw.server.controller.singleplayer;

import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.model.gameboard.Itinerary;

import java.util.Collections;

/**
 * Class UpdateAndShuffle represents one the action performed by Lorenzo il Magnifico
 * in a single player game, which consist in advancing on the itinerary and shuffling
 * all the solo action tokens.
 *
 */
public class UpdateAndShuffle extends SoloActionToken {

    /**
     * Default constructor.
     *
     * @param controller the controller managing the game
     */
    public UpdateAndShuffle(SinglePlayerController controller) {
        super(controller);
    }

    /**
     * Increases the black cross position by 1 and shuffles the solo action tokens.
     *
     * @return the type of the action performed by Lorenzo il Magnifico
     */
    @Override
    public UserAction doSoloAction() {
        Itinerary i = controller.getGame().getPlayers().get(0).getBoard().getItinerary();
        i.updatePosition(0, 1, i.toNotify(i.getBlackCrossPosition(), 1));
        Collections.shuffle(controller.getTokens());
        return UserAction.UPDATE_AND_SHUFFLE;
    }
}
