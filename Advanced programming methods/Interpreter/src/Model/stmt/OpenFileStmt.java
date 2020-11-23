package Model.stmt;

import Model.Exceptions.ADTException;
import Model.Exceptions.Custom_Exception;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Type.StringType;
import Model.Value.IValue;
import Model.Value.StringValue;
import Model.adt.IDict;
import Model.exp.Exp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;

public class OpenFileStmt implements IStmt{
    Exp expression;

    public OpenFileStmt(Exp e) {
        expression = e;
    }

    @Override
    public PrgState execute(PrgState program_state) throws STMTException, EXPException, ADTException, Custom_Exception {
        IDict<String, IValue> symTable = program_state.getSymTable();
        IValue value = expression.eval(symTable, program_state.getHeap());
        if (value.getType().equals(new StringType())) {

            IDict<StringValue, BufferedReader> file_table = program_state.getFileTable();
            StringValue val = (StringValue) value;
            if (file_table.isDefined(val) == false) {
                try {
                    Reader read = new FileReader(val.getValue());
                    BufferedReader buff_r = new BufferedReader(read);
                    file_table.update(val, buff_r);
                } catch(Exception err) {
                    throw new STMTException(err.getMessage());
                }
            }
            else {
                throw new STMTException("Can not access the file !");
            }
        }
        else {
            throw new STMTException("Not a string !");
        }
        return null;
    }

    @Override
    public String toString() {
        return "file_open ("+ expression + ")";
    }

}
