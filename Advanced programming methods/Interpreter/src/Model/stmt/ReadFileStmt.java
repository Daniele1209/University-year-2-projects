package Model.stmt;

import Model.Exceptions.ADTException;
import Model.Exceptions.Custom_Exception;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Type.IntegerType;
import Model.Type.StringType;
import Model.Value.IValue;
import Model.Value.IntegerValue;
import Model.Value.StringValue;
import Model.adt.IDict;
import Model.exp.Exp;

import java.io.BufferedReader;

public class ReadFileStmt implements IStmt{
    String name_var;
    String name_file;
    Exp expression;

    public ReadFileStmt(Exp exp, String v_name) {
        expression = exp;
        name_var = v_name;
    }

    @Override
    public PrgState execute(PrgState program_state) throws STMTException, EXPException, ADTException, Custom_Exception {
        IDict<String, IValue> symTable = program_state.getSymTable();
        IDict<StringValue, BufferedReader> file_table = program_state.getFileTable();

        if (symTable.isDefined(name_var)) {

                IValue exp_val = expression.eval(symTable);

                if(exp_val.getType().equals(new StringType())) {
                    StringValue str_val = (StringValue) exp_val;

                    if (file_table.isDefined(str_val)) {
                        BufferedReader buff_read = file_table.lookup(str_val);

                        try{
                            IValue value;
                            String string = buff_read.readLine();
                            IntegerType int_type = new IntegerType();

                            if (string != null) {
                                value = new IntegerValue(Integer.parseInt(string));
                            }
                            else {
                                value = new IntegerValue(0);
                            }
                            symTable.update(name_var, value);
                        } catch(Exception err) {
                            throw new STMTException(err.getMessage());
                        }
                    }
                    else {
                        throw new STMTException(str_val + "file not found !");
                    }
                }
                else {
                    throw new STMTException("Failed to evaluate a string value");
                }
        }
        else {
            throw new STMTException(name_var + " not defined before using it !");
        }

        return null;
    }

    @Override
    public String toString() {
        return "reading_from (" + expression + ")";
    }
}
