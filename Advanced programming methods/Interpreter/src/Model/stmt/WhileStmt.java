package Model.stmt;

import Model.Exceptions.ADTException;
import Model.Exceptions.Custom_Exception;
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

import java.time.chrono.IsoChronology;

public class WhileStmt implements IStmt {
    Exp expression;
    IStmt statement;

    public WhileStmt(Exp e, IStmt s) {
        expression = e;
        statement = s;
    }

    @Override
    public PrgState execute(PrgState program_state) throws STMTException, EXPException, ADTException, Custom_Exception {
        IDict<String, IValue> symTable = program_state.getSymTable();
        IStack<IStmt> stack = program_state.getStack();
        IHeap<IValue> heap = program_state.getHeap();
        IValue value = expression.eval(symTable, heap);

        BoolValue bool = (BoolValue) value;

        if(bool.getValue() == true) {
            stack.push(this);
            stack.push(statement);
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
            throw new STMTException(this.toString() + " is not a boolean !");
        }
    }

    @Override
    public String toString() {
        return "while (" + expression + ") " + statement;
    }
}
