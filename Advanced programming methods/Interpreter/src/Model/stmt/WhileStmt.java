package Model.stmt;

import Model.Exceptions.ADTException;
import Model.Exceptions.Custom_Exception;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Value.BoolValue;
import Model.Value.IValue;
import Model.adt.IDict;
import Model.adt.IHeap;
import Model.adt.IStack;
import Model.exp.Exp;

import java.time.chrono.IsoChronology;

public class WhileStmt implements IStmt {
    Exp expresion;
    IStmt statement;

    public WhileStmt(Exp e, IStmt s) {
        expresion = e;
        statement = s;
    }

    @Override
    public PrgState execute(PrgState program_state) throws STMTException, EXPException, ADTException, Custom_Exception {
        IDict<String, IValue> symTable = program_state.getSymTable();
        IStack<IStmt> stack = program_state.getStack();
        IHeap<IValue> heap = program_state.getHeap();
        IValue value = expresion.eval(symTable, heap);

        if(value.getType().equals(new BoolValue())) {
            BoolValue bool = (BoolValue) value;

            if(bool.getValue() == true) {
                stack.push(this);
                stack.push(statement);
            }
        }
        else
            throw new STMTException("While statement not a bool !");

        return null;
    }

    @Override
    public String toString() {
        return "while (" + expresion + ") " + statement;
    }
}
