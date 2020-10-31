package Model.exp;
import Model.Exceptions.EXPException;
import Model.Value.IValue;
import Model.adt.IDict;

public class VarExp implements Exp{
    String id;

    public VarExp(String str) {
        this.id = str;
    }

    @Override
    public IValue eval(IDict<String,IValue> symTable) throws EXPException {
        return symTable.lookup(id);
    }

    @Override
    public String toString() {
        return id;
    }

}
