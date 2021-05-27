package it.polimi.ingsw.client.clientNetwork;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.client.view.DevCardInfo;
import it.polimi.ingsw.client.view.GameBoardInfo;
import it.polimi.ingsw.client.view.LeadCardInfo;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.clientMessages.internal.*;
import it.polimi.ingsw.messages.serverMessages.newElement.*;
import it.polimi.ingsw.messages.serverMessages.*;
import it.polimi.ingsw.server.controller.UserAction;
import it.polimi.ingsw.server.model.DevCard;
import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.ResourceQuantity;

import java.util.ArrayList;
import java.util.List;

public class MessageHandler {
    private final ClientView view;

    public MessageHandler(ClientView view) {
        this.view = view;
    }

    public void process(Message message){
        if (message instanceof ErrorMessage){
            ErrorMessage e = (ErrorMessage) message;
            if ((e.getErrorType() == ErrorType.PLAYER_NUMBER))
                view.setClientMessage(new RequestPlayersNumber(e.getMessage()));
            else if (e.getErrorType() == ErrorType.WRONG_ACTION)
                view.setClientMessage(new ChooseAction(e.getMessage()
                                    + "Please choose an action (select a number between 0 and 11):\n" +
                                    Constants.getChooseAction() +  "\n>"));
            else if (e.getErrorType() == ErrorType.WRONG_MESSAGE)
                view.setClientMessage(new DisplayMessage(e.getMessage()));
            else if(e.getErrorType() == ErrorType.INVALID_END_TURN)
                view.setClientMessage(new ChooseAction(e.getMessage() +
                        Constants.getChooseAction() +  "\n>"));
            else if (e.getErrorType() == ErrorType.SETUP_DRAW)
                view.setClientMessage(new SetupDiscard(e.getMessage()));
            else if (e.getErrorType() == ErrorType.SETUP_RESOURCE)
                view.setClientMessage(new SetupResources(e.getMessage()));
            else if (e.getErrorType() == ErrorType.SOCKET_ERROR)
                view.setClientMessage(new DisplayMessage(e.getMessage()));
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
                applyChanges(a, !(m.getType() == UserAction.INITIAL_DISPOSITION) && isLast(m.getNewElements(), a));
            }
            if(m.getType() == UserAction.INITIAL_DISPOSITION)
                view.setClientMessage(new NewView("This is the initial disposition.\n"));
            else if (m.getType() == UserAction.SETUP_DRAW){
                view.setClientMessage(new SetupDiscard("These are your new four leader cards. Please select the indexes of the two you wish to discard:\n>"));
            }
            else if (m.getType() == UserAction.SELECT_LEADCARD) {
                view.setClientMessage(new DisplayMessage("These are the cards you chose."));
                view.setPlayerIndex(((NewIndex) m.getNewElements().get(1)).getPlayerIndex());
                String string = switch (view.getPlayerIndex()) {
                    case 1 -> "You are the second player; this gives you access to an additional resource" +
                            " of your choice; please write your choice below:" +
                            " [Coin/Stone/Servant/Shield]";
                    case 2 -> "You are the third player; this gives you access to an additional faith point and" +
                            " a resource of your choice; please write your choice below:" +
                            " [Coin/Stone/Servant/Shield]";
                    case 3 -> "You are the fourth player; this gives you access to an additional faith point and" +
                            " two resource of your choice; please write your choices below:" +
                            " [Coin/Stone/Servant/Shield]";
                    default -> "";
                };
                if (view.getPlayerIndex() != 0)
                    view.setClientMessage(new SetupResources(string + "\n>"));
            }
            else{
                String toPrint = "";
                if (!m.getNickname().equals(view.getNickname())) toPrint = m.getNickname() + m.getType().toString() + " ";
                view.setClientMessage(new DisplayMessage(toPrint + "This is the new state of the game."));
                view.setClientMessage(new ChooseAction("Please choose an action (select a number between 0 and 11):\n" +
                                                Constants.getChooseAction() +  "\n>"));
            }
        }
        else if (message instanceof TurnChange){
            TurnChange m = (TurnChange) message;
            if (m.getNewPlayer().equals(view.getNickname())){
                view.setTurnActive(true);
                String toPrint = "";
                if (m.getOldPlayer() != null)
                    toPrint = m.getOldPlayer() + "has ended his turn. ";
                view.setClientMessage(new ChooseAction(toPrint +
                        "It's your turn. Please choose an action (select a number between 0 and 11):\n" +
                        Constants.getChooseAction() +  "\n>"));
            }
            else {
                view.setTurnActive(false);
                view.setClientMessage(new DisplayMessage("It's " + m.getNewPlayer() +
                        "'s turn. Pleas wait for him/her to make an action..."));
            }
        }
        else if(message instanceof CloseMessage){
            view.setClientMessage(new DisplayMessage(((CloseMessage) message).getMessage()));
        }
    }

    private GameBoardInfo findBoardByOwner(String owner){
        if (view.getNickname().equals(owner)) return view.getOwnGameBoard();
        for (GameBoardInfo g : view.getOtherGameBoards()){
            if (g.getOwner().equals(owner)) return g;
        }
        GameBoardInfo newBoard = new GameBoardInfo(owner, view.getCli());
        view.addGameBoard(newBoard);
        return newBoard;
    }

    private boolean isLast(List<ChangeMessage> elements, ChangeMessage a){
        if (!(a instanceof NewDevDeck)) return true;
        int lastIndex = 0;
        for (ChangeMessage c : elements){
            if (c instanceof NewDevDeck) lastIndex = elements.indexOf(c);
        }
        return elements.indexOf(a) == lastIndex;
    }

    private void applyChanges(ChangeMessage m, boolean toPrint){
        if (m instanceof NewChest){
            NewChest c = (NewChest) m;
            for (ResourceQuantity r : c.getChest())
                findBoardByOwner(c.getOwner()).changeChest
                        (r.getResource().toString() + "s", r.getQuantity(),
                                c.getChest().indexOf(r) == c.getChest().size() - 1);
        }
        else if (m instanceof NewDevDeck){
            NewDevDeck d = (NewDevDeck) m;
            DevCardInfo c = null;
            if (d.getDeck() != null)
                c = new DevCardInfo(d.getDeck());
            view.setDevDecks(c, d.getColour().ordinal(), d.getLevel(), toPrint);
        }
        else if (m instanceof NewDevSpace){
            NewDevSpace d = (NewDevSpace) m;
            GameBoardInfo g = findBoardByOwner(d.getOwner());
            int slot = 0;
            for (List<DevCard> l : d.getDevSpace()){
                List<DevCardInfo> i = new ArrayList<>();
                for (DevCard c : l)
                    i.add(new DevCardInfo(c));
                g.changeDevSpace(slot, i, slot == 2);
                slot++;
            }
        }
        else if (m instanceof NewHandCards){
            NewHandCards h = (NewHandCards) m;
            GameBoardInfo g = findBoardByOwner(h.getOwner());
            List<LeadCardInfo> played = new ArrayList<>();
            List<LeadCardInfo> hand = new ArrayList<>();
            for (LeaderCard c : h.getHandLeaderCards()){
                if (c.isPlayed()) played.add(new LeadCardInfo(c));
                else hand.add(new LeadCardInfo(c));
            }
            g.setPlayedCards(played);
            view.setHand(hand);
        }
        else if (m instanceof NewIndex){
            view.setPlayerIndex(((NewIndex) m).getPlayerIndex());
        }
        else if (m instanceof NewItinerary){
            NewItinerary i = (NewItinerary) m;
            GameBoardInfo g = findBoardByOwner(i.getOwner());
            g.setBlackCrossPosition(i.getBlackCrossPosition());
            for (int j = 0; j < 3; j++)
                g.setPapalCardStatus(j, i.getCardStatus()[j].toString());
            g.setPosition(i.getPosition(), toPrint);
        }
        else if (m instanceof NewMarket){
            NewMarket k = (NewMarket) m;
            String[][] disposition = new String[3][4];
            for (int i = 0; i < 3; i++){
                for (int j = 0; j < 4; j++)
                    disposition[i][j] = k.getDisposition()[i][j].toString();
            }
            view.setExternalMarble(k.getExternal().toString());
            view.setMarket(disposition, toPrint);
        }
        else if (m instanceof NewWarehouse){
            NewWarehouse w = (NewWarehouse) m;
            GameBoardInfo g = findBoardByOwner(w.getOwner());
            int shelf = 0;
            for (ResourceQuantity r : w.getWarehouse()){
                g.changeWarehouse(shelf, ResourceQuantity.toStringList(ResourceQuantity.flatten(r)),
                        w.getWarehouse().indexOf(r) == w.getWarehouse().size() - 1);
                shelf++;
            }
        }
    }
}
