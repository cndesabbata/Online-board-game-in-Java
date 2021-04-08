package it.polimi.ingsw.model;
import java.util.*;

public class Wharehouse {
    private final ArrayList<Resource> firstShelf = new ArrayList<>();   //non è meglio usare un array lungo 3 i cui elementi sono dei ResourceReq?
    private final ArrayList<Resource> secondShelf = new ArrayList<>();
    private final ArrayList<Resource> thirdShelf = new ArrayList<>();

    public ArrayList<Resource> getFirstShelf() {
        return new ArrayList<Resource>(firstShelf);
    }

    public ArrayList<Resource> getSecondShelf() {
        return new ArrayList<Resource>(secondShelf);
    }

    public ArrayList<Resource> getThirdShelf() {
        return new ArrayList<Resource>(thirdShelf);
    }

    public void addFirstShelf(Resource resource)  throws FullShelfException, WrongDispositionException {
        if (firstShelf != null && firstShelf.size() == 1) throw new FullShelfException();
        else if (secondShelf != null && secondShelf.get(0) == resource) throw new WrongDispositionException();
        else if (thirdShelf != null && thirdShelf.get(0) == resource)   throw new WrongDispositionException();
        else firstShelf.add(resource);
    }

    public void deleteFirstShelf() {
        firstShelf.remove(0);
    }

    public int findShelf (Resource resource){
        if (firstShelf.size() > 0) { if (firstShelf.get(0) == resource) return 1;}
        if (secondShelf.size() > 0) { if (secondShelf.get(0) == resource) return 2;}
        if (thirdShelf.size() > 0) { if (thirdShelf.get(0) == resource) return 3;}
    }

    public void addSecondShelf(Resource resource)  throws FullShelfException, WrongDispositionException {
        if (secondShelf != null && secondShelf.size() == 2) throw new FullShelfException();
        else if (firstShelf != null && firstShelf.get(0) == resource) throw new WrongDispositionException();
        else if (secondShelf != null && secondShelf.get(0) != resource) throw new WrongDispositionException();
        else if (thirdShelf != null && thirdShelf.get(0) == resource) throw new WrongDispositionException();
        else secondShelf.add(resource);
    }

    public void deleteSecondShelf() {
        secondShelf.remove(0);
    }

    public void addThirdShelf(Resource resource)  throws FullShelfException, WrongDispositionException {
        if (thirdShelf.size() == 3 ) throw new FullShelfException();
        else if (firstShelf != null && firstShelf.get(0) == resource) throw new WrongDispositionException();
        else if (secondShelf != null && secondShelf.get(0) == resource) throw new WrongDispositionException();
        else if (thirdShelf != null && thirdShelf.get(0) != resource) throw new WrongDispositionException();
        else thirdShelf.add(resource);
    }

    public void deleteThirdShelf() {
        thirdShelf.remove(0);
    }

    public boolean checkquantity(Resource resource, int quantity){        //con questo metodo non sono più necessarie le eccezioni nelle delete
        int amount = 0;
        if(firstShelf != null){
            if(firstShelf.get(0) == resource) amount += 1;
        }
        if(secondShelf != null){
            for(Resource r : secondShelf){
                if(r == resource) amount += 1;
            }
        }
        if(thirdShelf != null){
            for(Resource r : thirdShelf){
                if(r == resource) amount += 1;
            }
        }
        return amount >= quantity;
    }
}
