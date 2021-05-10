package it.polimi.ingsw.client.view;

import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.messages.actions.PlayLeadCard;

public class ActionFactory {

    public static Action createAction(int n){
        return new PlayLeadCard(1);
    }
}
