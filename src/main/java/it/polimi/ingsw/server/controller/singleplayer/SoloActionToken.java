package it.polimi.ingsw.server.controller.singleplayer;

public abstract class SoloActionToken {
    protected SinglePlayerController controller;

    public SoloActionToken(SinglePlayerController controller) {
        this.controller = controller;
    }

    public abstract LorenzoAction doSoloAction();
}
