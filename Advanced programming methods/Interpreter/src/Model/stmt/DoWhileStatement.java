package Model.stmt;

import Model.Exceptions.ADTException;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Type.BoolType;
import Model.Type.IType;
import Model.Value.BoolValue;
import Model.Value.IValue;
import Model.adt.IDict;
import Model.adt.IHeap;
import Model.adt.IStack;
import Model.exp.Exp;

public class DoWhileStatement implements IStmt {
    Exp expression;
    IStmt statement;

    public DoWhileStatement(Exp e, IStmt s) {
        expression = e;
        statement = s;
    }

    @Override
    public PrgState execute(PrgState program_state) throws EXPException, ADTException {
        IDict<String, IValue> symTable = program_state.getSymTable();
        IStack<IStmt> stack = program_state.getStack();
        IHeap<IValue> heap = program_state.getHeap();

        IValue result = expression.eval(symTable, heap);

        BoolValue bool = (BoolValue) result;

        if(!bool.getValue()) {
            stack.push(this);
        }

        stack.push(statement);

        if(bool.getValue()) {
            stack.pop();
        }

        return null;
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnvironment) throws STMTException, EXPException {
        IType expType = expression.typecheck(typeEnvironment);
        if (expType.equals(new BoolType())) {
            statement.typecheck(typeEnvironment);
            return typeEnvironment;
        }
        else {
            throw new STMTException(this.toString() + " not a Boolean!");
        }
    }

    @Override
    public String toString() {
        return "do {\n\t" + statement + "\n} " +  "while ( " + expression + " )";
    }
}
