package it.polimi.ingsw.server.controller.singleplayer;

import it.polimi.ingsw.server.controller.UserAction;

public abstract class SoloActionToken {
    protected SinglePlayerController controller;

    public SoloActionToken(SinglePlayerController controller) {
        this.controller = controller;
    }

    public abstract UserAction doSoloAction();
}
