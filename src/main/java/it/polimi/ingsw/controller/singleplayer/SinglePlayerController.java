package it.polimi.ingsw.controller.singleplayer;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.messages.CustomMessage;
import it.polimi.ingsw.controller.messages.actions.Action;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.gameboard.GameBoard;
import it.polimi.ingsw.server.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SinglePlayerController extends GameController {
    List<SoloActionToken> tokens;
    boolean win;

    public SinglePlayerController(Server server) {
        super(server);
        tokens = new ArrayList<>();
        tokens.add(new UpdateAndShuffle(this));
        tokens.add(new UpdateItinerary(this));
        for (Colour c : Colour.values()){
            tokens.add(new DiscardDevCard(this, c));
        }
        Collections.shuffle(tokens);
    }

    public List<SoloActionToken> getTokens() {
        return tokens;
    }

    public void makeTokenAction(){
        tokens.add(tokens.remove(0));
        tokens.get(tokens.size()-1).doSoloAction();
    }

    @Override
    public void makeAction(Action action) {
        getActivePlayers().get(0).setActionDone(action.doAction(getActivePlayers().get(0)));
        checkAllPapalReports();
        checkEndGame();
    }

    @Override
    public void checkEndGame() {
        List<DevDeck> d;
        for (Colour c : Colour.values()){
            d = Arrays.asList(getGame().getDevDecks());
            d.removeIf(DevDeck-> DevDeck.isEmpty()||DevDeck.getColour()!=c);
            if (d.isEmpty()){
                win = false;
                endGame();
            }
        }
        GameBoard board = getActivePlayers().get(0).getBoard();
        if (board.getItinerary().getBlackCrossPosition() == 24){
            win = true;
            endGame();
        }
        if (board.getItinerary().getPosition() == 24 || board.getDevSpace().countCards() == 7){
            win = true;
            endGame();
        }
    }

    @Override
    public void setup() {

    }

    @Override
    public void endGame() {
        if (!win){
            getActiveConnections().get(0).sendSocketMessage(new CustomMessage("You lost the game: Lorenzo has won!"));
            setStarted(-1);
        }
        else{
            int score = calculateScore (getActivePlayers().get(0));
            getActiveConnections().get(0).sendSocketMessage(new CustomMessage("You won the game! Your score is " + score));
            setStarted(-1);
        }
    }

    private int calculateScore(Player player){
        int score = addItineraryVP(player) + addPapalVP(player) + addLeaderVP(player) + addDevCardVP(player);
        score += player.getBoard().getTotalResources()/5;
        return score;
    }

    private int addItineraryVP (Player player){
        int result = 0;
        int itineraryVP[] = {1,2,4,6,9,12,16,20};
        for (int i = 0; i < itineraryVP.length; i++){
            if (player.getBoard().getItinerary().getPosition() >= (i+1)*3) result += itineraryVP[i];
        }
        return result;
    }

    private int addPapalVP (Player player){
        int result = 0;
        CardStatus[] papalCardStatus = player.getBoard().getItinerary().getCardStatus();
        for (int i = 0; i < 3; i++){
            if (papalCardStatus[i] == CardStatus.FACE_UP) result += i+2;
        }
        return result;
    }

    private int addLeaderVP (Player player){
        int result = 0;
        for (LeaderCard leaderCard : player.getHandLeaderCards()) {
            if (leaderCard.isPlayed())
                result += leaderCard.getVictoryPoints();
        }
        return result;
    }

    private int addDevCardVP (Player player){
        int result = 0;
        List<List<DevCard>> playerDevCards = player.getBoard().getDevSpace().getCards();
        for (List<DevCard> devSlotCard : playerDevCards) {
            for (DevCard devCard : devSlotCard) {
                result += devCard.getVictoryPoints();
            }
        }
        return result;
    }

}
