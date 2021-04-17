package it.polimi.ingsw.model;
import java.util.*;


public class DevSpace {
    private ArrayList<DevCard> firstDeck = new ArrayList<>();
    private ArrayList<DevCard> secondDeck = new ArrayList<>();
    private ArrayList<DevCard> thirdDeck = new ArrayList<>();

    public DevSpace (){
        firstDeck = new ArrayList<>();
        secondDeck = new ArrayList<>();
        thirdDeck = new ArrayList<>();
    }

    public List<DevCard> getFirstDeck(){
        return new ArrayList<DevCard>(firstDeck);
    }

    public List<DevCard> getSecondDeck(){
        return new ArrayList<DevCard>(secondDeck);
    }

    public List<DevCard> getThirdDeck(){
        return new ArrayList<DevCard>(thirdDeck);
    }

    public void addFirstDeck(DevCard card){ firstDeck.add(0, card); }

    public void addSecondDeck(DevCard card){ secondDeck.add(0, card); }

    public void addThirdDeck(DevCard card){ thirdDeck.add(0, card); }

    public boolean checkPlace(int level, int slot){
        switch (slot){
            case 1: return checkDeck(firstDeck, level);
            case 2: return checkDeck(secondDeck, level);
            default: return checkDeck(thirdDeck, level);
        }
    }

    private boolean checkDeck (ArrayList<DevCard> deck, int level){
        int size = deck.size();
        if (size == 0 && level != 1) return false;
        if (size == 1 && level != 2) return false;
        if (size == 2 && level != 3) return false;
        if (size == 3) return false;
        return true;
    }

    public boolean checkCard (DevCard devCard){
        if(firstDeck.get(0).equals(devCard) || secondDeck.get(0).equals(devCard) || thirdDeck.get(0).equals(devCard))
            return true;
        else
            return false;
    }
}
