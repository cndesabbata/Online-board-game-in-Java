package it.polimi.ingsw.client.view;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.observer.Observable;

import java.util.ArrayList;
import java.util.List;

public class ClientView extends Observable {
    private final CLI cli;
    private final GUI gui;
    private String nickname;
    private String[][] market;
    private String externalMarble;
    private DevCardInfo[][] devDecks;
    private List<LeadCardInfo> hand;
    private List<GameBoardInfo> otherGameBoards;
    private GameBoardInfo ownGameBoard;
    private Integer playerIndex;

    public ClientView(CLI cli) {
        this.cli = cli;
        addObserver(cli);
        gui = null;
        this.devDecks = new DevCardInfo[4][3];
        hand = new ArrayList<>();
        otherGameBoards = new ArrayList<>();
        ownGameBoard = new GameBoardInfo(nickname);

    }

    public Integer getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(Integer playerIndex) {
        this.playerIndex = playerIndex;
    }

    public ClientView(GUI gui) {
        this.gui = gui;
        cli = null;
    }

    public void setHand(List<LeadCardInfo> hand) {
        this.hand = hand;
    }

    public void addGameBoard(GameBoardInfo newBoard){
        otherGameBoards.add(newBoard);
    }

    public void setMarket(String[][] market) {
        this.market = market;
    }

    public void setDevDecks(DevCardInfo newCard, int c, int l) {
        this.devDecks[c][l] = newCard;
    }

    public void setClientMessage(Message clientMessage) {
        notifyObservers(clientMessage);
    }

    public void setExternalMarble(String externalMarble) {
        this.externalMarble = externalMarble;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public CLI getCli() {
        return cli;
    }

    public GUI getGui() {
        return gui;
    }

    public String getNickname() {
        return nickname;
    }

    public String[][] getMarket() {
        return market;
    }

    public String getExternalMarble() {
        return externalMarble;
    }

    public DevCardInfo[][] getDevDecks() {
        return devDecks;
    }

    public List<LeadCardInfo> getHand() {
        return hand;
    }

    public List<GameBoardInfo> getOtherGameBoards() {
        return otherGameBoards;
    }

    public GameBoardInfo getOwnGameBoard() {
        return ownGameBoard;
    }


}
