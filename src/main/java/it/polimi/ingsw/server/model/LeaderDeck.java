package it.polimi.ingsw.server.model;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class LeaderDeck represents the deck of leader cards.
 *
 */
public class LeaderDeck {
    private final List<LeaderCard> cards = new ArrayList<>();

    /**
     * Creates the deck of leader cards retrieving all the necessary information
     * from the json file. After all the cards have been created, the deck
     * is shuffled.
     *
     */
    public LeaderDeck() {
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(Game.class.getResourceAsStream("/json/Leaders.json"));
        LeaderCardBlueprint[] cardBlueprints = gson.fromJson(reader, LeaderCardBlueprint[].class);
        for (LeaderCardBlueprint blueprint : cardBlueprints){
            this.cards.add(blueprint.BuildCard());
        }
        Collections.shuffle(cards);
    }

    /**
     * Draws the first card of the deck.
     *
     * @return the drawn leader card
     */
    public LeaderCard drawCard() {
        return cards.remove(0);
    }
}
