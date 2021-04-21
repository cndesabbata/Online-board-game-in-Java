package it.polimi.ingsw.model;

import java.util.*;


public class DevSpace {
    private final List<List<DevCard>> devSpace;

    public DevSpace() {
        devSpace = new ArrayList<>();
    }

    public boolean checkCards(DevCard requirement) {
        boolean check = false;
        for (List<DevCard> stack : devSpace) {
            for (DevCard card : stack) {
                if (card.equals(requirement))
                    check = true;
            }
        }
        return check;
    }

    public List<List<DevCard>> getDevCards() {
        return devSpace;
    }

    public void addCard(DevCard card, DevSpaceSlot slot) {
        devSpace.get(slot.ordinal()).add(0, card);
    }

    public int countDevCards() {
        int sum = 0;
        for(List<DevCard> devSlot: devSpace) {
            for(DevCard devCard : devSlot) {
                sum++;
            }
        }
        return sum;
    }

    public boolean checkPlace(int level, DevSpaceSlot slot) {
        List<DevCard> deck = devSpace.get(slot.ordinal());
        return (deck.size() < 3 && level == deck.size() + 1);                                                            //equivalent to: if(deck.size() >= 3) return false; else if(deck.size() + 1 = level) return true;
    }

    public boolean checkUpperCard(DevCard devCard) {
        boolean check = false;
        for (List<DevCard> deck : devSpace) {
            if (deck.get(0).equals(devCard)) {
                check = true;
                break;
            }
        }
        return check;
    }

}
