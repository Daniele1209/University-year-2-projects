package Model.stmt;
import Model.Exceptions.ADTException;
import Model.Exceptions.Custom_Exception;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Type.IType;
import Model.adt.IDict;

public interface IStmt {
    public PrgState execute(PrgState program_state) throws STMTException, EXPException, ADTException, Custom_Exception;
    IDict<String, IType> typecheck(IDict<String, IType> typeEnvironment) throws STMTException, EXPException;
}
