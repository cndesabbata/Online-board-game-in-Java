package it.polimi.ingsw.controller.singleplayer;

public abstract class SoloActionToken {
    protected SinglePlayerController controller;

    public SoloActionToken(SinglePlayerController controller) {
        this.controller = controller;
    }

    public void doSoloAction() {

    }
}
