package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import it.polimi.ingsw.messages.serverMessages.newElement.NewDevDeck;
import it.polimi.ingsw.server.observer.Observable;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class DevDeck extends Observable {
    private final List<DevCard> cards = new ArrayList<>();
    private final Colour colour;
    private final int level;

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
    public boolean isEmpty(){ return (cards.size()==0); }

    public DevCard getFirstCard(){
        if (cards.isEmpty()) return null;
        else return cards.get(0);
    }

    public void notifyNew(){
        notifyObservers(new NewDevDeck(colour, level, cards.get(0)));
    }

    public void notifyNew (String nickname){
        notifySingleObserver(new NewDevDeck(colour, level, cards.isEmpty() ? null : cards.get(0)), nickname);
    }

    /*used only only in tests*/
    public List<DevCard> getCards() {
        return cards;
    }

    public Colour getColour() {
        return colour;
    }

    public int getLevel() {
        return level;
    }

    public DevCard drawCard() {
        if (cards.isEmpty()) return null;
        else {
            DevCard d = cards.remove(0);
            notifyObservers(new NewDevDeck(colour, level, cards.isEmpty() ? null : cards.get(0)));
            return d;
        }
    }

    public List<ResourceQuantity> peepRequirements(){
        if (cards.isEmpty()) return new ArrayList<>();
        List<ResourceQuantity> result = new ArrayList<>();
        for (ResourceQuantity res : cards.get(0).getResourceRequirements()){
            result.add(new ResourcePosition(res.getQuantity(), res.getResource()));
        }
        return result;
    }


}
