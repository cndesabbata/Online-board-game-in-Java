package it.polimi.ingsw.client.view;

import java.util.List;
import java.util.Map;

public class GameBoardInfo {
    private int position;
    private Map<Integer, String> papalCards;
    private Map<String, Integer> chest;
    private Map<Integer, List<String>> warehouse;
    private List<LeadCardInfo> playedCards;
    private Map<Integer, List<DevCardInfo>> devSpace;

}
