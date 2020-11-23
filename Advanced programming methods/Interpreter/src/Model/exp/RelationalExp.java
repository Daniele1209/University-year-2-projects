package Model.exp;

import Model.Exceptions.EXPException;
import Model.Type.IntegerType;
import Model.Value.BoolValue;
import Model.Value.IValue;
import Model.Value.IntegerValue;
import Model.adt.IDict;
import Model.adt.IHeap;

public class RelationalExp implements Exp{
    Exp expression1;
    Exp expression2;
    String op;

    public RelationalExp(Exp e1, Exp e2, String operation) {
        expression1 = e1;
        expression2 = e2;
        op = operation;
    }

    @Override
    public IValue eval(IDict<String, IValue> symTable, IHeap<IValue> heap) throws EXPException {
        IValue value1;
        IValue value2;
        value1 = expression1.eval(symTable, heap);
        if (!value1.getType().equals(new IntegerType())) {
            throw new EXPException("Left operand not an integer !");
        }
        value2 = expression2.eval(symTable, heap);
        if (!value2.getType().equals(new IntegerType())) {
            throw new EXPException("Right operand not an integer !");
        }
        IntegerValue integer1 = (IntegerValue) value1;
        IntegerValue integer2 = (IntegerValue) value2;
        int left_side = integer1.getValue();
        int right_side = integer2.getValue();

        if(op.equals("<"))
            return new BoolValue(left_side < right_side);
        else if(op.equals("<="))
            return new BoolValue(left_side <= right_side);
        else if(op.equals(">"))
            return new BoolValue(left_side > right_side);
        else if(op.equals(">="))
            return new BoolValue(left_side >= right_side);
        else if(op.equals("=="))
            return new BoolValue(left_side == right_side);
        else if(op.equals("!="))
            return new BoolValue(left_side != right_side);
        else
            throw new EXPException("Not a valid relational operator");
    }

    @Override
    public String toString() {
        return expression1 + " " + op + " " + expression2;
    }
}
