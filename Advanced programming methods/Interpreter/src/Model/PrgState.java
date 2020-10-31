package Model;
import Model.Value.IValue;
import Model.adt.IDict;
import Model.adt.IList;
import Model.adt.IStack;
import Model.stmt.IStmt;

public class PrgState {

    IStack<IStmt> exeStack;
    IDict<String, IValue> symTable;
    IList<IValue> out;
    IStmt originalProgram; //optional field, but good to have

    public PrgState(IStack<IStmt> stack, IDict<String, IValue> SymTable, IList<IValue> Out) {
        exeStack = stack;
        symTable = SymTable;
        out = Out;
    }

    //getters
    public IStack<IStmt> getStack() {
        return exeStack;
    }

    public IDict<String, IValue> getSymTable() {
        return symTable;
    }

    public IList<IValue> getOut() {
        return out;
    }

    public IStmt getOriginalProgram() {
        return originalProgram;
    }
    //setters
    public void setExeStack(IStack<IStmt> stack) {
        exeStack = stack;
    }

    public void setSymTable(IDict<String, IValue> SymTable) {
        symTable = SymTable;
    }

    public void setOut(IList<IValue> Out) {
        out = Out;
    }

    @Override
    public String toString() {
        StringBuilder final_string = new StringBuilder();
        final_string.append("Stack: \n");
        final_string.append(exeStack).append("\n");
        final_string.append("Sym table: \n");
        final_string.append(symTable).append("\n");
        final_string.append("Output: \n");
        final_string.append(out).append("\n");

        return final_string.toString();
    }
}