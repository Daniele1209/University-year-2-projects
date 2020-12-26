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
import java.util.LinkedList;


public class Programs {
    @FXML
    private ListView<Controller> programsList;

    @FXML
    private Button runProgram;

    @FXML
    public void initialize() throws ADTException {
        programsList.setItems(getControllers());
        programsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private ObservableList<Controller> getControllers() throws ADTException {
        Listt<IStmt> stmts = getExampleList();
        LinkedList<Controller> list = new LinkedList<Controller>();
        for(int i = 0; i <= stmts.size()-1; i++) {
            try{
                IStack<IStmt> stack = new Stackk<>();
                PrgState state = new PrgState(stack, new Dict<String, IValue>(), new Listt<IValue>(), new Dict<StringValue, BufferedReader>(), new Heap<>(), stmts.get(i));
                Repo repository = new Repo(state, "log" + String.valueOf(i+1) + ".txt");
                Controller controller = new Controller(repository);
                list.add(controller);
            }catch(ADTException | Custom_Exception | IOException err) {
                err.printStackTrace();
            }
        }
        return FXCollections.observableArrayList(list);
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

        IStmt ex4 = new CompStmt(new VarDeclStmt("a", new IntegerType()),
                new CompStmt(new VarDeclStmt("b", new IntegerType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new IntegerValue(10))),
                                new CompStmt(new AssignStmt("b", new ArithExp('/', new VarExp("a"), new ValueExp(new IntegerValue(0)))),
                                        new PrintStmt(new VarExp("a"))))));
        exList.add(ex4);

        IStmt ex5 = new CompStmt(
                new VarDeclStmt("varf", new StringType()),
                new CompStmt(new AssignStmt("varf", new ValueExp(new StringValue("test.txt"))),
                        new CompStmt(new OpenFileStmt(new VarExp("varf")),
                                new CompStmt(new VarDeclStmt("varc", new IntegerType()),
                                        new CompStmt(new ReadFileStmt(new VarExp("varf"), "varc"),
                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(new ReadFileStmt(new VarExp("varf"), "varc"),
                                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                                        new CloseFileStmt(new VarExp("varf"))))))))));
        exList.add(ex5);

        IStmt ex6 = new CompStmt(new VarDeclStmt("v", new IntegerType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntegerValue(4))),
                        new CompStmt(new WhileStmt(new RelationalExp(new VarExp("v"), new ValueExp(new IntegerValue(0)), ">"),
                                new CompStmt(new PrintStmt(new VarExp("v")), new AssignStmt("v", new ArithExp('-', new VarExp("v"),
                                        new ValueExp(new IntegerValue(1)))))),
                                new PrintStmt(new VarExp("v")))));
        exList.add(ex6);

        IStmt ex7 = new CompStmt(new VarDeclStmt("v", new RefType(new IntegerType())),
                new CompStmt(new NewHeapStmt("v", new ValueExp(new IntegerValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntegerType()))),
                                new CompStmt(new NewHeapStmt("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                                new PrintStmt(new ArithExp('+', new ReadHeapExp(new ReadHeapExp(new VarExp("a"))),
                                                        new ValueExp(new IntegerValue(5)))))))));
        exList.add(ex7);

        IStmt ex8 = new CompStmt(new VarDeclStmt("v", new IntegerType()),
                new CompStmt(new VarDeclStmt("a", new RefType(new IntegerType())),
                        new CompStmt(new AssignStmt("v", new ValueExp(new IntegerValue(10))),
                                new CompStmt(new NewHeapStmt("a", new ValueExp(new IntegerValue(22))),
                                        new CompStmt(new ForkStmt(new CompStmt(new WriteHeapStmt("a", new ValueExp(new IntegerValue(30))),
                                                new CompStmt(new AssignStmt("v", new ValueExp(new IntegerValue(32))),
                                                        new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new ReadHeapExp(new VarExp("a"))))))),
                                                new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new ReadHeapExp(new VarExp("a")))))))));
        exList.add(ex8);

        return exList;
    }

    @FXML
    public void startProgram(javafx.event.ActionEvent event) {
        Controller controller = programsList.getSelectionModel().getSelectedItem();
        try {
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
        } catch (IOException err) {
            err.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("IOException Error !");
            alert.setHeaderText("IOException Error !");
            alert.setContentText(err.toString());
            alert.showAndWait();
        }
    }
}
