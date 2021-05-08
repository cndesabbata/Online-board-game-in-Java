package it.polimi.ingsw.client.clientNetwork;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.client.view.GameBoardInfo;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.clientMessages.DisplayMessage;
import it.polimi.ingsw.messages.clientMessages.RequestPlayersNumber;
import it.polimi.ingsw.messages.clientMessages.YourTurn;
import it.polimi.ingsw.messages.clientMessages.NewView;
import it.polimi.ingsw.messages.newElement.ChangeMessage;
import it.polimi.ingsw.messages.newElement.NewChest;
import it.polimi.ingsw.messages.serverMessages.*;
import it.polimi.ingsw.server.model.ResourceQuantity;

public class MessageHandler {
    private final ClientView view;

    public MessageHandler(ClientView view) {
        this.view = view;
    }

    public void process(Message message){
        if (message instanceof ErrorMessage){
            view.setClientMessage(new DisplayMessage("ERROR:" + ((ErrorMessage) message).getMessage()));
        }
        else if (message instanceof SetupMessage || message instanceof Disconnection){
            view.setClientMessage(new DisplayMessage(((CustomMessage) message).getMessage()));
        }
        else if (message instanceof PlayersNumberMessage){
            view.setClientMessage(new RequestPlayersNumber(((PlayersNumberMessage) message).getMessage()));
        }
        else if (message instanceof ChangesDone){
            ChangesDone m = (ChangesDone) message;
            for (ChangeMessage a : m.getNewElements()){
                applyChanges(a);
            }
            String toPrint = "";
            if (!m.getNickname().equals(view.getNickname())) toPrint = m.getNickname() + m.getType().toString();
            view.setClientMessage(new NewView(toPrint + " This is the new state of the game."));
        }
        else if (message instanceof TurnChange){
            TurnChange m = (TurnChange) message;
            if (m.getNewPlayer().equals(view.getNickname())){
                String toPrint = "";
                if (m.getOldPlayer() != null)
                    toPrint = m.getOldPlayer() + "has ended his turn. ";
                view.setClientMessage(new YourTurn(toPrint +
                        "It's your turn. Please choose an action (select a number between 0 and 9):\n" +
                        Constants.getChooseAction()));
            }
            else {
                view.setClientMessage(new DisplayMessage("It's " + m.getNewPlayer() +
                        "'s turn. Pleas wait for him/her to make an action..."));
            }
        }
    }

    private GameBoardInfo findBoardByOwner(String owner){
        if (view.getNickname().equals(owner)) return view.getOwnGameBoard();
        for (GameBoardInfo g : view.getOtherGameBoards()){
            if (g.getOwner().equals(owner)) return g;
        }
        GameBoardInfo newBoard = new GameBoardInfo(owner);
        view.addGameBoard(newBoard);
        return newBoard;
    }

    private void applyChanges(ChangeMessage m){
        if (m instanceof NewChest){
            NewChest c = (NewChest) m;
            for (ResourceQuantity r : c.getChest())
                findBoardByOwner(c.getOwner()).newChest(r.getResource().toString(), r.getQuantity());
        }
    }
}
