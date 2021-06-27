package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import it.polimi.ingsw.messages.serverMessages.newElement.NewDevDeck;
import it.polimi.ingsw.server.observer.Observable;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * Class DevDeck represents a deck of development cards.
 *
 */
public class DevDeck extends Observable {
    private final List<DevCard> cards = new ArrayList<>();
    private final Colour colour;
    private final int level;

    /**
     * Creates a DevDeck object of the specified level and colour. It retrieves
     * all the necessary information from the json files. After the cards have been
     * created, the deck is shuffled.
     *
     * @param level  the level of the cards in the deck
     * @param colour the colour of the cards in the deck
     */
    public DevDeck (int level, Colour colour){
        this.level = level;
        this.colour = colour;
        String path = null;
        if (level == 1){
            if (colour == Colour.YELLOW) path = "/json/FirstYellow.json";
            else if (colour == Colour.GREEN) path = "/json/FirstGreen.json";
            else if (colour == Colour.BLUE) path = "/json/FirstBlue.json";
            else if (colour == Colour.PURPLE) path = "/json/FirstPurple.json";
        }
        else if (level == 2){
            if (colour == Colour.YELLOW) path = "/json/SecondYellow.json";
            else if (colour == Colour.GREEN) path = "/json/SecondGreen.json";
            else if (colour == Colour.BLUE) path = "/json/SecondBlue.json";
            else if (colour == Colour.PURPLE) path = "/json/SecondPurple.json";
        }
        else if (level == 3){
            if (colour == Colour.YELLOW) path = "/json/ThirdYellow.json";
            else if (colour == Colour.GREEN) path = "/json/ThirdGreen.json";
            else if (colour == Colour.BLUE) path = "/json/ThirdBlue.json";
            else if (colour == Colour.PURPLE) path = "/json/ThirdPurple.json";
        }
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(Game.class.getResourceAsStream(path));
        DevCardBlueprint[] cardBlueprints = gson.fromJson(reader, DevCardBlueprint[].class);
        for (DevCardBlueprint blueprint : cardBlueprints){
            this.cards.add(blueprint.BuildCard(level, colour));
        }
        Collections.shuffle(cards);
    }

    /**
     * Checks id the deck is empty.
     *
     * @return {@code true} if the deck is empty, {@code false} otherwise
     */
    public boolean isEmpty(){
        return (cards.size()==0);
    }

    public DevCard getFirstCard(){
        if (cards.isEmpty()) return null;
        else return cards.get(0);
    }

    /**
     * Notifies all the players' virtual views with a NewDevDeck message.
     * Used in the setup phase.
     */
    public void notifyNew(){
        notifyObservers(new NewDevDeck(colour, level, cards.get(0)));
    }

    /**
     * Notifies the player's virtual view with a NewDevDeck message.
     * Used when a player is reconnecting to the game.
     *
     * @param nickname the player's nickname
     */
    public void notifyNew (String nickname){
        notifySingleObserver(new NewDevDeck(colour, level, cards.isEmpty() ? null : cards.get(0)), nickname);
    }

    /**
     * Returns the cards in the deck. Used in unit tests only.
     *
     * @return the list of cards in the deck
     */
    public List<DevCard> getCards() {
        return cards;
    }

    /**
     * Returns the colour of the deck.
     *
     * @return the colour of the deck
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Returns the level of the deck.
     *
     * @return the level of the deck
     */
    public int getLevel() {
        return level;
    }

    /**
     * Draws the first card from the deck and notifies all the players'
     * virtual views with a NewDevDeck message containing the new first card
     * of the deck.
     *
     * @return the drawn development card if the deck is not empty, {@code null} otherwise
     */
    public DevCard drawCard() {
        if (cards.isEmpty()) return null;
        else {
            DevCard d = cards.remove(0);
            notifyObservers(new NewDevDeck(colour, level, cards.isEmpty() ? null : cards.get(0)));
            return d;
        }
    }

    /**
     * Returns a copy of the requirements list of the first card of the deck.
     * Used in the checkAction method in BuyDevCard class.
     *
     * @return a copy of the requirements list of the first card or an empty list
     * if the deck is empty
     */
    public List<ResourceQuantity> peepRequirements(){
        if (cards.isEmpty()) return new ArrayList<>();
        List<ResourceQuantity> result = new ArrayList<>();
        for (ResourceQuantity res : cards.get(0).getResourceRequirements()){
            result.add(new ResourcePosition(res.getQuantity(), res.getResource()));
        }
        return result;
    }


}
