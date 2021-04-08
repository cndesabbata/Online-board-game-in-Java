package it.polimi.ingsw.model;
import java.util.*;


public class DevSpace {
    private ArrayList<DevCard> firstDeck;
    private ArrayList<DevCard> secondDeck;
    private ArrayList<DevCard> thirdDeck;

    public List<DevCard> getFirstDeck(){
        return new ArrayList<DevCard>(firstDeck);
    }

    public List<DevCard> getSecondDeck(){
        return new ArrayList<DevCard>(secondDeck);
    }

    public List<DevCard> getThirdDeck(){
        return new ArrayList<DevCard>(thirdDeck);
    }

    public void addFirstDeck(DevCard card) throws WrongPlacementException {
        if (!checkplace(firstDeck, card))
            throw new WrongPlacementException();
        else
            firstDeck.add(0, card);
    }

    public void addSecondDeck(DevCard card){
        if(!checkplace(secondDeck, card))
            throw new WrongPlacementException();
        else
        secondDeck.add(0, card);
    }

    public void addThirdDeck(DevCard card){
        if(!checkplace(thirdDeck, card))
            throw new WrongPlacementException();
        else
        thirdDeck.add(0, card);
    }
    private boolean checkplace(ArrayList <DevCard> deck, DevCard card){
        if(deck == null && card.getLevel() != 1)
            return false;
        if(deck.size() == 1 && card.getLevel() != 2)
            return false;
        if(deck.size() == 2 && card.getLevel() != 3)
            return false;
        if(deck.size() >= 3)
            return false;
        return true;

    }
}
