package it.polimi.ingsw.model;
import java.util.*;

public class Wharehouse {
    private  ArrayList<ResourceQuantity> wharehouse = new ArrayList<ResourceQuantity>();

    public Wharehouse(){
        for(int i = 0; i < NumOfShelf.values().length - 1; i++){
            wharehouse.add(new ResourceQuantity(0, Resource.EMPTY));
        }
    }

    public ArrayList<ResourceQuantity> getWharehouse(){
        return new ArrayList<ResourceQuantity>(wharehouse);
    }

    public ResourceQuantity getShelf(NumOfShelf numOfShelf) {
        ResourceQuantity shelf = wharehouse.get(numOfShelf.ordinal());
        return new ResourceQuantity(shelf.getQuantity(), shelf.getResource());
    }

    public void addResource (Resource resource, NumOfShelf numOfShelf)  {
        ResourceQuantity shelf = wharehouse.get(numOfShelf.ordinal());
        if(shelf.getResource() != Resource.EMPTY && shelf.getResource() != resource)
        {/*gestisci caso risorse diverse nello stesso scaffale*/}
        else if(shelf.getQuantity() == 1 + numOfShelf.ordinal())
        { /*gestisci caso scaffale pieno*/ }
        else if(!this.checkOtherShelves(resource, numOfShelf))
        {/*gestisci caso altri scaffali hanno stessa risorsa*/}
        else shelf.setQuantity(shelf.getQuantity() + 1);
    }

    public void removeResource (Resource resource)  {
        NumOfShelf numOfShelf = this.findShelf(resource);
        if(numOfShelf == NumOfShelf.NOT_FOUND)
            { /*gestisci caso scaffale non trovato*/}
        else {
            ResourceQuantity shelf = wharehouse.get(numOfShelf.ordinal());
            if (shelf.getQuantity() == 1) shelf.setResource(Resource.EMPTY);
            shelf.setQuantity(shelf.getQuantity() - 1);
        }
    }

    public NumOfShelf findShelf (Resource resource){
        int i;
        for(i = 0; i < wharehouse.size(); i++){
            if(wharehouse.get(i).getResource() == resource)
                break;
        }
        return NumOfShelf.values()[i];
    }

    public boolean checkOtherShelves(Resource resource, NumOfShelf numOfShelf){
        boolean check = true;
        for(int i = 0; i < wharehouse.size(); i++){
            if(i != numOfShelf.ordinal()){
                if(wharehouse.get(i).getResource() == resource) {
                    check = false;
                    break;
                }
            }
        }
        return check;
    }

    public boolean checkQuantity(Resource resource, int quantity){        //con questo metodo non sono più necessarie le eccezioni nelle delete
        NumOfShelf numOfShelf = this.findShelf(resource);
        ResourceQuantity shelf = wharehouse.get(numOfShelf.ordinal());
        return shelf.getQuantity() >= quantity;
    }
}