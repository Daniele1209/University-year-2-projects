package Model.stmt;

import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Type.BoolType;
import Model.Value.BoolValue;
import Model.Value.IValue;
import Model.adt.IStack;
import Model.exp.Exp;

public class IfStmt implements IStmt{
    IStmt then_statement;
    IStmt else_statement;
    Exp expression;

    public IfStmt(Exp exp, IStmt statement1, IStmt statement2) {
        expression = exp;
        then_statement = statement1;
        else_statement = statement2;
    }

    @Override
    public PrgState execute(PrgState program_state) throws STMTException, EXPException {
        IStack<IStmt> stk = program_state.getStack();
        IValue val = expression.eval(program_state.getSymTable());

        if(val.getType().equals(new BoolType())) {
            BoolValue bool = (BoolValue) val;
            if(bool.getValue())
                stk.push(then_statement);
            else
                stk.push(else_statement);
        }
        else
            throw new STMTException("Not a boolean !");

        program_state.setExeStack(stk);
        return program_state;
    }

    @Override
    public String toString() {
        return "if(" + expression + ") then { " + then_statement + " } else { " + else_statement + " }";
    }
}
