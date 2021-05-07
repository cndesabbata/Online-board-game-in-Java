package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.observer.Observable;

import java.util.List;

public class ClientView extends Observable {
    private final CLI cli;
    private final GUI gui;
    private String nickname;
    private String clientMessage;
    private String[][] market;
    private String externalMarble;
    private DevCardInfo[][] devDecks;
    private List<LeadCardInfo> hand;
    private List<GameBoardInfo> otherGameBoards;
    private GameBoardInfo ownGameBoard;

    public ClientView(CLI cli) {
        this.cli = cli;
        gui = null;
    }

    public ClientView(GUI gui) {
        this.gui = gui;
        cli = null;
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

    public String getClientMessage() {
        return clientMessage;
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
