package it.polimi.ingsw.client.view;

import it.polimi.ingsw.messages.clientMessages.internal.*;
import it.polimi.ingsw.server.observer.Observable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class GameBoardInfo is a simplified representation of a player's game board.
 *
 */
public class GameBoardInfo extends Observable {
    private String owner;
    private Integer position;
    private final Map<Integer, String> papalCards;
    private final Map<String, Integer> chest;
    private final Map<Integer, List<String>> warehouse;
    private List<LeadCardInfo> playedCards;
    private final Map<Integer, List<DevCardInfo>> devSpace;
    private Integer blackCrossPosition;
    private int index;

    /**
     * Creates a new GameBoardInfo instance. Used by the CLI.
     *
     * @param nickname the owner of the game board
     * @param cli      the Cli object associated with this game board
     */
    public GameBoardInfo(String nickname, Cli cli) {
        this.owner = nickname;
        position = 0;
        papalCards = new HashMap<>();
        addObserver(cli);
        chest = new HashMap<>();
        warehouse = new HashMap<>();
        playedCards = new ArrayList<>();
        devSpace = new HashMap<>();
        blackCrossPosition = null;
        chest.put("Coins", 0);
        chest.put("Stones", 0);
        chest.put("Servants", 0);
        chest.put("Shields", 0);
        for (int i = 0; i < 3; i++){
            warehouse.put(i, new ArrayList<>());
            papalCards.put(i, "Face down");
            devSpace.put(i, new ArrayList<>());
        }
    }

    /**
     * Creates a new GameBoardInfo instance. Used by the GUI.
     *
     * @param nickname the owner of the game board
     * @param gui      the Gui object associated with this game board
     */
    public GameBoardInfo (String nickname, Gui gui){
        this.owner = nickname;
        position = 0;
        papalCards = new HashMap<>();
        addObserver(gui);
        chest = new HashMap<>();
        warehouse = new HashMap<>();
        playedCards = new ArrayList<>();
        devSpace = new HashMap<>();
        blackCrossPosition = null;
        chest.put("Coins", 0);
        chest.put("Stones", 0);
        chest.put("Servants", 0);
        chest.put("Shields", 0);
        for (int i = 0; i < 3; i++){
            warehouse.put(i, new ArrayList<>());
            papalCards.put(i, "Face down");
            devSpace.put(i, new ArrayList<>());
        }
    }

    /**
     * Returns the black cross position.
     *
     * @return the black cross position
     */
    public Integer getBlackCrossPosition() {
        return blackCrossPosition;
    }

    /**
     * Sets the black cross position.
     *
     * @param blackCrossPosition the new black cross position
     */
    public void setBlackCrossPosition(Integer blackCrossPosition) {
        this.blackCrossPosition = blackCrossPosition;
    }

    /**
     * Returns the owner of the game board.
     *
     * @return the owner of the game board
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the new state of a papal card.
     *
     * @param index     the index of the papal card
     * @param newStatus the new state of the card
     */
    public void setPapalCardStatus(int index, String newStatus) {
        papalCards.put(index, newStatus);
    }

    /**
     * Sets the owner of the game board.
     *
     * @param owner the owner of the game board
     */
    public void setOwner(String owner) { this.owner = owner; }

    /**
     * Changes the quantity of one of the stored resources. If the update is the last of a group
     * of changes to the chest, notifies all observers with a {@link PrintChest} message.
     *
     * @param type        the resource type
     * @param newQuantity the new amount stored
     * @param last        {@code true} if the change is the last of a group of changes, {@code false} otherwise
     */
    public void changeChest(String type, int newQuantity, boolean last){
        chest.put(type, newQuantity);
        if (last) notifyObservers(new PrintChest(owner));
    }

    /**
     * Changes a warehouse shelf. If the update is the last of a group of changes to
     * the warehouse, notifies all observers with a {@link PrintWarehouse} message.
     *
     * @param shelf        the shelf to change
     * @param resources    the new resources stored by the shelf
     * @param last         {@code true} if the change is the last of a group of changes, {@code false} otherwise
     */
    public void changeWarehouse(int shelf, List<String> resources, boolean last){
        warehouse.put(shelf, resources);
        if (last) notifyObservers(new PrintWarehouse(owner));
    }

    /**
     * Sets the played leader cards. If list of played cards is not empty, all
     * observers are notified with a {@link PrintPlayedCards} message.
     *
     * @param playedCards the new played leader cards
     */
    public void setPlayedCards(List<LeadCardInfo> playedCards) {
        this.playedCards = playedCards;
        if (!playedCards.isEmpty()) notifyObservers(new PrintPlayedCards(owner));
    }

    /**
     * Changes a slot in the development space. If the update is the last of a group of changes
     * to the development space, notifies all observers with a {@link PrintDevSpace} message.
     *
     * @param slot        the slot to change
     * @param cards       the development cards in the slot
     * @param last        {@code true} if the change is the last of a group of changes, {@code false} otherwise
     */
    public void changeDevSpace(int slot, List<DevCardInfo> cards, boolean last) {
        devSpace.put(slot, cards);
        if (last) notifyObservers(new PrintDevSpace(owner));
    }

    /**
     * Sets the position on the itinerary. If needed, it notifies
     * all the observers with a {@link PrintItinerary} message.
     *
     * @param position the new position
     * @param toPrint  {@code true} if the itinerary needs to be printed, {@code false} otherwise
     */
    public void setPosition(Integer position, boolean toPrint) {
        this.position = position;
        if (toPrint) notifyObservers(new PrintItinerary(owner));
    }

    /**
     * Returns the position on the itinerary.
     *
     * @return the position on the itinerary
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * Returns the map that represents papal cards and their state.
     *
     * @return the map that represents papal cards and their state
     */
    public Map<Integer, String> getPapalCards() {
        return papalCards;
    }

    /**
     * Returns the map that represents the chest.
     *
     * @return the map that represents the chest
     */
    public Map<String, Integer> getChest() {
        return chest;
    }

    /**
     * Returns the map that represents the warehouse.
     *
     * @return the map that represents the warehouse
     */
    public Map<Integer, List<String>> getWarehouse() {
        return warehouse;
    }

    /**
     * Returns the played leader cards.
     *
     * @return the played leader cards
     */
    public List<LeadCardInfo> getPlayedCards() {
        return playedCards;
    }

    /**
     * Returns the map that represents the development space.
     *
     * @return the map that represents the development space
     */
    public Map<Integer, List<DevCardInfo>> getDevSpace() {
        return devSpace;
    }

    /**
     * Returns the index of the player.
     *
     * @return the index of the player
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index of the player.
     *
     * @param index the index of the player
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Checks if the player has the resources provided as parameter.
     *
     * @param requirements the resources
     * @return {@code true} if the player has the resources, {@code false} otherwise
     */
    public boolean totalResourceCheck(List<String> requirements){
        List <String> total = new ArrayList<>();
        for(String s : chest.keySet()){
            String s1 = s.substring(0, s.length() - 1);
            for(int n = chest.get(s); n > 0; n--) {
                total.add(s1);
            }
        }
        for(int n = 0; n < warehouse.size(); n++){
            total.addAll(warehouse.get(n));
        }
        for(String s : chest.keySet()){                                                                                 //it's just an easy way out to not use Resource
            String s1 = s.substring(0, s.length() - 1);
            if(total.stream().filter(t -> t.equalsIgnoreCase(s1)).count() <
                    requirements.stream().filter(t -> t.equalsIgnoreCase(s1)).count())
                return false;
        }
        return true;
    }

    /**
     * Checks if the player has certain development cards.
     *
     * @param requirements the development cards
     * @return {@code true} if the player has the specified development cards, {@code false} otherwise
     */
    public boolean devCardsCheck(List<DevCardInfo> requirements){
        for(DevCardInfo d : requirements){
            if(!devCardCheck(d))
                return false;
        }
        return true;
    }

    /**
     * Checks if the player has certain development card.
     *
     * @param d the development card
     * @return {@code true} if the player has the specified development card, {@code false} otherwise
     */
    private boolean devCardCheck(DevCardInfo d){
        for(int i = 0; i < devSpace.size(); i++){
            if(devSpace.get(i).stream().anyMatch(c -> c.getColour().equalsIgnoreCase(d.getColour()) &&
                    (d.getLevel() == 0 || c.getLevel().equals(d.getLevel()))))
                return true;
        }
        return false;
    }

    /**
     * Checks if the player a certain amount of resources.
     *
     * @param quantity the amount to check
     * @return {@@code true} if the player has the specified amount of resources, {@code false otherwise}
     */
    public boolean totalQuantityCheck(int quantity){
        for(String s : chest.keySet()){
           quantity -= chest.get(s);
        }
        for(int n = 0; n < warehouse.size(); n++){
            quantity -= warehouse.get(n).size();
        }
        return quantity <= 0;
    }
}
