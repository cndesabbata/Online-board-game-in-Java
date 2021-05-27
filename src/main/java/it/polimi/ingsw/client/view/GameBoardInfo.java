package it.polimi.ingsw.client.view;

import it.polimi.ingsw.messages.clientMessages.internal.*;
import it.polimi.ingsw.server.observer.Observable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameBoardInfo extends Observable {
    private String owner;
    private Integer position;
    private final Map<Integer, String> papalCards;
    private final Map<String, Integer> chest;
    private final Map<Integer, List<String>> warehouse;
    private List<LeadCardInfo> playedCards;
    private final Map<Integer, List<DevCardInfo>> devSpace;
    private Integer blackCrossPosition;

    public GameBoardInfo(String nickname, CLI cli) {
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

    public Integer getBlackCrossPosition() {
        return blackCrossPosition;
    }

    public void setBlackCrossPosition(Integer blackCrossPosition) {
        this.blackCrossPosition = blackCrossPosition;
    }

    public String getOwner() {
        return owner;
    }

    public void setPapalCardStatus(int index, String newStatus) {
        papalCards.put(index, newStatus);
    }

    public void setOwner(String owner) { this.owner = owner; }

    public void changeChest(String type, int newQuantity, boolean last){
        chest.put(type, newQuantity);
        if (last) notifyObservers(new PrintChest(owner));
    }

    public void changeWarehouse(int shelf, List<String> resources, boolean last){
        warehouse.put(shelf, resources);
        if (last) notifyObservers(new PrintWarehouse(owner));
    }

    public void setPlayedCards(List<LeadCardInfo> playedCards) {
        this.playedCards = playedCards;
        if (!playedCards.isEmpty()) notifyObservers(new PrintPlayedCards(owner));
    }

    public void changeDevSpace(int slot, List<DevCardInfo> cards, boolean last) {
        devSpace.put(slot, cards);
        if (last) notifyObservers(new PrintDevSpace(owner));
    }

    public void setPosition(Integer position, boolean toPrint) {
        this.position = position;
        if (toPrint) notifyObservers(new PrintItinerary(owner));
    }

    public Integer getPosition() {
        return position;
    }

    public Map<Integer, String> getPapalCards() {
        return papalCards;
    }

    public Map<String, Integer> getChest() {
        return chest;
    }

    public Map<Integer, List<String>> getWarehouse() {
        return warehouse;
    }

    public List<LeadCardInfo> getPlayedCards() {
        return playedCards;
    }

    public Map<Integer, List<DevCardInfo>> getDevSpace() {
        return devSpace;
    }
}
