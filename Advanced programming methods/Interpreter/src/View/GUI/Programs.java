package View.GUI;

import Controller.Controller;
import Model.Exceptions.ADTException;
import Model.Exceptions.Custom_Exception;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.PrgState;
import Model.Type.*;
import Model.Value.BoolValue;
import Model.Value.IValue;
import Model.Value.IntegerValue;
import Model.Value.StringValue;
import Model.adt.*;
import Model.exp.*;
import Model.stmt.*;
import Repository.IRepo;
import Repository.Repo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class Programs {
    @FXML
    private ListView<String> programsList;

    @FXML
    private Button runProgram;

    @FXML
    public void initialize() throws ADTException {
        List<String> examples = new ArrayList<>();
        examples.add("integer v; v = 2; print(v)");
        examples.add("integer a; integer b; a=2+3*5; b=a+1; print(b)");
        examples.add("bool a; int v; a=true; if(a) then { v = 2 } else { v = 3 }; print(v)");
        examples.add("string varf; varf= 'test.in'; file_open(varf); integer varc; reading_from(varf, varc); print(varc); reading_from(varf,varc); print(varc); closing_file(varf)");
        examples.add("integer v; v=4; (while (v > 0) print(v); v=v-1); print(v)");
        examples.add("Ref integer v; new(v,20); Ref Ref integer a; new(a,v); print(rH (v)); print(rH (rH (a)) + 5)");
        examples.add("integer v; Ref integer a; v=10; new(a,22); fork(wH(a,30)); v=32; print(v); print(rH(a))); print(v); print(rH(a))");
        examples.add("Ref integer a; new(a,20); (for(v=0;v<3;v=v+1) fork(print(v); v=v*rh(a))); print(rh(a))");
        examples.add("int a; int b; int c; a=1;b=2;c=5; (switch(a*10) (case (b*c) : print(a);print(b))(case (10) : print(100);print(200))(default : print(300))); print(300)");
        examples.add("v=0; (repeat (fork(print(v);v=v-1);v=v+1) until v==3); x=1;y=2;z=3;w=4; print(v*10)");
        examples.add("v=0; (while(v<3) (fork(print(v);v=v+1);v=v+1); sleep(5); print(v*10)");
        examples.add("bool b; int c; b=true; c=b?100:200; print(c); c= (false)?100:200; print(c);");
        programsList.setItems(FXCollections.observableList(examples));
        programsList.getSelectionModel().select(0);
    }

    public Listt<IStmt> getExampleList() {
        Listt<IStmt> exList = new Listt<IStmt>();

        IStmt ex1 = new CompStmt(new VarDeclStmt("v",new IntegerType()),
                new CompStmt(new AssignStmt("v",new ValueExp(new IntegerValue(2))),
                        new PrintStmt(new VarExp("v"))));
        exList.add(ex1);

        IStmt ex2 = new CompStmt( new VarDeclStmt("a",new IntegerType()),
                new CompStmt(new VarDeclStmt("b",new IntegerType()),
                        new CompStmt(new AssignStmt("a", new ArithExp('+', new ValueExp(new IntegerValue(2)),
                                new ArithExp('*',new ValueExp(new IntegerValue(3)), new ValueExp(new IntegerValue(5))))),
                                new CompStmt(new AssignStmt("b",new ArithExp('+',new VarExp("a"), new ValueExp(new IntegerValue(1)))),
                                        new PrintStmt(new VarExp("b"))))));
        exList.add(ex2);

        IStmt ex3 = new CompStmt(new VarDeclStmt("a",new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntegerType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(new IfStmt(new VarExp("a"),new AssignStmt("v",new ValueExp(new IntegerValue(2))),
                                        new AssignStmt("v", new ValueExp(new IntegerValue(3)))), new PrintStmt(new VarExp("v"))))));
        exList.add(ex3);

        IStmt ex4 = new CompStmt(
                new VarDeclStmt("varf", new StringType()),
                new CompStmt(new AssignStmt("varf", new ValueExp(new StringValue("test.txt"))),
                        new CompStmt(new OpenFileStmt(new VarExp("varf")),
                                new CompStmt(new VarDeclStmt("varc", new IntegerType()),
                                        new CompStmt(new ReadFileStmt(new VarExp("varf"), "varc"),
                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(new ReadFileStmt(new VarExp("varf"), "varc"),
                                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                                        new CloseFileStmt(new VarExp("varf"))))))))));
        exList.add(ex4);

        IStmt ex5 = new CompStmt(new VarDeclStmt("v", new IntegerType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntegerValue(4))),
                        new CompStmt(new WhileStmt(new RelationalExp(new VarExp("v"), new ValueExp(new IntegerValue(0)), ">"),
                                new CompStmt(new PrintStmt(new VarExp("v")), new AssignStmt("v", new ArithExp('-', new VarExp("v"),
                                        new ValueExp(new IntegerValue(1)))))),
                                new PrintStmt(new VarExp("v")))));
        exList.add(ex5);

        IStmt ex6 = new CompStmt(new VarDeclStmt("v", new RefType(new IntegerType())),
                new CompStmt(new NewHeapStmt("v", new ValueExp(new IntegerValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntegerType()))),
                                new CompStmt(new NewHeapStmt("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                                new PrintStmt(new ArithExp('+', new ReadHeapExp(new ReadHeapExp(new VarExp("a"))),
                                                        new ValueExp(new IntegerValue(5)))))))));
        exList.add(ex6);

        IStmt ex7 = new CompStmt(new VarDeclStmt("v", new IntegerType()),
                new CompStmt(new VarDeclStmt("a", new RefType(new IntegerType())),
                        new CompStmt(new AssignStmt("v", new ValueExp(new IntegerValue(10))),
                                new CompStmt(new NewHeapStmt("a", new ValueExp(new IntegerValue(22))),
                                        new CompStmt(new ForkStmt(new CompStmt(new WriteHeapStmt("a", new ValueExp(new IntegerValue(30))),
                                                new CompStmt(new AssignStmt("v", new ValueExp(new IntegerValue(32))),
                                                        new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new ReadHeapExp(new VarExp("a"))))))),
                                                new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new ReadHeapExp(new VarExp("a")))))))));
        exList.add(ex7);

        IStmt ex8 = new CompStmt(new VarDeclStmt("a", new RefType(new IntegerType())),
                new CompStmt(new NewHeapStmt("a", new ValueExp(new IntegerValue(20))),
                        new CompStmt(new VarDeclStmt("v",  new IntegerType()),
                            new CompStmt(new ForStmt(new AssignStmt("v", new ValueExp(new IntegerValue(0))),
                                    new AssignStmt("v",  new ArithExp('+', new VarExp("v"), new ValueExp(new IntegerValue(1)))),
                                        new RelationalExp(new VarExp("v"), new ValueExp(new IntegerValue(3)), "<"),
                                            new ForkStmt(new CompStmt(new PrintStmt(new VarExp("v")), new AssignStmt("v",
                                                new ArithExp('*', new VarExp("v"), new ReadHeapExp(new VarExp("a"))))))),
                                                    new PrintStmt(new ReadHeapExp(new VarExp("a")))))));
        exList.add(ex8);

        IStmt ex9 = new CompStmt(new VarDeclStmt("a", new IntegerType()),
                        new CompStmt(new VarDeclStmt("b", new IntegerType()),
                                new CompStmt(new VarDeclStmt("c", new IntegerType()),
                                    new CompStmt(new AssignStmt("a", new ValueExp(new IntegerValue(1))),
                                        new CompStmt(new AssignStmt("b", new ValueExp(new IntegerValue(2))),
                                                new CompStmt(new AssignStmt("c", new ValueExp(new IntegerValue(5))),
                                                    new CompStmt(new SwitchStmt(new ArithExp('*', new VarExp("a"), new ValueExp(new IntegerValue(10))),
                                                            new ArithExp('*', new VarExp("b"), new VarExp("c")),
                                                                new CompStmt(new PrintStmt(new VarExp("a")), new PrintStmt(new VarExp("b"))),
                                                                    new ValueExp(new IntegerValue(10)),
                                                                        new CompStmt(new PrintStmt(new ValueExp(new IntegerValue(100))), new PrintStmt(new ValueExp(new IntegerValue(200)))),
                                                                            new PrintStmt(new ValueExp(new IntegerValue(300)))), new PrintStmt(new ValueExp(new IntegerValue(300))))))))));
        exList.add(ex9);

        IStmt ex10 = new CompStmt(new VarDeclStmt("v", new IntegerType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntegerValue(0))),
                        new CompStmt(new DoWhileStatement(new RelationalExp(new VarExp("v"), new ValueExp(new IntegerValue(3)), "=="),
                                new CompStmt(new ForkStmt(new CompStmt(new PrintStmt(new VarExp("v")),
                                        new AssignStmt("v", new ArithExp('-', new VarExp("v"), new ValueExp(new IntegerValue(1)))))),
                                                    new AssignStmt("v", new ArithExp('+', new VarExp("v"), new ValueExp(new IntegerValue(1)))))),
                                                            new PrintStmt(new ArithExp('*', new VarExp("v"), new ValueExp(new IntegerValue(10)))))));
        exList.add(ex10);

        IStmt ex11 = new CompStmt(new VarDeclStmt("v", new IntegerType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntegerValue(0))),
                        new CompStmt(new WhileStmt(new RelationalExp(new VarExp("v"), new ValueExp(new IntegerValue(3)), "<"),
                                new CompStmt(new ForkStmt(new CompStmt(new PrintStmt(new VarExp("v")),
                                        new AssignStmt("v", new ArithExp('+', new VarExp("v"), new ValueExp(new IntegerValue(1)))))),
                                            new AssignStmt("v", new ArithExp('+', new VarExp("v"), new ValueExp(new IntegerValue(1)))))),
                                                new CompStmt(new SleepStatement(new IntegerValue(5)),new PrintStmt(new ArithExp('*',
                                                        new VarExp("v"), new ValueExp(new IntegerValue(10))))))));
        exList.add(ex11);

        IStmt ex12 = new CompStmt( new VarDeclStmt("b",new BoolType()),
                         new CompStmt( new VarDeclStmt("c",new IntegerType()),
                                 new CompStmt(new AssignStmt("b", new ValueExp(new BoolValue(true))),
                                         new CompStmt( new ConditionalAssigStmt("c", new VarExp("b"), new ValueExp(new IntegerValue(100)), new ValueExp(new IntegerValue(200))),
                                                 new CompStmt( new PrintStmt(new VarExp("c")),
                                                         new CompStmt( new ConditionalAssigStmt("c", new ValueExp(new BoolValue(false)), new ValueExp(new IntegerValue(100)), new ValueExp(new IntegerValue(200))),
                                                                new PrintStmt(new VarExp("c"))))))));
        exList.add(ex12);

        return exList;
    }

    @FXML
    public void startProgram(javafx.event.ActionEvent event) throws ADTException {
        Listt<IStmt> stmts = getExampleList();
        LinkedList<Controller> list = new LinkedList<Controller>();
        int index = programsList.getSelectionModel().getSelectedIndex();

        try{
                IStack<IStmt> stack = new Stackk<>();
                IStmt run_program = stmts.get(index);
                run_program.typecheck(new Dict<>());
                PrgState state = new PrgState(stack, new Dict<String, IValue>(), new Listt<IValue>(), new Dict<StringValue, BufferedReader>(), new Heap<>(), stmts.get(index));
                Repo repository = new Repo(state, "log" + String.valueOf(index+1) + ".txt");
                Controller controller = new Controller(repository);
                list.add(controller);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("Execution.fxml"));
                Execution execController = new Execution(controller);
                loader.setController(execController);
                StackPane executorRoot = loader.load();
                Scene executorScene = new Scene(executorRoot, 1350, 800);
                Stage executorStage = new Stage();
                executorStage.setScene(executorScene);
                executorStage.setTitle("Program Execution");
                executorStage.show();
        }catch(ADTException | Custom_Exception | IOException | EXPException | STMTException err) {
            //err.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("IOException Error !");
            alert.setHeaderText("IOException Error !");
            alert.setContentText(err.toString());
            alert.showAndWait();
            }
    }
}
