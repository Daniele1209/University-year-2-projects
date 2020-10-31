package Model.stmt;

import Model.Exceptions.ADTException;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Type.BoolType;
import Model.Type.IType;
import Model.Type.IntegerType;
import Model.Value.BoolValue;
import Model.Value.IValue;
import Model.Value.IntegerValue;
import Model.adt.IDict;
import Model.adt.IStack;

public class VarDeclStmt implements IStmt {
    String name;
    IType type;

    public VarDeclStmt(String str, IType t) {
        name = str;
        type = t;
    }

    @Override
    public PrgState execute(PrgState program_state) throws STMTException, EXPException, ADTException {
        IStack<IStmt> stk = program_state.getStack();
        IDict<String, IValue> symTable = program_state.getSymTable();

        if(symTable.isDefined(name))
            throw new STMTException("Variable already exists !");
        else
            if(type.equals(new IntegerType()))
                symTable.add(name, new IntegerValue());
            else if(type.equals(new BoolType()))
                symTable.add(name, new BoolValue());
            else
                throw new STMTException("Not a valid type !");

        program_state.setSymTable(symTable);
        program_state.setExeStack(stk);

        return program_state;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }
}
