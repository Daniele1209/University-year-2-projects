package Model.stmt;

import Model.Exceptions.ADTException;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Type.*;
import Model.Value.*;
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
            symTable.add(name, type.def_val());

        program_state.setSymTable(symTable);
        program_state.setExeStack(stk);

        return program_state;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }
}
