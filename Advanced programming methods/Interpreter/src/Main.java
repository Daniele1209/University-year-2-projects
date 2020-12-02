import Model.Exceptions.ADTException;
import Model.Exceptions.Custom_Exception;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.Type.BoolType;
import Model.Type.IntegerType;
import Model.Type.RefType;
import Model.Type.StringType;
import Model.Value.BoolValue;
import Model.Value.IValue;
import Model.Value.IntegerValue;
import Model.Value.StringValue;
import Model.adt.*;
import Model.exp.*;
import Model.stmt.*;
import Repository.IRepo;
import Repository.Repo;
import Controller.Controller;
import Model.PrgState;
import Tests.ExpressionsTest;
import View.ExitCommand;
import View.RunExample;
import View.TextMenu;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.temporal.ValueRange;
import java.util.Scanner;
import java.util.concurrent.CompletionStage;

public class Main {

    public static void main(String[] args) throws STMTException, ADTException, Custom_Exception, EXPException, IOException {
        IStack<IStmt> stack1 = new Stackk<>();
        IStack<IStmt> stack2 = new Stackk<>();
        IStack<IStmt> stack3 = new Stackk<>();
        IStack<IStmt> stack4 = new Stackk<>();
        IStack<IStmt> stack5 = new Stackk<>();
        IStack<IStmt> stack6 = new Stackk<>();
        IStack<IStmt> stack7 = new Stackk<>();
        IStack<IStmt> stack8 = new Stackk<>();
        //test execution
        ExpressionsTest test = new ExpressionsTest();
        test.ExecTest();
        System.out.println("Tests done !\n");
        //Example1:
        //int v; v=2;
        IStmt ex1 = new CompStmt(new VarDeclStmt("v",new IntegerType()),
                new CompStmt(new AssignStmt("v",new ValueExp(new IntegerValue(2))),
                        new PrintStmt(new VarExp("v"))));
        PrgState program1 = new PrgState(stack1, new Dict<String, IValue>(), new List<IValue>(), new Dict<StringValue, BufferedReader>(), new Heap<>(),  ex1);
        IRepo repository1 = new Repo(program1,"log1.txt");
        Controller controller1 = new Controller(repository1);

        //Example2:
        //int a;int b;
        //a=2+3*5;b=a+1;
        IStmt ex2 = new CompStmt( new VarDeclStmt("a",new IntegerType()),
                new CompStmt(new VarDeclStmt("b",new IntegerType()),
                        new CompStmt(new AssignStmt("a", new ArithExp('+', new ValueExp(new IntegerValue(2)),
                                new ArithExp('*',new ValueExp(new IntegerValue(3)), new ValueExp(new IntegerValue(5))))),
                                    new CompStmt(new AssignStmt("b",new ArithExp('+',new VarExp("a"), new ValueExp(new IntegerValue(1)))),
                                        new PrintStmt(new VarExp("b"))))));
        PrgState program2 = new PrgState(stack2, new Dict<String, IValue>(), new List<IValue>(), new Dict<StringValue, BufferedReader>(),new Heap<>(), ex2);
        IRepo repository2 = new Repo(program2,"log2.txt");
        Controller controller2 = new Controller(repository2);

        //Example3:
        //bool a; int v; a=true;
        //(If a Then v=2 Else v=3);
        IStmt ex3 = new CompStmt(new VarDeclStmt("a",new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntegerType()),
                    new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                        new CompStmt(new IfStmt(new VarExp("a"),new AssignStmt("v",new ValueExp(new IntegerValue(2))),
                                new AssignStmt("v", new ValueExp(new IntegerValue(3)))), new PrintStmt(new VarExp("v"))))));
        PrgState program3 = new PrgState(stack3, new Dict<String, IValue>(), new List<IValue>(), new Dict<StringValue, BufferedReader>(),new Heap<>(), ex3);
        IRepo repository3 = new Repo(program3,"log3.txt");
        Controller controller3 = new Controller(repository3);

        //Example4:
        //int a;int b;
        //a=10;b=a/0;
        IStmt ex4 = new CompStmt(new VarDeclStmt("a", new IntegerType()),
                                 new CompStmt(new VarDeclStmt("b", new IntegerType()),
                                    new CompStmt(new AssignStmt("a", new ValueExp(new IntegerValue(10))),
                                         new CompStmt(new AssignStmt("b", new ArithExp('/', new VarExp("a"), new ValueExp(new IntegerValue(0)))),
                                                 new PrintStmt(new VarExp("a"))))));
        PrgState program4 = new PrgState(stack4, new Dict<String, IValue>(), new List<IValue>(), new Dict<StringValue, BufferedReader>(),new Heap<>(), ex4);
        IRepo repository4 = new Repo(program4,"log4.txt");
        Controller controller4 = new Controller(repository4);

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
        PrgState program5 = new PrgState(stack5, new Dict<String, IValue>(), new List<IValue>(), new Dict<StringValue, BufferedReader>(),new Heap<>(), ex5);
        IRepo repository5 = new Repo(program5,"log5.txt");
        Controller controller5 = new Controller(repository5);

        IStmt ex6 = new CompStmt(new VarDeclStmt("v", new IntegerType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntegerValue(4))),
                        new CompStmt(new WhileStmt(new RelationalExp(new VarExp("v"), new ValueExp(new IntegerValue(0)), ">"),
                                new CompStmt(new PrintStmt(new VarExp("v")), new AssignStmt("v", new ArithExp('-', new VarExp("v"),
                                        new ValueExp(new IntegerValue(1)))))),
                                            new PrintStmt(new VarExp("v")))));
        PrgState program6 = new PrgState(stack6, new Dict<String, IValue>(), new List<IValue>(), new Dict<StringValue, BufferedReader>(),new Heap<>(), ex6);
        IRepo repository6 = new Repo(program6,"log6.txt");
        Controller controller6 = new Controller(repository6);

        IStmt ex7 = new CompStmt(new VarDeclStmt("v", new RefType(new IntegerType())),
                new CompStmt(new NewHeapStmt("v", new ValueExp(new IntegerValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntegerType()))),
                                new CompStmt(new NewHeapStmt("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                                new PrintStmt(new ArithExp('+', new ReadHeapExp(new ReadHeapExp(new VarExp("a"))),
                                                        new ValueExp(new IntegerValue(5)))))))));
        PrgState program7 = new PrgState(stack7, new Dict<String, IValue>(), new List<IValue>(), new Dict<StringValue, BufferedReader>(),new Heap<>(), ex7);
        IRepo repository7 = new Repo(program7,"log7.txt");
        Controller controller7 = new Controller(repository7);

        IStmt ex8 = new CompStmt(new VarDeclStmt("v", new IntegerType()),
                new CompStmt(new VarDeclStmt("a", new RefType(new IntegerType())),
                        new CompStmt(new AssignStmt("v", new ValueExp(new IntegerValue(10))),
                                new CompStmt(new NewHeapStmt("a", new ValueExp(new IntegerValue(22))),
                                        new CompStmt(new ForkStmt(new CompStmt(new WriteHeapStmt("a", new ValueExp(new IntegerValue(30))),
                                                new CompStmt(new AssignStmt("v", new ValueExp(new IntegerValue(32))),
                                                        new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new ReadHeapExp(new VarExp("a"))))))),
                                                            new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new ReadHeapExp(new VarExp("a")))))))));
        PrgState program8 = new PrgState(stack8, new Dict<String, IValue>(), new List<IValue>(), new Dict<StringValue, BufferedReader>(),new Heap<>(), ex8);
        IRepo repository8 = new Repo(program8,"log8.txt");
        Controller controller8 = new Controller(repository8);

        TextMenu menu = new TextMenu();
        repository1.addPrg(program1);
        repository2.addPrg(program2);
        repository3.addPrg(program3);
        repository4.addPrg(program4);
        repository5.addPrg(program5);
        repository6.addPrg(program6);
        repository7.addPrg(program7);
        repository8.addPrg(program8);

        menu.addCommand(new RunExample("1", ex1.toString(), controller1));
        menu.addCommand(new RunExample("2", ex2.toString(), controller2));
        menu.addCommand(new RunExample("3", ex3.toString(), controller3));
        menu.addCommand(new RunExample("4", ex4.toString(), controller4));
        menu.addCommand(new RunExample("5", ex5.toString(), controller5));
        menu.addCommand(new RunExample("6", ex6.toString(), controller6));
        menu.addCommand(new RunExample("7", ex6.toString(), controller7));
        menu.addCommand(new RunExample("8", ex6.toString(), controller8));
        menu.addCommand(new ExitCommand("9", "quit"));
        menu.show();

    }
}
