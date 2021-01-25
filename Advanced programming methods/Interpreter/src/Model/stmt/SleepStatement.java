package Model.stmt;

import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Type.IType;
import Model.Type.IntegerType;
import Model.Value.IntegerValue;
import Model.adt.IDict;
import Model.adt.IStack;

public class SleepStatement implements IStmt{
    IntegerValue nr;

    public SleepStatement(IntegerValue number) {
        this.nr = number;
    }

    @Override
    public PrgState execute(PrgState program_state) throws STMTException, EXPException {
        IStack<IStmt> stack = program_state.getStack();

        if (nr.getValue() != 0){
            nr = new IntegerValue(nr.getValue() - 1);
            stack.push(this);
        }

        return null;
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnvironment) throws STMTException, EXPException {
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "sleep( " + nr + " )";
    }
}
