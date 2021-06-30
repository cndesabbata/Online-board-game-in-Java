package it.polimi.ingsw.server.model.gameboard;

import it.polimi.ingsw.messages.serverMessages.newElement.NewChest;
import it.polimi.ingsw.messages.serverMessages.newElement.NewDevSpace;
import it.polimi.ingsw.server.model.DevCard;
import it.polimi.ingsw.server.observer.Observable;

import java.util.*;

/**
 * Class DevSpace represents the development space on the game board.
 *
 */
public class DevSpace extends Observable {
    private final List<List<DevCard>> devSpace;
    private final String owner;

    /**
     * Default constructor.
     *
     * @param nickname the nickname of the player who owns this DevSpace object
     */
    public DevSpace (String nickname){
        owner = nickname;
        devSpace = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            devSpace.add(new ArrayList<>());
        }
        notifyObservers(new NewDevSpace(devSpace, owner));
    }

    /**
     * Notifies a player's virtual view with a {@link NewDevSpace} message. Used when a
     * player is reconnecting to a game.
     *
     * @param nickname the nickname of the player to notify
     */
    public void notifyNew(String nickname){
        notifySingleObserver(new NewDevSpace(devSpace, owner), nickname);
    }

    /**
     * Checks if the player has a development card with the same colour and with
     * a level equal or greater than the one provided.
     *
     * @param requirement the required development card
     * @return {@code true} if the player has a development card that satisfies the requirement, {@code false} otherwise
     */
    public boolean checkCards (DevCard requirement){
        if (requirement.getLevel() == 0)
            return devSpace.stream().flatMap(Collection::stream).anyMatch(dc -> dc.getColour() == requirement.getColour());         //it transforms the devspace into a stream of stacks
        else
            return devSpace.stream().flatMap(Collection::stream)
                    .anyMatch(dc -> dc.getColour() == requirement.getColour() && dc.getLevel() == requirement.getLevel());
    }

    /**
     * Adds a development card on top of the selected slot in the development space and
     * notifies all players' virtual views with a {@link NewDevSpace} message.
     *
     * @param card the card to add in the slot
     * @param slot the desired slot
     */
    public void addCard (DevCard card, DevSpaceSlot slot){
        devSpace.get(slot.ordinal()).add(0,card);
        notifyObservers(new NewDevSpace(devSpace, owner));
    }

    /**
     * Checks if a selected slot has room for a card of a specified level.
     *
     * @param level the specified level
     * @param slot  the development slot to check
     * @return {@code true} if there is a place for a cord of that level, {@code false} otherwise
     */
    public boolean checkPlace(int level, DevSpaceSlot slot){
       List<DevCard> deck = devSpace.get(slot.ordinal());
       return (deck.size() < 3 && level == deck.size() + 1);                                                            //equivalent to: if(deck.size() >= 3) return false; else if(deck.size() + 1 = level) return true;
    }

    /**
     * Returns the list of lists of development cards contained in the development space.
     *
     * @return the list of lists of development cards
     */
    public List<List<DevCard>> getCards() {
        return devSpace;
    }

    /**
     * Returns the number of development cards in the development space.
     *
     * @return the number of development cards in the development space
     */
    public int countCards() {
        return devSpace.stream().map(List::size).reduce(0, Integer::sum);                                               //equivalent to: devSpace.stream().map(deck -> deck.size()).reduce(0, (a,b) -> a + b);
    }

}
