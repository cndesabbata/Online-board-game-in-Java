package it.polimi.ingsw.model;
import java.util.*;


public class DevSpace {
    private final List<List<DevCard>> devSpace;

    public DevSpace (){
        devSpace = new ArrayList<>();
    }

    /*controls if the player has a certain DevCard*/
    public boolean checkCards (DevCard requirement){
        boolean check = false;
        for(List <DevCard> stack : devSpace){
           for(DevCard card : stack){
               if (card.equals(requirement)) {
                   check = true;
                   break;
               }
           }
       }
       return check;
    }

    /*adds a new card "on top" of the deck in the selected slot*/
    public void addCard (DevCard card, DevSpaceSlot slot){
        devSpace.get(slot.ordinal()).add(0,card);
    }

    /*controls if there is place for a card of a specific level in the selected slot*/
    public boolean checkPlace(int level, DevSpaceSlot slot){
       List<DevCard> deck = devSpace.get(slot.ordinal());
       return (deck.size() < 3 && level == deck.size() + 1);                                                            //equivalent to: if(deck.size() >= 3) return false; else if(deck.size() + 1 = level) return true;
    }

    /*checks if the player has a DevCard that can activate (it must be "on top")*/
    public boolean checkUpperCard (DevCard devCard){
        boolean check = false;
        for(List <DevCard> deck : devSpace){
            if(deck.get(0).equals(devCard)) {
                check = true;
                break;
            }
        }
        return check;
    }

    public List<List<DevCard>> getCards() {
        return devSpace;
    }

    /*returns the number of DevCards in DevSpace*/
    public int countCards() {
        return devSpace.stream().map(List::size).reduce(0, Integer::sum);                                   //equivalent to: devSpace.stream().map(deck -> deck.size()).reduce(0, (a,b) -> a + b);
    }

}
