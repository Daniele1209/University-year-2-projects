package Model.exp;
import Model.Exceptions.EXPException;
import Model.Type.BoolType;
import Model.Type.IType;
import Model.Value.BoolValue;
import Model.Value.IValue;
import Model.adt.IDict;
import Model.adt.IHeap;

public class LogicExp implements Exp{
    String op;
    Exp e1, e2;

    public LogicExp(Exp first, Exp second, String oper) {
        this.e1 = first;
        this.e2 = second;
        this.op = oper;
    }

    @Override
    public IValue eval(IDict<String, IValue> symTable, IHeap<IValue> heap) throws EXPException {
        IValue left = e1.eval(symTable, heap);
        IValue right = e2.eval(symTable, heap);

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

    @Override
    public IType typecheck(IDict<String,IType> typeEnv) throws EXPException {
        IType type1 = e1.typecheck(typeEnv);
        IType type2 = e2.typecheck(typeEnv);

        if (type1.equals(new BoolType())) {
            if (type2.equals(new BoolType())) {
                return new BoolType();
            }
            else {
                throw new EXPException("second operand is not an integer");
            }
        }
        else {
            throw new EXPException("first operand is not an integer");
        }
    }
}
