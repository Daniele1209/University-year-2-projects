package Model.Type;

import Model.Value.BoolValue;
import Model.Value.IValue;
import Model.Value.StringValue;

public class StringType implements IType{

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass() || obj == null)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "string";
    }

    @Override
    public IValue def_val() {
        return new StringValue("");
    }

    @Override
    public IType deepCopy() {
        return new StringType();
    }
}
