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
import Model.exp.Exp;

public class ConditionalAssigStmt implements IStmt {
    String var;
    Exp exp1;
    Exp exp2;
    Exp exp3;

    public ConditionalAssigStmt(String v, Exp e1, Exp e2, Exp e3) {
        var = v;
        exp1 = e1;
        exp2 = e2;
        exp3 = e3;
    }

    @Override
    public PrgState execute(PrgState program_state) throws STMTException, EXPException, ADTException, Custom_Exception {
        IDict<String, IValue> symTable = program_state.getSymTable();
        IStack<IStmt> stk = program_state.getStack();

        IStmt state = new IfStmt(exp1, new AssignStmt(var, exp2),  new AssignStmt(var, exp3));

        stk.push(state);
        program_state.setExeStack(stk);

        return null;
    }

    @Override
    public IDict<String, IType> typecheck(IDict<String, IType> typeEnvironment) throws STMTException, EXPException {
        IType expType = exp1.typecheck(typeEnvironment);
        IType exp2Type = exp2.typecheck(typeEnvironment);
        IType exp3Type = exp3.typecheck(typeEnvironment);
        if (expType.equals(new BoolType()) && exp2Type.equals(exp3Type)) {
            return typeEnvironment;
        }
        else {
            throw new STMTException("Conditions not met !");
        }
    }

    @Override
    public String toString() {
        return var + " = (" + exp1.toString() + ")?" + exp2.toString() + ":" + exp3.toString() + ";";
    }
}
