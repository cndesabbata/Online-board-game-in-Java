package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;

public abstract class Action {
    private final ArrayList<LeaderEffect> leaderEffects;

    public Action(ArrayList<LeaderEffect> leaderEffects) {
        this.leaderEffects = leaderEffects;
    }

    abstract boolean doAction(Player player);

    abstract void checkAction(Player player) throws WrongActionException;

    public ArrayList<LeaderEffect> getLeaderEffects() {
        return new ArrayList<>(leaderEffects);
    }
}

