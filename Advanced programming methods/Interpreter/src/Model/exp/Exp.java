package Model.exp;
import Model.Value.IValue;
import Model.adt.IDict;
import Model.Exceptions.EXPException;
import Model.adt.IHeap;

public interface Exp {

    public abstract IValue eval(IDict<String,IValue> symTable, IHeap<IValue> heap) throws EXPException;
    public abstract String toString();
}
