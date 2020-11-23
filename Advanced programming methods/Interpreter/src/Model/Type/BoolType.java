package Model.Type;

import Model.Value.BoolValue;
import Model.Value.IValue;

public class BoolType implements IType{

    public boolean equals(Object obj){
        return obj instanceof BoolType;
    }

    public String toString() {
        return "boolean";
    }
    @Override
    public IValue def_val() {
        return new BoolValue(false);
    }

}
