package Model.Value;
import Model.Type.IType;
import Model.Type.IntegerType;
import Model.Value.IValue;

public class IntegerValue implements IValue {
    int value;

    public IntegerValue(int val) {
        this.value = val;
    }

    public IntegerValue() {
        this.value = 0;
    }

    public int getValue() {
        return value;
    }

    @Override
    public IType getType() {
        return new IntegerType();
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

}