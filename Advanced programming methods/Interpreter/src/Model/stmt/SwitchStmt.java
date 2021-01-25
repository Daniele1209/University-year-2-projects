package Model.stmt;

import Model.Exceptions.ADTException;
import Model.Exceptions.Custom_Exception;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Type.BoolType;
import Model.Type.IType;
import Model.Value.BoolValue;
import Model.Value.IValue;
import Model.adt.IDict;
import Model.adt.IStack;
import Model.exp.*;

public class SwitchStmt implements IStmt {
    private Exp expression, expression1, expression2;
    private IStmt statement1, statement2, statement3;

    public SwitchStmt(Exp e,Exp ex1, IStmt st1, Exp ex2, IStmt st2, IStmt st3) {
        expression = e;
        expression1 = ex1;
        expression2 = ex2;
        statement1 = st1;
        statement2 = st2;
        statement3 = st3;
    }

    @Override
    public PrgState execute(PrgState program_state) throws STMTException, EXPException, ADTException, Custom_Exception {
        IStack<IStmt> stk = program_state.getStack();
        IDict<String, IValue> symTable = program_state.getSymTable();

        IStmt state = new IfStmt(new RelationalExp(expression, expression1, "=="), statement1,
                        new IfStmt(new RelationalExp(expression, expression2, "=="), statement2, statement3));

        stk.push(state);
        program_state.setExeStack(stk);

        return null;
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnvironment) throws STMTException, EXPException {
        IType expType = expression.typecheck(typeEnvironment);
        IType expType1 = expression1.typecheck(typeEnvironment);
        IType expType2 = expression2.typecheck(typeEnvironment);

        if (expType.equals(expType1) && expType.equals(expType2) && expType1.equals(expType2)) {
            statement1.typecheck(typeEnvironment);
            statement2.typecheck(typeEnvironment);
            statement3.typecheck(typeEnvironment);
            return typeEnvironment;
        }
        else {
            throw new STMTException("Condition " + this.toString() + "  not boolean !");
        }
    }

    @Override
    public String toString() {
        return "switch(" + expression + ") " + "(case " + expression1 + " : " + " + " + statement1 + ")" +
                                               "(case " + expression2 + " : " + " + " + statement2 + ")" +
                                               "(default: " + statement3 + ");";
    }
}
