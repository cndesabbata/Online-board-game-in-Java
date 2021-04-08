package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Chest {
    private int numOfCoins;
    private int numOfStones;
    private int numOfServants;
    private int numOfShields;

    public int getNumOfCoins() { return numOfCoins; }

    public int getNumOfStones() { return numOfStones; }

    public int getNumOfServants() { return numOfServants; }

    public int getNumOfShields() { return numOfShields; }

    public void setNumOfCoins(int numOfCoins) {
        this.numOfCoins = numOfCoins;
    }

    public void setNumOfStones(int numOfStones) {
        this.numOfStones = numOfStones;
    }

    public void setNumOfServants(int numOfServants) {
        this.numOfServants = numOfServants;
    }

    public void setNumOfShields(int numOfShields) {
        this.numOfShields = numOfShields;
    }

    public boolean checkquantity (Resource resource, int quantity){             //con questo metodo non ci servono le eccezioni per le set sulle quantità negative
        boolean check = false;
        switch (resource) {
            case COIN:
                check = numOfCoins >= quantity;
                break;
            case STONE:
                check = numOfStones >= quantity;
                break;
            case SERVANT:
                check = numOfServants >= quantity;
                break;
            case SHIELD:
                check = numOfShields >= quantity;
                break;
        }
        return check;
    }
}

