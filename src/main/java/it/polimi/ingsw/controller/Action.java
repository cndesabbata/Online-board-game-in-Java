package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;

public interface  Action {

     boolean doAction(Player player);

     void checkAction(Player player) throws WrongActionException;

}

