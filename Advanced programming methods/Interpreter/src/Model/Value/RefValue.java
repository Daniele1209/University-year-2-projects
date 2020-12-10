package Model.Value;

import Model.Type.IType;
import Model.Type.RefType;

public class RefValue implements IValue{
    int address;
    IType location;

    public  RefValue(IType t, int a) {
        address = a;
        location = t;
    }

    public IType getLocation() {
        return location;
    }

    public int getAddress() {
        return address;
    }

    @Override
    public IType getType() {
        return new RefType(location);
    }

    @Override
    public String toString() {
        return "(" + address + ", " + location + ")";
    }

    @Override
    public IValue deepCopy() {
        return new RefValue(location.deepCopy(), address);
    }
}
