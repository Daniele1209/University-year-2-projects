package Model.stmt;

import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Type.IType;
import Model.Value.IValue;
import Model.adt.IDict;
import Model.adt.IList;
import Model.adt.IStack;
import Model.exp.Exp;

public class PrintStmt implements IStmt{
    Exp expression;

    public PrintStmt(Exp exp) {
        expression = exp;
    }

    @Override
    public PrgState execute(PrgState program_state) throws STMTException, EXPException {
        IStack<IStmt> stk = program_state.getStack();
        IList<IValue> out = program_state.getOut();

        out.add(expression.eval(program_state.getSymTable(), program_state.getHeap()));
        program_state.setExeStack(stk);
        program_state.setOut(out);

        return program_state;
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnvironment) throws STMTException, EXPException {
        expression.typecheck(typeEnvironment);
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "print(" + expression.toString() + ")";
    }
}
