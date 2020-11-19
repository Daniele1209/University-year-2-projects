package Model.exp;
import Model.Value.IValue;
import Model.Exceptions.EXPException;
import Model.adt.IDict;
import Model.adt.IHeap;

public class ValueExp implements Exp{
    IValue v;

    public ValueExp(IValue val) {
        this.v = val;
    }

    @Override
    public IValue eval(IDict<String,IValue> symTable, IHeap<IValue> heap) throws EXPException {
        return v;
    }

    @Override
    public String toString() {
        return v.toString();
    }
}
