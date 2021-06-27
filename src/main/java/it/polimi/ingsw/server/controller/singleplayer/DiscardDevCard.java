package it.polimi.ingsw.server.controller.singleplayer;

import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.model.Colour;

/**
 * Class DiscardDevCard represents one the action performed by Lorenzo il Magnifico
 * in a single player game, which consist in discarding two development card of
 * a specified colour.
 *
 */
public class DiscardDevCard extends SoloActionToken{
    private final Colour colour;

    /**
     * Default constructor.
     *
     * @param controller the controller managing the game
     * @param colour     the colour of the development cards to discard
     */
    public DiscardDevCard(SinglePlayerController controller, Colour colour) {
        super(controller);
        this.colour = colour;
    }

    /**
     * Discards two development cards of a specified colour. It first tries do discard
     * level 1 cards and if there is not any left it tries to do the same thing for
     * level 2 cards. If there is not any level 2 card left it tries to discard level
     * 3 cards.
     *
     * @return the type of the action performed by Lorenzo il Magnifico
     */
    @Override
    public UserAction doSoloAction() {
        for (int i = 0; i < 2; i++) {
            if (controller.getGame().drawDevCard(colour, 1) == null) {
                if (controller.getGame().drawDevCard(colour, 2) == null) {
                    controller.getGame().drawDevCard(colour, 3);
                }
            }
        }
        return UserAction.DISCARD_DEV_CARD;
    }
}
