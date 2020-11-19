package Model.Type;

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
    public String toString() {
        return "Ref (" + inner + ")";
    }
}
