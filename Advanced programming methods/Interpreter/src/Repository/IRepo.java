package Repository;
import Model.Exceptions.Custom_Exception;
import Model.PrgState;

import java.io.IOException;
import java.util.List;

public interface IRepo {
    void addPrg(PrgState newPrg);
    PrgState getCrtPrg();
    void printState(PrgState program_state) throws Custom_Exception, IOException;
    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> list);

}
