package it.polimi.ingsw.model;
import java.util.*;


public class DevSpace {
    private final ArrayList<DevCard> firstDeck;
    private final ArrayList<DevCard> secondDeck;
    private final ArrayList<DevCard> thirdDeck;

    public DevSpace (){
        firstDeck = new ArrayList<>();
        secondDeck = new ArrayList<>();
        thirdDeck = new ArrayList<>();
    }

    public boolean checkCards (DevCard requirement){
        return (checkLevelColour(firstDeck, requirement)
                || checkLevelColour(firstDeck, requirement)
                || checkLevelColour(firstDeck, requirement));
    }

    private boolean checkLevelColour(ArrayList<DevCard> deck, DevCard requirement){
        for (DevCard card : deck){
            if (card.getColour().equals(requirement.getColour()) && card.getLevel() >= requirement.getLevel())
                return true;
        }
        return false;
    }

    public void addCard (DevCard card, int slot){
        switch (slot){
            case (1): addFirstDeck(card);
            case (2): addSecondDeck(card);
            case (3): addThirdDeck(card);
        }
    }

    private void addFirstDeck(DevCard card){ firstDeck.add(0, card); }

    private void addSecondDeck(DevCard card){ secondDeck.add(0, card); }

    private void addThirdDeck(DevCard card){ thirdDeck.add(0, card); }

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
        return size != 3;
    }

    public boolean checkCard (DevCard devCard){
        return firstDeck.get(0).equals(devCard) || secondDeck.get(0).equals(devCard) || thirdDeck.get(0).equals(devCard);
    }
}
