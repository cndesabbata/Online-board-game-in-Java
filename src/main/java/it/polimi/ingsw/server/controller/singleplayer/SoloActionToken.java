package it.polimi.ingsw.server.controller.singleplayer;

import it.polimi.ingsw.server.controller.UserAction;

/**
 * Abstract class SoloActionToken represents a generic action done by
 * Lorenzo il Magnifico during a single player game.
 *
 */
public abstract class SoloActionToken {
    protected SinglePlayerController controller;

    /**
     * Default Constructor
     *
     * @param controller the controller managing the game
     */
    public SoloActionToken(SinglePlayerController controller) {
        this.controller = controller;
    }

    public abstract UserAction doSoloAction();
}
