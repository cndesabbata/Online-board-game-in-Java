package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Chest {
    private ArrayList<ResouceReq> resources;

    public ResouceReq getResources(int index) {
        return resources.get(index);
    }

    public void setResources(int index, int amount) throws NegativeAmountException {
        if (amount < 0)
            throw new NegativeAmountException();
        else
            resources.get(index).setquantity(amount);
    }
}

