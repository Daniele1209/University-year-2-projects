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
import Model.exp.Exp;

public class WriteHeapStmt implements IStmt{
    Exp expression;
    String variable;

    @Override
    public PrgState execute(PrgState program_state) throws STMTException, EXPException, ADTException, Custom_Exception {
        IHeap<IValue> heap = program_state.getHeap();
        IDict<String, IValue> symTable = program_state.getSymTable();

        if(symTable.isDefined(variable)) {
            if(symTable.lookup(variable).getType() instanceof RefType) {
                RefValue ref_value = (RefValue) symTable.lookup(variable);

                if(heap.contains(ref_value.getAddress())) {
                    IValue val = expression.eval(symTable, heap);

                    if(symTable.lookup(variable).getType().equals(new RefType(val.getType()))) {
                        int addr = ref_value.getAddress();
                        heap.update(addr, val);
                    }
                    else
                        throw new STMTException("Variable that points has a different type !");
                }
                else
                    throw new STMTException("-> " + variable + "not in heap !");
            }
            else
                throw new STMTException("Variable not referenced before using !");
        }
        else
            throw new STMTException("Variable not defined before using !");

        return null;
    }

    @Override
    public String toString() {
        return "wH (" + variable + " ; " + expression + ")";
    }
}
