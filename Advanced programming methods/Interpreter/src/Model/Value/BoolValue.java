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
    public IType getType() {
        return new BoolType();
    }

    @Override
    public String toString() {
        return value ? "true" : "false";
    }

}
