package it.polimi.ingsw.model;

import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class DevDeck {
    private final List<DevCard> cards = new LinkedList<>();

    public DevDeck (int level, Colour colour){
        String path = null;
        if (level == 1){
            if (colour == Colour.YELLOW) path = "/FirstYellow.json";
            else if (colour == Colour.GREEN) path = "/FirstGreen.json";
            else if (colour == Colour.BLUE) path = "/FirstBlue.json";
            else if (colour == Colour.PURPLE) path = "/FirstPurple.json";
        }
        else if (level == 2){
            if (colour == Colour.YELLOW) path = "/SecondYellow.json";
            else if (colour == Colour.GREEN) path = "/SecondGreen.json";
            else if (colour == Colour.BLUE) path = "/SecondBlue.json";
            else if (colour == Colour.PURPLE) path = "/SecondPurple.json";
        }
        else if (level == 3){
            if (colour == Colour.YELLOW) path = "/ThirdYellow.json";
            else if (colour == Colour.GREEN) path = "/ThirdGreen.json";
            else if (colour == Colour.BLUE) path = "/ThirdBlue.json";
            else if (colour == Colour.PURPLE) path = "/ThirdPurple.json";
        }
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(Game.class.getResourceAsStream(path));
        DevCardBlueprint[] cardBlueprints = gson.fromJson(reader, DevCardBlueprint[].class);
        for (DevCardBlueprint blueprint : cardBlueprints){
            this.cards.add(blueprint.BuildCard(level, colour));
        }
        Collections.shuffle(cards);
    }

    public DevCard drawCard() {
        return cards.remove(0);
    }
}
