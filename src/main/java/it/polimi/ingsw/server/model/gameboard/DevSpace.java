package it.polimi.ingsw.server.model.gameboard;
import it.polimi.ingsw.messages.newElement.NewDevSpace;
import it.polimi.ingsw.server.model.DevCard;
import it.polimi.ingsw.server.observer.Observable;

import java.util.*;


public class DevSpace extends Observable {
    private final List<List<DevCard>> devSpace;
    private final String owner;

    public DevSpace (String nickname){
        owner = nickname;
        devSpace = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            devSpace.add(new ArrayList<>());
        }
        notifyObservers(new NewDevSpace(devSpace, owner));
    }

    /*controls if the player has a DevCard with the same colour of requirement and the same (or greater) level*/
    public boolean checkCards (DevCard requirement){
        return devSpace.stream().flatMap(Collection::stream)                                                            //it transforms the devspace into a stream of stacks
                .anyMatch(dc -> dc.getColour() == requirement.getColour() && dc.getLevel() == requirement.getLevel());
    }

    /*adds a new card "on top" of the deck in the selected slot*/
    public void addCard (DevCard card, DevSpaceSlot slot){
        devSpace.get(slot.ordinal()).add(0,card);
        notifyObservers(new NewDevSpace(devSpace, owner));
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
            if(deck.size()>0 && deck.get(0).equals(devCard)) {
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
