package Model.stmt;

import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Type.BoolType;
import Model.Type.IType;
import Model.Type.IntegerType;
import Model.Value.IValue;
import Model.adt.IDict;
import Model.adt.IStack;
import Model.exp.Exp;

public class ForStmt implements IStmt {
    private IStmt statement1;
    private IStmt statement2;
    private Exp expression;
    private IStmt statement;

    public ForStmt(IStmt s1, IStmt s2, Exp e, IStmt s) {
        statement1 = s1;
        statement2 = s2;
        expression = e;
        statement = s;
    }

    @Override
    public PrgState execute(PrgState program_state) throws STMTException, EXPException {
        IStack<IStmt> stk = program_state.getStack();
        IDict<String, IValue> symTable = program_state.getSymTable();

        IStmt state = new CompStmt(statement1, new WhileStmt(expression, new CompStmt(statement, statement2)));
        stk.push(state);
        program_state.setExeStack(stk);

        return null;
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnvironment) throws EXPException, STMTException {
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "for(" + statement1 + " ; " + expression + " ; " + statement2 + ") " + statement + ")";
    }

}
