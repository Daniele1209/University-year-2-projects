package Model.Type;

import Model.Value.BoolValue;
import Model.Value.IValue;
import Model.Value.IntegerValue;

public class IntegerType implements IType{

    public boolean equals(Object obj){
        return obj instanceof IntegerType;
    }

    public String toString() {
        return "integer";
    }
    @Override
    public IValue def_val() {
        return new IntegerValue(0);
    }
    @Override
    public IType deepCopy() {
        return new IntegerType();
    }
}
