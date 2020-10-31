package Model.stmt;

import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;

public class NopStmt implements IStmt{

    @Override
    public PrgState execute(PrgState program_state) throws STMTException, EXPException {
        return program_state;
    }
}
