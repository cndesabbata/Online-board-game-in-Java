package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.ResourcePosition;

import java.util.List;

/**
 * Class ResourceSelection is a {@link Message} sent from the player to server. It contains
 * the resources chosen by the player in the setup phase.
 *
 */
public class ResourceSelection implements Message {
    private final List<ResourcePosition> resources;

    /**
     * Creates a new ResourceSelection instance.
     *
     * @param resources the list of ResourcePosition objects chosen by the player
     */
    public ResourceSelection(List<ResourcePosition> resources) {
        this.resources = resources;
    }

    /**
     * Returns the resources chosen by the player.
     *
     * @return the resources chosen by the player
     */
    public List<ResourcePosition> getResources() {
        return resources;
    }
}
