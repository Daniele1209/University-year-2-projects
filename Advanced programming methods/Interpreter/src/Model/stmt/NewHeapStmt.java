package Model.stmt;

import Model.Exceptions.ADTException;
import Model.Exceptions.Custom_Exception;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Type.RefType;
import Model.Value.IValue;
import Model.Value.RefValue;
import Model.adt.IDict;
import Model.adt.IHeap;
import Model.adt.IStack;
import Model.exp.Exp;

public class NewHeapStmt implements IStmt{
    Exp expression;
    String variable;

    public NewHeapStmt(String v, Exp e) {
        variable = v;
        expression = e;
    }

    @Override
    public PrgState execute(PrgState program_state) throws STMTException, EXPException, ADTException, Custom_Exception {
        IStack<IStmt> stack = program_state.getStack();
        IDict<String, IValue> symTable = program_state.getSymTable();
        IHeap<IValue> heap = program_state.getHeap();

        if(symTable.isDefined(variable)) {
            if (symTable.lookup(variable).getType() instanceof RefType) {
                IValue table_value = symTable.lookup(variable);
                IValue value = expression.eval(symTable, heap);

                if (value.getType().equals(((RefType) (table_value.getType())).getInner())) {
                    int address = heap.allocate(value);
                    symTable.update(variable, new RefValue(value.getType(), address));
                } else
                    throw new STMTException("Type not valid !");
            } else
                throw new STMTException("Value not referenced before !");
        }
        else
            throw new STMTException("Value not declared before !");

        program_state.setExeStack(stack);
        program_state.setHeap(heap);
        program_state.setSymTable(symTable);

        return program_state;
    }

    @Override
    public String toString() {
        return "new (" + variable + expression + ")";
    }

}
