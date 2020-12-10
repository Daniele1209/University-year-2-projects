package Model.Type;

import Model.Value.IValue;
import Model.Value.IntegerValue;
import Model.Value.RefValue;

import java.util.Objects;

public class RefType implements IType{
    IType inner;

    public RefType(IType t) {
        inner = t;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RefType r_type = (RefType) o;
        return Objects.equals(inner, r_type.inner);
    }

    public IType getInner() {
        return inner;
    }
    @Override
    public IValue def_val() {
        return new RefValue(inner, 0);
    }

    @Override
    public String toString() {
        return "Ref (" + inner + ")";
    }

    @Override
    public IType deepCopy() {
        return new RefType(inner.deepCopy());
    }
}
