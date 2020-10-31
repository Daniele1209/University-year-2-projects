package Repository;
import Model.PrgState;

import java.util.LinkedList;
import java.util.List;

public class Repo implements IRepo {

    List<PrgState> myPrgStates;

    public Repo() {
        myPrgStates = new LinkedList<>();
    }

    @Override
    public PrgState getCrtPrg() {
        PrgState program_state = myPrgStates.get(0);
        myPrgStates.remove(0);
        return program_state;
    }

    @Override
    public void addPrg(PrgState newPrg) {
        myPrgStates.add(newPrg);
    }
}
