package Tests;
import Controller.Controller;
import Model.*;
import Model.Exceptions.ADTException;
import Model.Exceptions.Custom_Exception;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.Type.BoolType;
import Model.Type.IntegerType;
import Model.Value.BoolValue;
import Model.Value.IValue;
import Model.Value.IntegerValue;
import Model.Value.StringValue;
import Model.adt.Dict;
import Model.adt.IStack;
import Model.adt.List;
import Model.adt.Stackk;
import Model.exp.ValueExp;
import Model.exp.VarExp;
import Model.stmt.*;
import Repository.IRepo;
import Repository.Repo;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExpressionsTest {
    public void ExecTest() throws IOException, Custom_Exception, EXPException, ADTException, STMTException {
        IStack<IStmt> stack = new Stackk<>();
        IStmt test = new CompStmt(new VarDeclStmt("a", new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntegerType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(new IfStmt(new VarExp("a"), new AssignStmt("v", new ValueExp(new IntegerValue(2))),
                                        new AssignStmt("v", new ValueExp(new IntegerValue(3)))), new PrintStmt(new VarExp("v"))))));
        PrgState test_program = new PrgState(stack, new Dict<String, IValue>(), new List<IValue>(), new Dict<StringValue, BufferedReader>(), test);
        IRepo test_repository = new Repo(test_program, "test_log.txt");
        Controller test_controller = new Controller(test_repository);
        test_repository.addPrg(test_program);
        try {
            test_controller.allStep();
        } catch (Custom_Exception err) {}
        String line = Files.readAllLines(Paths.get("test_log.txt")).get(113);
        int result = Integer.parseInt(String.valueOf(line.charAt(0)));
        assert result == 2;
    }
}
