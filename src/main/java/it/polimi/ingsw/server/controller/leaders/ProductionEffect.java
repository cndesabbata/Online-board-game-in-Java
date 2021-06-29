package it.polimi.ingsw.server.controller.leaders;

import it.polimi.ingsw.messages.actions.StartProduction;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.server.model.*;

/**
 * Class ProductionEffect represents the effect of the product leader card.
 *
 */
public class ProductionEffect implements LeaderEffect {
    private final ResourcePosition inputRes;
    private final ResourcePosition outputRes;

    /**
     * Default constructor.
     *
     * @param inputRes  the input resources for the leader
     * @param outputRes the output resources produced by the leader
     */
    public ProductionEffect(ResourcePosition inputRes, ResourcePosition outputRes) {
        this.inputRes = inputRes;
        this.outputRes = outputRes;
    }

    /**
     * If the player is performing a {@link StartProduction} action, it adds the input
     * and output lists to the lists contained in the action object and updates the
     * player's position on the itinerary by 1.
     *
     * @param player the player performing the action
     * @param action the action performed by the player
     * @throws WrongActionException when the user does not have the corresponding leader card
     */
    @Override
    public void doLeaderEffect(Player player, Action action) throws WrongActionException {
        if (action instanceof StartProduction) {
            if(!(player.hasPlayedLeaderCard(LeaderType.PRODUCT, inputRes.getResource())))
                throw new WrongActionException("The user does not have the played Product Leader Card.");
            ((StartProduction) action).addInputRes(inputRes);
            ((StartProduction) action).addOutputRes(outputRes);
            player.getBoard().getItinerary().updatePosition(1, null,
                    player.getBoard().getItinerary().toNotify(player.getBoard().getItinerary().getPosition(), 1));
        }
    }
}
