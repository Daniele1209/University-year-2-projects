package Model.stmt;

import Model.Exceptions.ADTException;
import Model.Exceptions.Custom_Exception;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Type.IType;
import Model.Type.RefType;
import Model.Value.IValue;
import Model.Value.RefValue;
import Model.adt.IDict;
import Model.adt.IHeap;
import Model.exp.Exp;
import Model.exp.ValueExp;

public class WriteHeapStmt implements IStmt{
    Exp expression;
    String variable;

    public WriteHeapStmt(String a, Exp valueExp) {
        variable = a;
        expression = valueExp;
    }

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
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnvironment) throws STMTException, EXPException {
        if (typeEnvironment.isDefined(variable)) {
            IType variableType = typeEnvironment.lookup(variable);
            IType expType = expression.typecheck(typeEnvironment);
            if (!(variableType instanceof RefType)) {
                throw new STMTException(this.toString() + " is not a string !");
            }
            if (!variableType.equals(new RefType(expType))) {
                throw new STMTException("Right side has different type than left side !");
            }
            return typeEnvironment;
        }
        else {
            throw new STMTException(variable + " is not defined !");
        }
    }

    @Override
    public String toString() {
        return "wH (" + variable + " ; " + expression + ")";
    }
}
