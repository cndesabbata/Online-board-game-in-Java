package it.polimi.ingsw.model;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LeaderDeck {
    private final List<LeaderCard> cards = new LinkedList<>();

    public LeaderDeck() {
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(Game.class.getResourceAsStream("/Leaders.json"));
        LeaderCardBlueprint[] cardBlueprints = gson.fromJson(reader, LeaderCardBlueprint[].class);
        for (LeaderCardBlueprint blueprint : cardBlueprints){
            this.cards.add(blueprint.BuildCard());
        }
        Collections.shuffle(cards);
    }

    public LeaderCard drawCard() {
        return cards.remove(0);
    }
}
