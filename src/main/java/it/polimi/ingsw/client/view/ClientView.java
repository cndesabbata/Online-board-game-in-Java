package it.polimi.ingsw.client.view;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.clientMessages.internal.PrintDevDecks;
import it.polimi.ingsw.messages.clientMessages.internal.PrintHandCards;
import it.polimi.ingsw.messages.clientMessages.internal.PrintMarket;
import it.polimi.ingsw.messages.clientMessages.internal.ViewMessage;
import it.polimi.ingsw.server.controller.GamePhase;
import it.polimi.ingsw.server.observer.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * Class ClientView is a simplified representation of the model.
 *
 */
public class ClientView extends Observable {
    private final Cli cli;
    private final Gui gui;
    private String nickname;
    private String[][] market;
    private String externalMarble;
    private final DevCardInfo[][] devDecks;
    private List<LeadCardInfo> hand;
    private final List<GameBoardInfo> otherGameBoards;
    private final GameBoardInfo ownGameBoard;
    private boolean turnActive;
    private GamePhase gamePhase;

    /**
     * Creates a new ClientView instance. Used by the CLI.
     *
     * @param cli the Cli object associated with this client view.
     */
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

    /**
     * Creates a new ClientView instance. Used by the GUI.
     *
     * @param gui the Guii object associated with this client view.
     */
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

    /**
     * Returns the game phase.
     *
     * @return the game phase
     */
    public GamePhase getGamePhase() {
        return gamePhase;
    }

    /**
     * Sets the game phase.
     *
     * @param gamePhase the new phase of the game
     */
    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    /**
     * Returns the turnActive attribute.
     *
     * @return {@code true} if the player is the current player, {@code false} otherwise
     */
    public boolean isTurnActive() {
        return turnActive;
    }

    /**
     * Sets the turnActive attribute.
     *
     * @param turnActive set to {@code true} if the player is the current player, {@code false} otherwise
     */
    public void setTurnActive(boolean turnActive) {
        this.turnActive = turnActive;
    }

    /**
     * Sets the leader cards in the player's hand.
     *
     * @param hand the new leader cards
     */
    public void setHand(List<LeadCardInfo> hand) {
        this.hand = hand;
        notifyObservers(new PrintHandCards());
    }

    /**
     * Adds a GameBoard object to the list of game boards.
     *
     * @param newBoard the GameBoard object to add
     */
    public void addGameBoard(GameBoardInfo newBoard){
        otherGameBoards.add(newBoard);
    }

    /**
     * Sets the market disposition.
     *
     * @param market  the new market disposition
     * @param toPrint {@code true} if the updated disposition needs to be showed to the player, {@code false} otherwise
     */
    public void setMarket(String[][] market, boolean toPrint) {
        this.market = market;
        if (toPrint) notifyObservers(new PrintMarket());
    }

    /**
     * Sets one of the development cards which are on top of the development decks.
     *
     * @param newCard the new development card
     * @param c       the int that represents the colour of the card
     * @param l       the level of the card
     * @param toPrint {@code true} if the updated decks need to be showed to the player, {@code false} otherwise
     */
    public void setDevDecks(DevCardInfo newCard, int c, int l, boolean toPrint) {
        this.devDecks[l - 1][c] = newCard;
        if (toPrint) notifyObservers(new PrintDevDecks());
    }

    /**
     * Notifies the CLI or the GUI with a {@link ViewMessage}.
     *
     * @param clientMessage the message
     */
    public void setClientMessage(Message clientMessage) {
        notifyObservers(clientMessage);
    }

    /**
     * Sets the external marble.
     *
     * @param externalMarble the new external marble
     */
    public void setExternalMarble(String externalMarble) {
        this.externalMarble = externalMarble;
    }

    /**
     * Sets the player's nickname.
     *
     * @param nickname the player's nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Returns the Cli object associated with this client view.
     *
     * @return the Cli object
     */
    public Cli getCli() {
        return cli;
    }

    /**
     * Returns the Gui object associated with this client view.
     *
     * @return the Gui object
     */
    public Gui getGui() {
        return gui;
    }

    /**
     * Returns the player's nickname.
     *
     * @return the player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Returns the market disposition.
     *
     * @return the market disposition
     */
    public String[][] getMarket() {
        return market;
    }

    /**
     * Returns the external marble.
     *
     * @return the external marble
     */
    public String getExternalMarble() {
        return externalMarble;
    }

    /**
     * Returns the array of DevCardInfo objects which represent the
     * development cards on top of the development decks.
     *
     * @return the array of DevCardInfo objects
     */
    public DevCardInfo[][] getDevDecks() {
        return devDecks;
    }

    /**
     * Returns the list of LeadCardInfo objects which represent the leader cards
     * in the player's hand.
     *
     * @return the list of hand leader cards
     */
    public List<LeadCardInfo> getHand() {
        return hand;
    }

    /**
     * Returns the list of game boards owned by the other players.
     *
     * @return the list of game boards owned by the other players
     */
    public List<GameBoardInfo> getOtherGameBoards() {
        return otherGameBoards;
    }

    /**
     * Returns the player's game board.
     *
     * @return the player's game board
     */
    public GameBoardInfo getOwnGameBoard() {
        return ownGameBoard;
    }
}
