package Model.Type;

public class BoolType implements IType{

    public boolean equals(Object obj){
        return obj instanceof BoolType;
    }

    public String toString() {
        return "boolean";
    }

}
