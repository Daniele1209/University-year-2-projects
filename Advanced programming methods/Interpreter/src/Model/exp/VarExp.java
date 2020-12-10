package Model.exp;
import Model.Exceptions.EXPException;
import Model.Type.IType;
import Model.Value.IValue;
import Model.adt.IDict;
import Model.adt.IHeap;

public class VarExp implements Exp{
    String id;

    public VarExp(String str) {
        this.id = str;
    }

    @Override
    public IValue eval(IDict<String,IValue> symTable, IHeap<IValue> heap) throws EXPException {
        return symTable.lookup(id);
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public IType typecheck(IDict<String,IType> typeEnv) throws EXPException {
        if(typeEnv.isDefined(id)){
            return typeEnv.lookup(id);
        }
        else{
            throw new EXPException(this.toString() + " is not defined");
        }
    }
}
