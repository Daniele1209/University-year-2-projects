package Model.stmt;
import Model.Exceptions.ADTException;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;

public interface IStmt {
    public PrgState execute(PrgState program_state) throws STMTException, EXPException, ADTException;
}
