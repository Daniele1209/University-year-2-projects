package Model.stmt;

import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Type.IType;
import Model.adt.IDict;

public class NopStmt implements IStmt{

    @Override
    public PrgState execute(PrgState program_state) throws STMTException, EXPException {
        return null;
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnvironment) throws STMTException, EXPException {
        return typeEnvironment;
    }
}
