package Model.Type;

public class IntegerType implements IType{

    public boolean equals(Object obj){
        return obj instanceof BoolType;
    }

    public String toString() {
        return "integer";
    }

}
