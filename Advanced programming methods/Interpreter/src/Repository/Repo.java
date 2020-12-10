package Repository;
import Model.Exceptions.Custom_Exception;
import Model.PrgState;
import Model.stmt.IStmt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Repo implements IRepo {

    List<PrgState> myPrgStates;
    String file_name;
    IStmt originalProgram;

    public Repo(PrgState program_state, String name) throws Custom_Exception, IOException {
        file_name = name;
        myPrgStates = new LinkedList<>();
        originalProgram = program_state.getOriginalProgram();

        File file = new File(file_name);
        file.createNewFile();
        try(FileWriter file_write = new FileWriter(file)) {
            file_write.write(" ");
        } catch (IOException err) {
            throw new Custom_Exception(err.getMessage());
        }
        myPrgStates.add(program_state);
    }

    @Override
    public PrgState getCrtPrg() {
        PrgState program_state = myPrgStates.get(0);
        myPrgStates.remove(0);
        return program_state;
    }

    @Override
    public void addPrg(PrgState newPrg) {
        myPrgStates.add(newPrg);
    }

    @Override
    public List<PrgState> getPrgList() {
        return myPrgStates;
    }

    @Override
    public void setPrgList(List<PrgState> list) {
        myPrgStates = list;
    }

    @Override
    public void printState(PrgState program_state) throws Custom_Exception, IOException {
        File file = new File(file_name);
        file.createNewFile();

        try{
            FileWriter file_write = new FileWriter(file ,true);
            file_write.write(program_state + "\n");
            file_write.close();
        } catch(Exception err) {
            throw new Custom_Exception(err.getMessage());
        }
    }
}
