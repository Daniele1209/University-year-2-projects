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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntegerValue))
            return false;
        IntegerValue iv = (IntegerValue) o;
        return iv.value == value;
    }

    @Override
    public IType getType() {
        return new IntegerType();
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public IValue deepCopy() {
        return new IntegerValue(value);
    }
}