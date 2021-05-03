package it.polimi.ingsw.server.serverNetwork;

import it.polimi.ingsw.messages.actions.Action;
import it.polimi.ingsw.messages.clientMessages.*;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.messages.serverMessages.ErrorMessage;
import it.polimi.ingsw.messages.serverMessages.PlayersNumberMessage;
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

public class ClientConnection implements Runnable {
    private final Server server;
    private Socket socket;
    private boolean active;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String playerNickname;
    private GameController gameController;

    public ClientConnection(Socket socket, Server server ) {
        this.server = server;
        this.socket = socket;
        active = true;
        try {
            input = new ObjectInputStream(this.socket.getInputStream());
            output = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Error during initialization of the client!");
        }
    }

    public void close(){
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void readInput() throws IOException, ClassNotFoundException {
        Message inputClientMessage = (Message) input.readObject();
        if (inputClientMessage != null) messageHandler(inputClientMessage);
    }

    @Override
    public void run(){
        try {
            while (active) {
                readInput();
            }
            server.removeClient(this);
        } catch (IOException e) {
            if (gameController.getPhase() == GamePhase.STARTED && gameController instanceof MultiPlayerController){
                if (((MultiPlayerController) getGameController()).getCurrentPlayer().getNickname().equals(playerNickname))
                    ((MultiPlayerController) getGameController()).changeTurn();
                server.unregisterClient(this);
            }
            else server.removeClient(this);
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e){
            System.err.println(e.getMessage());
        }
    }

    public void messageHandler(Message clientMessage){
        if (clientMessage instanceof SetNickname){
            if (gameController == null && playerNickname == null){
                try {
                    server.registerClient(((SetNickname) clientMessage).getNickname(), this);
                    playerNickname = ((SetNickname) clientMessage).getNickname();
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
            else sendSocketMessage(new ErrorMessage("Invalid action: you already have a nickname"));
        }
        else if (clientMessage instanceof Reconnect){
            if (gameController == null && playerNickname == null){
                try {
                    server.reconnectClient(((Reconnect) clientMessage).getNickname(), this);
                    playerNickname = ((SetNickname) clientMessage).getNickname();
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
            else sendSocketMessage(new ErrorMessage("Invalid action: you are already connected"));
        }
        else if (clientMessage instanceof SetPlayersNumber){
            if (((SetPlayersNumber) clientMessage).getNumOfPlayers() < 1
                    || ((SetPlayersNumber) clientMessage).getNumOfPlayers() > 4){
                sendSocketMessage(new ErrorMessage("Not a valid input, please provide a number between 1 and 4"));
                sendSocketMessage(new PlayersNumberMessage("Choose the number of players: [1...4]"));
            }
            else server.setTotalPlayers(((SetPlayersNumber) clientMessage).getNumOfPlayers(), this);
        }
        else if (clientMessage instanceof LeaderCardSelection){
            if (checkMessageMultiplayer(GamePhase.SETUP)
                    && ((MultiPlayerController) getGameController()).getCurrentPlayer().getActionDone() == UserAction.SETUP_DRAW){
                if (checkLeadCardSelection((LeaderCardSelection) clientMessage))
                ((MultiPlayerController) getGameController()).initialDiscardLeader(((LeaderCardSelection) clientMessage).getIndexes());
                else sendSocketMessage(new ErrorMessage("Indexes out of bound."));
            }
            else if (checkMessageSinglePlayer(GamePhase.SETUP)
                    && getGameController().getActivePlayers().get(0).getActionDone() == UserAction.SETUP_DRAW){
                if (checkLeadCardSelection((LeaderCardSelection) clientMessage))
                    ((SinglePlayerController) getGameController()).initialDiscardLeader(((LeaderCardSelection) clientMessage).getIndexes());
                else sendSocketMessage(new ErrorMessage("Indexes out of bound."));
            }
            else sendSocketMessage(new ErrorMessage("Not a valid action; the setup phase is ended or it is not yet your turn."));
        }
        else if (clientMessage instanceof ResourceSelection){
            if (checkMessageMultiplayer(GamePhase.SETUP)
                    && ((MultiPlayerController) getGameController()).getCurrentPlayer().getActionDone() == UserAction.SELECT_LEADCARD){
                if (checkResourceSelection((ResourceSelection) clientMessage))
                    ((MultiPlayerController) getGameController()).addInitialResources(((ResourceSelection) clientMessage).getResources());
                else sendSocketMessage(new ErrorMessage("Invalid resources choice."));
            }
            else if (checkMessageSinglePlayer(GamePhase.SETUP)
                    && getGameController().getActivePlayers().get(0).getActionDone() == UserAction.SELECT_LEADCARD){
                if (checkResourceSelection((ResourceSelection) clientMessage))
                    ((SinglePlayerController) getGameController()).startMatch();
                else sendSocketMessage(new ErrorMessage("Invalid resources choice."));
            }
            else sendSocketMessage(new ErrorMessage("Not a valid action; the setup phase is ended or it is not yet your turn."));
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
            else sendSocketMessage(new ErrorMessage("Not a valid action; you cannot end your turn at the moment."));
        }
        else if (clientMessage instanceof Action){
            if (checkMessageMultiplayer(GamePhase.STARTED) || checkMessageSinglePlayer(GamePhase.STARTED)){
                try{
                    ((Action) clientMessage).checkAction(((MultiPlayerController) getGameController()).getCurrentPlayer());
                } catch (WrongActionException e){
                    sendSocketMessage(new ErrorMessage(e.getMessage()));
                }
                getGameController().makeAction((Action) clientMessage);
            }
            else sendSocketMessage(new ErrorMessage("Not a valid action; please wait for your turn."));
        }
    }

    private boolean checkMessageMultiplayer (GamePhase phase){
        return getGameController() instanceof MultiPlayerController
                && getGameController().getPhase() == phase
                && isPlayerTurn();
    }

    private boolean checkMessageSinglePlayer (GamePhase phase){
        return getGameController() instanceof SinglePlayerController
                && getGameController().getPhase() == phase;
    }

    private boolean isPlayerTurn(){
        return playerNickname.equals(((MultiPlayerController) getGameController()).getCurrentPlayer().getNickname());
    }

    private boolean checkLeadCardSelection (LeaderCardSelection message){
        return message.getIndexes()[0] > 0 && message.getIndexes()[0] < 5
                && message.getIndexes()[1] > 0 && message.getIndexes()[1] < 5;
    }

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
        switch (getGameController().getActivePlayers().indexOf(((MultiPlayerController) getGameController()).getCurrentPlayer())){
            case 0: return message.getResources().size() == 0;
            case 3: return message.getResources().size() == 2;
            default: return message.getResources().size() == 1;
        }
    }

    public void sendSocketMessage(Message message){
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            close();
        }
    }

    public String getPlayerNickname() {
        return playerNickname;
    }
}
