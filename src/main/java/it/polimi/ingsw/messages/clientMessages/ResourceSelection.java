package it.polimi.ingsw.messages.clientMessages;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.ResourcePosition;

import java.util.List;

public class ResourceSelection implements Message {
    private final List<ResourcePosition> resources;

    public ResourceSelection(List<ResourcePosition> resources) {
        this.resources = resources;
    }

    public List<ResourcePosition> getResources() {
        return resources;
    }
}
