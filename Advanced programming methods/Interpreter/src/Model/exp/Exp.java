package Model.exp;
import Model.Value.IValue;
import Model.adt.IDict;
import Model.Exceptions.EXPException;

public interface Exp {

    public abstract IValue eval(IDict<String,IValue> symTable) throws EXPException;
    public abstract String toString();
}
