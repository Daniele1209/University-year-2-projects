package Model.stmt;

import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.adt.IStack;

public class CompStmt implements IStmt{
    IStmt first;
    IStmt second;

    public CompStmt(IStmt state1, IStmt state2) {
        first = state1;
        second = state2;
    }

    @Override
    public PrgState execute(PrgState program_state) throws STMTException, EXPException {
        IStack<IStmt> stk = program_state.getStack();
        stk.push(second);
        stk.push(first);
        program_state.setExeStack(stk);

        return program_state;
    }

    @Override
    public String toString() {
        return first.toString() + ";\n" + second.toString();
    }
}
