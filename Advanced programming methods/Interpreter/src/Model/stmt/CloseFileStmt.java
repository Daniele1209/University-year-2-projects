package Model.stmt;

import Model.Exceptions.ADTException;
import Model.Exceptions.Custom_Exception;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Type.IType;
import Model.Type.StringType;
import Model.Value.IValue;
import Model.Value.StringValue;
import Model.adt.IDict;
import Model.adt.IHeap;
import Model.exp.Exp;

import java.io.BufferedReader;


public class CloseFileStmt implements IStmt{
    Exp expression;

    public CloseFileStmt(Exp expr) {
        expression = expr;
    }

    @Override
    public PrgState execute(PrgState program_state) throws STMTException, EXPException, ADTException, Custom_Exception {
        IDict<String, IValue> symTable = program_state.getSymTable();
        IValue value = expression.eval(symTable, program_state.getHeap());

        if (value.getType().equals(new StringType())) {
            IDict<StringValue, BufferedReader> file_table = program_state.getFileTable();
            StringValue string_value = (StringValue) value;
            if (file_table.isDefined(string_value)) {
                BufferedReader buff_read = file_table.lookup(string_value);

                try{
                    buff_read.close();
                } catch(Exception err) {
                    throw new STMTException(err.getMessage());
                }
                file_table.remove(string_value);
            }
            else {
                throw new STMTException("File not found !");
            }
        }
        else {
            throw new STMTException("Evaluation failed !");
        }
        return null;
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnvironment) throws STMTException, EXPException {
        IType expType = expression.typecheck(typeEnvironment);
        if (expType.equals(new StringType())) {
            return typeEnvironment;
        }
        else {
            throw new STMTException("Close file expression is not a string");
        }
    }

    @Override
    public String toString() {
        return "closing_file (" + expression + ")";
    }
}
