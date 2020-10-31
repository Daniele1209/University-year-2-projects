package Model.stmt;

import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Type.IType;
import Model.Value.IValue;
import Model.adt.IDict;
import Model.exp.Exp;

public class AssignStmt implements IStmt{
    String id;
    Exp expression;

    public AssignStmt(String str, Exp expr) {
        id =  str;
        expression = expr;
    }

    @Override
    public PrgState execute(PrgState program_state) throws STMTException, EXPException {
        IDict<String, IValue> symTable = program_state.getSymTable();
        IValue val = expression.eval(symTable);

        if(symTable.isDefined(id)) {
            IType t = (symTable.lookup(id)).getType();

            if(val.getType().equals(t))
                symTable.update(id, val);
            else
                throw new STMTException("Types do not match !");
        }
        else
            throw new STMTException(id + " variable not declared before !");
        program_state.setSymTable(symTable);

        return program_state;
    }

    @Override
    public String toString() {
        return (id + " = " + expression.toString());
    }
}