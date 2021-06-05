package it.polimi.ingsw.client.view;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.clientMessages.internal.PrintDevDecks;
import it.polimi.ingsw.messages.clientMessages.internal.PrintHandCards;
import it.polimi.ingsw.messages.clientMessages.internal.PrintMarket;
import it.polimi.ingsw.server.controller.GamePhase;
import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.observer.Observable;

import java.util.ArrayList;
import java.util.List;

public class ClientView extends Observable {
    private final Cli cli;
    private final Gui gui;
    private String nickname;
    private String[][] market;
    private String externalMarble;
    private DevCardInfo[][] devDecks;
    private List<LeadCardInfo> hand;
    private List<GameBoardInfo> otherGameBoards;
    private GameBoardInfo ownGameBoard;
    private Integer playerIndex;
    private boolean turnActive;
    private GamePhase gamePhase;

    public ClientView(Cli cli) {
        this.cli = cli;
        addObserver(cli);
        gui = null;
        this.devDecks = new DevCardInfo[3][4];
        hand = new ArrayList<>();
        otherGameBoards = new ArrayList<>();
        ownGameBoard = new GameBoardInfo(nickname, cli);
        turnActive = false;
    }

    public Integer getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(Integer playerIndex) {
        this.playerIndex = playerIndex;
    }

    public ClientView(Gui gui) {
        this.gui = gui;
        addObserver(gui);
        cli = null;
        this.devDecks = new DevCardInfo[3][4];
        hand = new ArrayList<>();
        otherGameBoards = new ArrayList<>();
        ownGameBoard = new GameBoardInfo(nickname, gui);
        turnActive = false;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    public boolean isTurnActive() {
        return turnActive;
    }

    public void setTurnActive(boolean turnActive) {
        this.turnActive = turnActive;
    }

    public void setHand(List<LeadCardInfo> hand) {
        this.hand = hand;
        notifyObservers(new PrintHandCards());
    }

    public void addGameBoard(GameBoardInfo newBoard){
        otherGameBoards.add(newBoard);
    }

    public void setMarket(String[][] market, boolean toPrint) {
        this.market = market;
        if (toPrint) notifyObservers(new PrintMarket());
    }

    public void setDevDecks(DevCardInfo newCard, int c, int l, boolean toPrint) {
        this.devDecks[l - 1][c] = newCard;
        if (toPrint) notifyObservers(new PrintDevDecks());
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

    public Cli getCli() {
        return cli;
    }

    public Gui getGui() {
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
