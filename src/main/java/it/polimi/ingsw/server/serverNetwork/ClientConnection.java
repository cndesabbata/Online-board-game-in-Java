package it.polimi.ingsw.server.serverNetwork;

import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.messages.clientMessages.*;
import it.polimi.ingsw.messages.clientMessages.JoinLobby;
import it.polimi.ingsw.messages.serverMessages.ErrorType;
import it.polimi.ingsw.messages.serverMessages.PlayersNumberMessage;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.messages.serverMessages.ErrorMessage;
import it.polimi.ingsw.server.controller.GamePhase;
import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.controller.multiplayer.MultiPlayerController;
import it.polimi.ingsw.server.controller.singleplayer.SinglePlayerController;
import it.polimi.ingsw.server.exceptions.WrongActionException;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.ResourcePosition;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class ClientConnection handles the connection between a client and the server.
 * It contains methods to receive and send messages through the socket and other
 * utility methods.
 *
 */
public class ClientConnection implements Runnable {
    private final Server server;
    private final Socket socket;
    private boolean active;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String playerNickname;
    private GameController gameController;

    /**
     * Creates a new ClientConnection object and instantiates the input and output streams.
     *
     * @param socket the socket which accepted the client connection
     * @param server the main server object
     */
    public ClientConnection(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;
        active = true;
        try {
            input = new ObjectInputStream(this.socket.getInputStream());
            output = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error during initialization of the client!");
        }
        this.playerNickname = null;
        this.gameController = null;
    }

    /**
     * Terminates the connection with the client by closing the socket.
     *
     */
    public void close(){
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Returns the game controller associated with this ClientConnection object.
     *
     * @return the associated game controller
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Sets the game controller associated with this ClientConnection object.
     *
     * @param gameController the new game controller
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Reads a message from the client and calls {@link #messageHandler(Message)}
     * method to handle it.
     *
     * @throws IOException when the client is not online anymore
     * @throws ClassNotFoundException when the class of the serializable object cannot be found
     */
    public void readInput() throws IOException, ClassNotFoundException {
        Message inputClientMessage = (Message) input.readObject();
        if (inputClientMessage != null)
            messageHandler(inputClientMessage);
    }

    /**
     * Reads messages from the socket. When the client disconnects, if the game phase is set to
     * STARTED it calls the {@link Server#unregisterClient(ClientConnection)} and changes the
     * if the player was the current player. All the other disconnections are handled by calling
     * the {@link Server#removeClient(ClientConnection)} method.
     *
     * @see Runnable#run()
     */
    @Override
    public void run(){
        try {
            while (active) {
                readInput();
            }
            server.removeClient(this);
        } catch (IOException e) {
            if (playerNickname != null){
                if (gameController!=null && gameController.getPhase() == GamePhase.STARTED){
                    if (gameController instanceof MultiPlayerController &&
                            ((MultiPlayerController) getGameController()).getCurrentPlayer().getNickname().equalsIgnoreCase(playerNickname))
                        ((MultiPlayerController) getGameController()).changeTurn();
                    server.unregisterClient(this);
                }
                else server.removeClient(this);
                System.out.println(playerNickname + " disconnected.");
            }
            else {
                System.out.println("A player disconnected.");
            }
        } catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles a message received from the client by calling different methods and sending different
     * server messages depending on the input provided.
     *
     * @param clientMessage the message received from the client
     */
    public void messageHandler(Message clientMessage){
        if (clientMessage instanceof SetNickname){
            if (gameController == null){
                try {
                    playerNickname = ((SetNickname) clientMessage).getNickname();
                    server.registerClient(playerNickname, this);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
            else sendSocketMessage(new ErrorMessage("Invalid action: you already have a nickname.", ErrorType.WRONG_MESSAGE));
        }
        else if (clientMessage instanceof Reconnect){
            if (gameController == null){
                try {
                    playerNickname = ((Reconnect) clientMessage).getNickname();
                    server.reconnectClient(((Reconnect) clientMessage).getNickname(), this);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
            else sendSocketMessage(new ErrorMessage(
                    "Invalid action: you are already connected.", ErrorType.WRONG_MESSAGE));
        }
        else if (clientMessage instanceof SetPlayersNumber){
            if (((SetPlayersNumber) clientMessage).getNumOfPlayers() < 1
                    || ((SetPlayersNumber) clientMessage).getNumOfPlayers() > 4){
                sendSocketMessage(new ErrorMessage(
                        "Not a valid input, please provide a number between 1 and 4.", ErrorType.PLAYER_NUMBER));
            }
            else server.setTotalPlayers(((SetPlayersNumber) clientMessage).getNumOfPlayers(), this);
        }
        else if (clientMessage instanceof JoinLobby){
            synchronized (server) {
                JoinLobby j = (JoinLobby) clientMessage;
                if (server.getLobbies().stream().noneMatch(L -> L.getOwner().equalsIgnoreCase(j.getLobbyHost()))){
                    if(!server.getLobbies().isEmpty())
                        sendSocketMessage(new PlayersNumberMessage(
                                "Not a valid lobby, please select another one.", server.getLobbies()));
                    else
                        sendSocketMessage(new PlayersNumberMessage(
                                "There is no available lobby.", server.getLobbies()));
                }

                else
                    server.join(this, j.getLobbyHost());
            }
        }
        else if (clientMessage instanceof LeaderCardSelection){
            if (checkMessageMultiplayer(GamePhase.SETUP)
                    && ((MultiPlayerController) getGameController()).getCurrentPlayer().getActionDone() == UserAction.SETUP_DRAW){
                if (checkLeadCardSelection((LeaderCardSelection) clientMessage))
                ((MultiPlayerController) getGameController()).initialDiscardLeader(((LeaderCardSelection) clientMessage).getIndexes());
                else sendSocketMessage(new ErrorMessage("Indexes out of bound. Please choose two numbers between 1 and 4", ErrorType.SETUP_DRAW));
            }
            else if (checkMessageSinglePlayer(GamePhase.SETUP)
                    && getGameController().getActivePlayers().get(0).getActionDone() == UserAction.SETUP_DRAW){
                if (checkLeadCardSelection((LeaderCardSelection) clientMessage))
                    ((SinglePlayerController) getGameController()).initialDiscardLeader(((LeaderCardSelection) clientMessage).getIndexes());
                else sendSocketMessage(new ErrorMessage("Indexes out of bound. Please choose two numbers between 1 and 4", ErrorType.SETUP_DRAW));
            }
            else sendSocketMessage(new ErrorMessage(
                    "Not a valid action; the setup phase is ended or it is not yet your turn.", ErrorType.WRONG_MESSAGE));
        }

        else if (clientMessage instanceof ResourceSelection){
            if (checkMessageMultiplayer(GamePhase.SETUP)
                    && ((MultiPlayerController) getGameController()).getCurrentPlayer().getActionDone() == UserAction.SELECT_LEADCARD){
                if (checkResourceSelection((ResourceSelection) clientMessage))
                    ((MultiPlayerController) getGameController()).addInitialResources(((ResourceSelection) clientMessage).getResources());
                else sendSocketMessage(new ErrorMessage("Invalid resources choice. " +
                        "Please select the correct number and type of resources.", ErrorType.SETUP_RESOURCE));
            }
            else sendSocketMessage(new ErrorMessage(
                    "Not a valid action; the setup phase is ended or it is not yet your turn.", ErrorType.WRONG_MESSAGE));
        }

        else if (clientMessage instanceof EndTurn){
            if (checkMessageMultiplayer(GamePhase.STARTED)
                && ((MultiPlayerController) getGameController()).getCurrentPlayer().isExclusiveActionDone()){
                ((MultiPlayerController) getGameController()).changeTurn();
            }
            else if (checkMessageSinglePlayer(GamePhase.STARTED)
                    && getGameController().getActivePlayers().get(0).isExclusiveActionDone()){
                ((SinglePlayerController) getGameController()).makeTokenAction();
            }
            else sendSocketMessage(new ErrorMessage(
                        "You must do an action before ending your turn.\n"
                                + "Please choose an action", ErrorType.INVALID_END_TURN));
        }

        else if (clientMessage instanceof Action){
            if (checkMessageMultiplayer(GamePhase.STARTED) || checkMessageSinglePlayer(GamePhase.STARTED)){
                try{
                    if(getGameController() instanceof MultiPlayerController)
                        ((Action) clientMessage).checkAction(((MultiPlayerController) getGameController()).getCurrentPlayer());
                    else
                        ((Action) clientMessage).checkAction(getGameController().getGame().getPlayers().get(0));
                    getGameController().makeAction((Action) clientMessage);
                } catch (WrongActionException e){
                    sendSocketMessage(new ErrorMessage(e.getMessage(), ErrorType.WRONG_ACTION));
                }
            }
            else sendSocketMessage(new ErrorMessage(
                    "Not a valid action; please wait for your turn.", ErrorType.WRONG_MESSAGE));
        }
    }

    /**
     * Checks if the the game controller is a multiplayer one, if the game phase
     * is equal to the one provided and if the player is the current player.
     *
     * @param phase the game phase
     * @return {@code true} if the condition is verified, {@code false} otherwise
     */
    private boolean checkMessageMultiplayer (GamePhase phase){
        return getGameController() instanceof MultiPlayerController
                && getGameController().getPhase() == phase
                && isPlayerTurn();
    }

    /**
     * Checks if the the game controller is a single player one and if the game phase
     * is equal to the one provided.
     *
     * @param phase the game phase
     * @return {@code true} if the condition is verified, {@code false} otherwise
     */
    private boolean checkMessageSinglePlayer (GamePhase phase){
        return getGameController() instanceof SinglePlayerController
                && getGameController().getPhase() == phase;
    }


    /**
     * Checks if the current player of the game is the one associated with this ClientConnection object.
     *
     * @return {@code true} if the condition is verified, {@code false} otherwise
     */
    private boolean isPlayerTurn(){
        return playerNickname.equals(((MultiPlayerController) getGameController()).getCurrentPlayer().getNickname());
    }

    /**
     * Checks if the index provided in the leader card selection message are valid.
     *
     * @param message the leader card selection message
     * @return {@code true} if the condition is verified, {@code false} otherwise
     */
    private boolean checkLeadCardSelection (LeaderCardSelection message){
        return message.getIndexes()[0] > 0 && message.getIndexes()[0] < 5
                && message.getIndexes()[1] > 0 && message.getIndexes()[1] < 5;
    }

    /**
     * Checks if the resources provided in the resource selection message are not of type FAITHPOINT
     * and if their amount is the one specified by the game rule.
     *
     * @param message the resource selection message
     * @return {@code true} if the condition is verified, {@code false} otherwise
     */
    private boolean checkResourceSelection(ResourceSelection message){
        for (ResourcePosition r : message.getResources()){
            if (r.getResource() == Resource.FAITHPOINT)
                return false;
        }
        try {
            ((MultiPlayerController) getGameController()).getCurrentPlayer().getBoard().getWarehouse().checkIncrement(message.getResources());
        } catch (WrongActionException e){
            return false;
        }
        return switch (getGameController().getActivePlayers().indexOf(((MultiPlayerController) getGameController()).getCurrentPlayer())) {
            case 0 -> message.getResources().size() == 0;
            case 3 -> message.getResources().size() == 2;
            default -> message.getResources().size() == 1;
        };
    }

    /**
     * Sends a message to client through the socket.
     *
     * @param message to message to send to the client
     */
    public void sendSocketMessage(Message message){
        try {
            output.reset();
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            close();
        }
    }

    /**
     * Returns the nickname of the player associated with this ClientConnection object.
     *
     * @return the player's nickname
     */
    public String getPlayerNickname() {
        return playerNickname;
    }
}
