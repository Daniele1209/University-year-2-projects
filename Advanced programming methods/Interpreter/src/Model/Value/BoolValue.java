package Model.Value;
import Model.Type.IType;
import Model.Type.BoolType;

public class BoolValue implements IValue {
    boolean value;

    public BoolValue(boolean val) {
        this.value = val;
    }

    public BoolValue() {
        value = false;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoolValue))
            return false;
        BoolValue iv = (BoolValue) o;
        return iv.value == value;
    }

    @Override
    public IType getType() {
        return new BoolType();
    }

    @Override
    public String toString() {
        return value ? "true" : "false";
    }

}
