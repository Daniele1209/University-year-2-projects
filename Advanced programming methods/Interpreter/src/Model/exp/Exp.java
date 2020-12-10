package Model.exp;
import Model.Type.IType;
import Model.Value.IValue;
import Model.adt.IDict;
import Model.Exceptions.EXPException;
import Model.adt.IHeap;

public interface Exp {

    public abstract IValue eval(IDict<String,IValue> symTable, IHeap<IValue> heap) throws EXPException;
    public abstract String toString();
    IType typecheck(IDict<String,IType> typeEnv) throws EXPException;
}
