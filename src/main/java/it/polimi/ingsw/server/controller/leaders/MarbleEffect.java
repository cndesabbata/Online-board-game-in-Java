package it.polimi.ingsw.server.controller.leaders;

import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.messages.actions.BuyResources;
import it.polimi.ingsw.server.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class MarbleEffect represents the effect of the marble leader card.
 *
 */
public class MarbleEffect implements LeaderEffect {
    private final int whiteMarbles;
    private final Resource resource;
    private final List<ResourcePosition> extraResources;

    /**
     * Default constructor.
     *
     * @param whiteMarbles   the number of white marbles to convert
     * @param resource       the type of the resource the white marbles will be converted to
     * @param extraResources the list of ResourcePosition objects representing the extra resources gained
     */
    public MarbleEffect(int whiteMarbles, Resource resource, List<ResourcePosition> extraResources) {
        this.whiteMarbles = whiteMarbles;
        this.resource = resource;
        this.extraResources = extraResources;
    }

    /**
     * Returns the type of resource this effect converts white marbles to.
     *
     * @return the type of resource
     */
    public Resource getResource() {
        return resource;
    }

    public int getWhiteMarbles() {
        return whiteMarbles;
    }

    /**
     * Adds the extra resources gained from the leader to the one regularly gained from the market when
     * performing a {@link BuyResources} action.
     *
     * @param player the player performing the action
     * @param action the action performed by the player
     * @throws WrongActionException when the player does not have the corresponding leader card, when the extra
     * resources do not match the number of white marbles or when the extra resources do not match the resource
     * type of the leader
     */
    @Override
    public void doLeaderEffect(Player player, Action action) throws WrongActionException {
        if(action instanceof BuyResources) {
            if(!(player.hasPlayedLeaderCard(LeaderType.MARBLE, resource)))
                throw new WrongActionException("The user does not have the played Marble Leader Card.");
            BuyResources buyResources = (BuyResources) action;
            if(extraResources.size() != whiteMarbles)
                throw new WrongActionException("Number of extra resources does not match the number of white marbles converted.");
            for(ResourcePosition resourcePosition : extraResources) {
                if (resourcePosition.getResource() != resource)
                    throw new WrongActionException("Extra resources must match marble leaders' resource.");
            }
            buyResources.addExtraRes(extraResources);
            buyResources.setLeaderUsed(true);
        }
    }
}
