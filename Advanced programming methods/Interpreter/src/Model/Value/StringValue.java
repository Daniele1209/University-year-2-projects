package Model.Value;

import Model.Type.IType;
import Model.Type.StringType;

import java.util.Objects;

public class StringValue implements IValue{
    String string;

    public StringValue() {
        string = "";
    }

    public StringValue(String s) {
        string = s;
    }

    public String getValue() {
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        StringValue iv = (StringValue) o;
        return Objects.equals(string, iv.string);
    }

    @Override
    public IType getType() {
        return new StringType();
    }

    @Override
    public String toString() {
        return string;
    }
}
