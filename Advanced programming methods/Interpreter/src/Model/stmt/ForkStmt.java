package Model.stmt;

import Model.Exceptions.ADTException;
import Model.Exceptions.Custom_Exception;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Type.IType;
import Model.Value.IValue;
import Model.Value.StringValue;
import Model.adt.*;

import java.io.BufferedReader;
import java.util.Map;

public class ForkStmt implements IStmt {
    IStmt stmt;

    public ForkStmt(IStmt s) {
        stmt = s;
    }

    @Override
    public PrgState execute(PrgState program_state) throws ADTException {
        IDict<String, IValue> new_table = new Dict<>();
        for (Map.Entry<String, IValue> entry: program_state.getSymTable().getMap().entrySet()) {
            new_table.add(entry.getKey(), entry.getValue().deepCopy());
        }
        IStack <IStmt> stack = new Stackk<>();
        stack.push(stmt);
        PrgState new_program = new PrgState(stack, new_table, program_state.getOut(), program_state.getFileTable(), program_state.getHeap());
        return new_program;
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnvironment) throws STMTException, EXPException {
        stmt.typecheck(typeEnvironment);
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "fork (" + stmt + ")";
    }
}
