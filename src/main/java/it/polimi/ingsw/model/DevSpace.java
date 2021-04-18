package it.polimi.ingsw.model;
import java.util.*;


public class DevSpace {
    private final ArrayList<ArrayList<DevCard>> devSpace;

    public DevSpace (){
        devSpace = new ArrayList<>();
    }

    public boolean checkCards (DevCard requirement){
        boolean check = false;
       for(ArrayList <DevCard> stack : devSpace){
           for(DevCard card : stack){
               if(card.equals(requirement))
                   check = true;
           }
       }
       return check;
    }

    public void addCard (DevCard card, DevSpaceSlot slot){
        devSpace.get(slot.ordinal()).add(0,card);
    }

    public boolean checkPlace(int level, DevSpaceSlot slot){
       ArrayList<DevCard> deck = devSpace.get(slot.ordinal());
       return (deck.size() < 3 && level == deck.size() + 1);                                                            //equivalent to: if(deck.size() >= 3) return false; else if(deck.size() + 1 = level) return true;
    }

    public boolean checkUpperCard (DevCard devCard){
        boolean check = false;
        for(ArrayList <DevCard> deck : devSpace){
            if(deck.get(0).equals(devCard)) {
                check = true;
                break;
            }
        }
        return check;
    }

}
