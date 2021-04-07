package it.polimi.ingsw.model;
import java.util.*;

public class Wharehouse {
    private ArrayList<Resource> firstshelf;   //non è meglio usare un array lungo 3 i cui elementi sono dei ResourceReq?
    private ArrayList<Resource> secondshelf;
    private ArrayList<Resource> thirdshelf;

    public ArrayList<Resource> getFirstShelf() {
        return new ArrayList<Resource>(firstshelf);
    }

    public ArrayList<Resource> getSecondShelf() {
        return new ArrayList<Resource>(secondshelf);
    }

    public ArrayList<Resource> getThirdShelf() {
        return new ArrayList<Resource>(thirdshelf);
    }

    public void addFirstShelf(Resource resource)  throws FullShelfException, WrongDispositionException {
        if (firstshelf != null && firstshelf.size() == 1) throw new FullShelfException();
        else if (secondshelf != null && secondshelf.get(0) == resource) throw new WrongDispositionException();
        else if (thirdshelf != null && thirdshelf.get(0) == resource)   throw new WrongDispositionException();
        else firstshelf.add(resource);
    }

    public void deleteFirstShelf()  throws EmptyShelfException {
        if (firstshelf == null)
            throw new EmptyShelfException();
        else
            firstshelf.remove(0);
    }

    public void addSecondShelf(Resource resource)  throws FullShelfException, WrongDispositionException {
        if (secondshelf != null && secondshelf.size() == 2) throw new FullShelfException();
        else if (firstshelf != null && firstshelf.get(0) == resource) throw new WrongDispositionException();
        else if (secondshelf != null && secondshelf.get(0) != resource) throw new WrongDispositionException();
        else if (thirdshelf != null && thirdshelf.get(0) == resource) throw new WrongDispositionException();
        else secondshelf.add(resource);
    }

    public void deleteSecondShelf()  throws EmptyShelfException {
        if (secondshelf == null)
            throw new EmptyShelfException();
        else
            secondshelf.remove(0);
    }

    public void addThirdShelf(Resource resource)  throws FullShelfException, WrongDispositionException {
        if (thirdshelf.size() == 3 ) throw new FullShelfException();
        else if (firstshelf != null && firstshelf.get(0) == resource) throw new WrongDispositionException();
        else if (secondshelf != null && secondshelf.get(0) == resource) throw new WrongDispositionException();
        else if (thirdshelf != null && thirdshelf.get(0) != resource) throw new WrongDispositionException();
        else thirdshelf.add(resource);
    }

    public void deleteThirdShelf()  throws EmptyShelfException {
        if (thirdshelf== null)
            throw new EmptyShelfException();
        else
            thirdshelf.remove(0);
    }
}
