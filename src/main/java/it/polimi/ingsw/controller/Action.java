package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Action {
    private final List<LeaderEffect> leaderEffects;

    public Action(List<LeaderEffect> leaderEffects) {
        this.leaderEffects = leaderEffects;
    }

    public abstract boolean doAction(Player player);

    public abstract void checkAction(Player player) throws WrongActionException;

    public List<LeaderEffect> getLeaderEffects() {
        return new ArrayList<>(leaderEffects);
    }
}

