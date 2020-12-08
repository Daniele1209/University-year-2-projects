package Model;
import Model.Exceptions.ADTException;
import Model.Exceptions.Custom_Exception;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.Value.IValue;
import Model.Value.StringValue;
import Model.adt.IDict;
import Model.adt.IHeap;
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
    IHeap<IValue> heap;
    static int id;
    static int free_id = 0;

    public PrgState(IStack<IStmt> stack, IDict<String, IValue> SymTable, IList<IValue> Out, IDict<StringValue, BufferedReader> fileTable, IHeap<IValue> heap, IStmt program_state) {
        exeStack = stack;
        symTable = SymTable;
        out = Out;
        file_table = fileTable;
        originalProgram = program_state;
        this.heap = heap;
        exeStack.push(program_state);
        id = newId();
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

    public IHeap<IValue> getHeap() {
        return heap;
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

    public void setHeap(IHeap<IValue> heap) {
        this.heap = heap;
    }

    public boolean isNotCompleted() {
        return !exeStack.isEmpty();
    }

    public static synchronized int newId() {
        ++free_id;
        return free_id;
    }

    public PrgState oneStep() throws Custom_Exception, ADTException, EXPException, STMTException {
        if (exeStack.isEmpty()) {
            throw new Custom_Exception("Program stack is empty");
        }
        IStmt currentStatement = exeStack.pop();
        return currentStatement.execute(this);
    }

    @Override
    public String toString() {
        StringBuilder final_string = new StringBuilder();
        final_string.append("ID: \n");
        final_string.append(id).append("\n");
        final_string.append("Stack: \n");
        final_string.append(exeStack).append("\n");
        final_string.append("Sym table: \n");
        final_string.append(symTable).append("\n");
        final_string.append("Heap: \n");
        final_string.append(heap).append("\n");
        final_string.append("File table: \n");
        final_string.append(file_table).append("\n");
        final_string.append("Output: \n");
        final_string.append(out).append("\n");

        return final_string.toString();
    }
}