package Model.stmt;

import Model.Exceptions.ADTException;
import Model.Exceptions.Custom_Exception;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
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
    public PrgState execute(PrgState program_state) throws STMTException {
        IDict<String, IValue> symTable = program_state.getSymTable();
        IStack<IStmt> stack = program_state.getStack();
        IList<IValue> out = program_state.getOut();
        IDict<StringValue, BufferedReader> file_tbl = program_state.getFileTable();
        IHeap<IValue> heap = program_state.getHeap();
        Stackk<IStmt>  new_stack = new Stackk<IStmt>();
        Dict<String, IValue> new_table = new Dict<String, IValue>();

        for(Map.Entry<String, IValue> obj : symTable.getMap().entrySet()) {
            new_table.update(new String(obj.getKey()), obj.getValue());
        }

        return new PrgState(new_stack, new_table, out, file_tbl, heap, stmt);
    }

    @Override
    public String toString() {
        return "fork (" + stmt + ")";
    }
}
