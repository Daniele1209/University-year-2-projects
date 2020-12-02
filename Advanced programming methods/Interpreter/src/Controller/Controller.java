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
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import Model.PrgState;

public class Controller {
    IRepo repository;
    ExecutorService executor;

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
/*  *Old version of oneStep()

    public PrgState oneStep(PrgState state) throws Custom_Exception, ADTException, EXPException, STMTException {
        IStack<IStmt> stack = state.getStack();
        if(!stack.isEmpty()) {
            IStmt current = stack.pop();
            return current.execute(state);
        }
        else
            throw new Custom_Exception("Empty stack !");
    }

    *Old version of allStep()*

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
*/
    void oneStepForAllPrg(List<PrgState> prgList) throws InterruptedException, Custom_Exception {

        prgList.forEach(prg -> {
            try {
                repository.printState(prg);
            } catch (Custom_Exception | IOException e) {
                e.printStackTrace();
            }
        });
        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>) (p::oneStep))
                .collect(Collectors.toList());
        try {
            List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (InterruptedException | ExecutionException e) {
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            prgList.addAll(newPrgList);
        } catch (InterruptedException err) {
            throw new Custom_Exception(err.getMessage());
        }
        //after the execution, print the PrgState List into the log file
        prgList.forEach(prg -> {
            try {
                repository.printState(prg);
            } catch (Custom_Exception | IOException e) {
                e.printStackTrace();
            }
        });
        //Save the current programs in the repository
        repository.setPrgList(prgList);
    }

    void allStep() throws Custom_Exception, InterruptedException {
        executor = Executors.newFixedThreadPool(2);
        //remove the completed programs
        List<PrgState> prgList = removeCompletedPrograms(repository.getPrgList());
        while(prgList.size() > 0){
            oneStepForAllPrg(prgList);
            //remove the completed programs
            prgList=removeCompletedPrograms(repository.getPrgList());
        }
        executor.shutdownNow();

        // update the repository state
        repository.setPrgList(prgList);
    }

    public List<PrgState> removeCompletedPrograms(List<PrgState> prgList) {
        return prgList.stream().filter(PrgState::isNotCompleted).collect(Collectors.toList());
    }
}
