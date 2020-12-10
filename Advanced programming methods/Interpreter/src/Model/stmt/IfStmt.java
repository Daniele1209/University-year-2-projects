package Model.stmt;

import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Type.BoolType;
import Model.Type.IType;
import Model.Value.BoolValue;
import Model.Value.IValue;
import Model.adt.IDict;
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
        IValue val = expression.eval(program_state.getSymTable(), program_state.getHeap());

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
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnvironment) throws STMTException, EXPException {
        IType expType = expression.typecheck(typeEnvironment);
        if (expType.equals(new BoolType())) {
            then_statement.typecheck(typeEnvironment);
            else_statement.typecheck(typeEnvironment);
            return typeEnvironment;
        }
        else {
            throw new STMTException("Condition is not a boolean");
        }
    }

    @Override
    public String toString() {
        return "if(" + expression + ") then { " + then_statement + " } else { " + else_statement + " }";
    }
}
