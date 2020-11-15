package Model;
import Model.Value.IValue;
import Model.Value.StringValue;
import Model.adt.IDict;
import Model.adt.IList;
import Model.adt.IStack;
import Model.stmt.IStmt;

import java.io.BufferedReader;

public class PrgState {

    IStack<IStmt> exeStack;
    IDict<String, IValue> symTable;
    IList<IValue> out;
    IStmt originalProgram; //optional field, but good to have
    IDict<StringValue, BufferedReader> file_table;

    public PrgState(IStack<IStmt> stack, IDict<String, IValue> SymTable, IList<IValue> Out,  IDict<StringValue, BufferedReader> fileTable) {
        exeStack = stack;
        symTable = SymTable;
        out = Out;
        file_table = fileTable;
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

    public IDict<StringValue, BufferedReader>  getFileTable() {
        return file_table;
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

    public void setFileTable(IDict<StringValue, BufferedReader> ft) {
        file_table = ft;
    }

    @Override
    public String toString() {
        StringBuilder final_string = new StringBuilder();
        final_string.append("Stack: \n");
        final_string.append(exeStack).append("\n");
        final_string.append("Sym table: \n");
        final_string.append(symTable).append("\n");
        final_string.append("File table: \n");
        final_string.append(file_table).append("\n");
        final_string.append("Output: \n");
        final_string.append(out).append("\n");

        return final_string.toString();
    }
}