package it.polimi.ingsw.server.controller.singleplayer;

public class UpdateItinerary extends SoloActionToken{
    public UpdateItinerary(SinglePlayerController controller) {
        super(controller);
    }

    @Override
    public void doSoloAction() {
        controller.getGame().getPlayers().get(0).getBoard().getItinerary().updateBlackCross(2);
    }
}
