package View;
import Model.Exceptions.ADTException;
import Model.Exceptions.Custom_Exception;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;
import Model.Type.BoolType;
import Model.Type.IntegerType;
import Model.Value.BoolValue;
import Model.Value.IValue;
import Model.Value.IntegerValue;
import Model.adt.*;
import Model.exp.ArithExp;
import Model.exp.ValueExp;
import Model.exp.VarExp;
import Model.stmt.*;
import Repository.Repo;
import Controller.Controller;
import Model.PrgState;

import java.time.temporal.ValueRange;
import java.util.Scanner;
import java.util.concurrent.CompletionStage;

public class Main {

    static Repo repository = new Repo();
    static Controller controller = new Controller(repository);

    public static void main(String[] args) throws STMTException, ADTException, Custom_Exception, EXPException {
        IStack<IStmt> stack = new Stackk<>();
        //Example1:
        //int v; v=2;
        IStmt ex1 = new CompStmt(new VarDeclStmt("v",new IntegerType()),
                new CompStmt(new AssignStmt("v",new ValueExp(new IntegerValue(2))),
                        new PrintStmt(new VarExp("v"))));

        //Example2:
        //int a;int b;
        //a=2+3*5;b=a+1;
        IStmt ex2 = new CompStmt( new VarDeclStmt("a",new IntegerType()),
                new CompStmt(new VarDeclStmt("b",new IntegerType()),
                        new CompStmt(new AssignStmt("a", new ArithExp('+', new ValueExp(new IntegerValue(2)),
                                new ArithExp('*',new ValueExp(new IntegerValue(3)), new ValueExp(new IntegerValue(5))))),
                                    new CompStmt(new AssignStmt("b",new ArithExp('+',new VarExp("a"), new ValueExp(new IntegerValue(1)))),
                                        new PrintStmt(new VarExp("b"))))));

        //Example3:
        //bool a; int v; a=true;
        //(If a Then v=2 Else v=3);
        IStmt ex3 = new CompStmt(new VarDeclStmt("a",new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntegerType()),
                    new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                        new CompStmt(new IfStmt(new VarExp("a"),new AssignStmt("v",new ValueExp(new IntegerValue(2))),
                                new AssignStmt("v", new ValueExp(new IntegerValue(3)))), new PrintStmt(new VarExp("v"))))));

        //Example4:
        //int a;int b;
        //a=10;b=a/0;
        IStmt ex4 = new CompStmt(new VarDeclStmt("a", new IntegerType()),
                                 new CompStmt(new VarDeclStmt("b", new IntegerType()),
                                    new CompStmt(new AssignStmt("a", new ValueExp(new IntegerValue(10))),
                                         new CompStmt(new AssignStmt("b", new ArithExp('/', new VarExp("a"), new ValueExp(new IntegerValue(0)))),
                                                 new PrintStmt(new VarExp("a"))))));


        boolean program_end = true;
        while(program_end) {
            Scanner scan = new Scanner(System.in);
            System.out.println("Choose the example: \n");
            System.out.println("ex1: int v; v=2;\n");
            System.out.println("ex2: int a;int b;\n" + "     a=2+3*5;b=a+1;\n");
            System.out.println("ex3: bool a; int v; a=true;\n" + "     (If a Then v=2 Else v=3);\n");
            System.out.println("ex4: int a; int b;\n" + "     a=10;b=a/0;\n");
            String option = scan.nextLine();

            if(option.equals("ex1")) {
                stack.push(ex1);
            }
            else if(option.equals("ex2")) {
                stack.push(ex2);
            }
            else if(option.equals("ex3")) {
                stack.push(ex3);
            }
            else if(option.equals("ex4")) {
                stack.push(ex4);
            }
            else
                System.out.println("Select a valid example !\n");

            if(!stack.isEmpty()) {
                PrgState program_state = new PrgState(stack, new Dict<String, IValue>(), new List<IValue>());
                System.out.println(program_state);
                try {
                    repository.addPrg(program_state);
                    controller.allStep();
                } catch (Custom_Exception | ADTException | EXPException | STMTException exeption) {
                    System.out.println(exeption.getMessage());
                }
            }
        }
    }
}
