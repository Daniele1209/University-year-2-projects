package Controller;
import Model.Exceptions.ADTException;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Value.IValue;
import Model.Value.RefValue;
import Model.adt.IStack;
import Repository.IRepo;
import Repository.Repo;
import Model.Exceptions.Custom_Exception;
import Model.stmt.IStmt;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Model.PrgState;

public class Controller {
    IRepo repository;

    public Controller(IRepo Repo) {
        repository = Repo;
    }

    public void addProgram(PrgState newPrg) {
        repository.addPrg(newPrg);
    }

    Map<Integer, IValue> unsafeGarbageCollector(List<Integer> addr, Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(elem -> addr.contains(elem.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {
                    RefValue v1 = (RefValue) v;
                    return v1.getAddress();
                })
                .collect(Collectors.toList());
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
                repository.printState(program_state);
                program_state.getHeap().setMap(unsafeGarbageCollector(getAddrFromSymTable(program_state.getSymTable().getMap().values()), program_state.getHeap().getMap()));
            } catch(Custom_Exception | ADTException | EXPException | STMTException exept) {
                throw new Custom_Exception(exept.getMessage());
            }
        }
    }
}
