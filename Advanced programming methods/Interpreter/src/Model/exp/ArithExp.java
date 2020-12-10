package Model.exp;
import Model.Type.IType;
import Model.Type.IntegerType;
import Model.Value.IValue;
import Model.Value.IntegerValue;
import Model.adt.IDict;
import Model.Exceptions.EXPException;
import Model.adt.IHeap;

public class ArithExp implements Exp{
    char op;
    Exp e1, e2;

    public ArithExp(char op, Exp expression1, Exp expression2) {
        this.op = op;
        e1 = expression1;
        e2 = expression2;
    }

    @Override
    public IValue eval(IDict<String, IValue> symTable, IHeap<IValue> heap) throws EXPException {
        IValue left = e1.eval(symTable, heap);
        IValue right = e2.eval(symTable, heap);

        if (op == '+')
            return new IntegerValue(((IntegerValue)left).getValue() + ((IntegerValue)right).getValue());
        if (op == '-')
            return new IntegerValue(((IntegerValue)left).getValue() - ((IntegerValue)right).getValue());
        if (op == '*')
            return new IntegerValue(((IntegerValue)left).getValue() * ((IntegerValue)right).getValue());
        if (op == '/')
            if(((IntegerValue)right).getValue() != 0)
                return new IntegerValue(((IntegerValue)left).getValue() / ((IntegerValue)right).getValue());
            else
                throw new EXPException("Division by 0 !");
         else
             throw new EXPException("Not a valid operation !");
    }

    public char getOp() {
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
        return e1.toString() + " " + op + " " + e2.toString();
    }

    @Override
    public IType typecheck(IDict<String,IType> typeEnv) throws EXPException {
        IType type1 = e1.typecheck(typeEnv);
        IType type2 = e2.typecheck(typeEnv);

        if (type1.equals(new IntegerType())) {
            if (type2.equals(new IntegerType())) {
                return new IntegerType();
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
