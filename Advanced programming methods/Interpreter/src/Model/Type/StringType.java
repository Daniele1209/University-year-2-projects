package Model.Type;

import Model.Value.IValue;

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
}
