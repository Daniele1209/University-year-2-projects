package Repository;
import Model.PrgState;

public interface IRepo {
    void addPrg(PrgState newPrg);
    PrgState getCrtPrg();

}
