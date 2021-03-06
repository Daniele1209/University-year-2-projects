package Model.exp;

import Model.Exceptions.EXPException;
import Model.Type.IType;
import Model.Type.RefType;
import Model.Value.IValue;
import Model.Value.RefValue;
import Model.adt.IDict;
import Model.adt.IHeap;

public class ReadHeapExp implements Exp{
    Exp expression;

    public ReadHeapExp(Exp e) {
        expression = e;
    }

    @Override
    public IValue eval(IDict<String, IValue> symTable, IHeap<IValue> Heap) throws EXPException {
        IValue value = expression.eval(symTable, Heap);

        if(value instanceof RefValue) {
            RefValue ref_value = (RefValue) value;

            if(Heap.contains(ref_value.getAddress()))
                return Heap.get(ref_value.getAddress());
            else
                throw new EXPException(ref_value.getAddress() + " address not found !");
        }
        else
            throw new EXPException("Expression failed to evaluate to a RefType!");
    }

    @Override
    public String toString() {
        return "rH (" + expression + ")";
    }

    @Override
    public IType typecheck(IDict<String,IType> typeEnv) throws EXPException {
        IType type = expression.typecheck(typeEnv);
        if (type instanceof RefType) {
            RefType refType = (RefType)type;
            return refType.getInner();
        }
        else {
            throw new EXPException("the rH argument is not a Ref Type");
        }
    }
}
