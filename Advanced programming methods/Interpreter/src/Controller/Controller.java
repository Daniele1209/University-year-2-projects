package Controller;
import Model.Exceptions.ADTException;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.adt.IStack;
import Repository.IRepo;
import Repository.Repo;
import Model.Exceptions.Custom_Exception;
import Model.stmt.IStmt;

import java.io.IOException;
import java.util.List;
import Model.PrgState;

public class Controller {
    IRepo repository;

    public Controller(IRepo Repo) {
        repository = Repo;
    }

    public void addProgram(PrgState newPrg) {
        repository.addPrg(newPrg);
    }

    public PrgState oneStep(PrgState state) throws Custom_Exception, ADTException, EXPException, STMTException {
        IStack<IStmt> stack = state.getStack();
        if(!stack.isEmpty()) {
            IStmt current = stack.pop();
            return current.execute(state);
        }
        else
            throw new Custom_Exception("Empty stack !");
    }

    public void allStep() throws Custom_Exception, EXPException, STMTException, ADTException, IOException {
        PrgState program_state = repository.getCrtPrg();
        repository.printState(program_state);
        while(!program_state.getStack().isEmpty()) {
            try {
                oneStep(program_state);
                System.out.println(program_state);
            } catch(Custom_Exception | ADTException | EXPException | STMTException exept) {
                throw new Custom_Exception(exept.getMessage());
            }
        }
    }
}
