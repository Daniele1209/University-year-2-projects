package Model.exp;
import Model.Exceptions.EXPException;
import Model.Value.BoolValue;
import Model.Value.IValue;
import Model.adt.IDict;

public class LogicExp implements Exp{
    String op;
    Exp e1, e2;

    public LogicExp(Exp first, Exp second, String oper) {
        this.e1 = first;
        this.e2 = second;
        this.op = oper;
    }

    @Override
    public IValue eval(IDict<String, IValue> symTable) throws EXPException {
        IValue left = e1.eval(symTable);
        IValue right = e2.eval(symTable);

        if (op.equals("||"))
            return new BoolValue(((BoolValue)left).getValue() || ((BoolValue)right).getValue());
        if (op.equals("&&"))
            return new BoolValue(((BoolValue)left).getValue() && ((BoolValue)right).getValue());
        else
            return new BoolValue(false);
    }

    public String getOp() {
        return this.op;
    }

    public Exp getFirst() {
        return this.e1;
    }

    public Exp getSecond() {
        return this.e2;
    }

    @Override
    public String toString() {
        return null;
    }
}
