package Model.Value;

import Model.Type.IType;
import Model.Type.StringType;

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
    public IType getType() {
        return new StringType();
    }

    @Override
    public String toString() {
        return string;
    }
}
