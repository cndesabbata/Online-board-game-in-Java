package it.polimi.ingsw.client.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameBoardInfo {
    private String owner;
    private Integer position;
    private final Map<Integer, String> papalCards;
    private final Map<String, Integer> chest;
    private final Map<Integer, List<String>> warehouse;
    private List<LeadCardInfo> playedCards;
    private final Map<Integer, List<DevCardInfo>> devSpace;
    private Integer blackCrossPosition;

    public GameBoardInfo(String nickname) {
        this.owner = nickname;
        position = 0;
        papalCards = new HashMap<>();
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

    public void changeChest(String type, int newQuantity){
        chest.put(type, newQuantity);
    }

    public void changeWarehouse(int shelf, List<String> resources){
        warehouse.put(shelf, resources);
    }

    public void setPlayedCards(List<LeadCardInfo> playedCards) {
        this.playedCards = playedCards;
    }

    public void changeDevSpace(int slot, List<DevCardInfo> cards) {
        devSpace.put(slot, cards);
    }

    public void setPosition(Integer position) {
        this.position = position;
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
