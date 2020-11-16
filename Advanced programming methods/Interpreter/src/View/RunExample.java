package View;

import Controller.Controller;
import Model.Exceptions.ADTException;
import Model.Exceptions.Custom_Exception;
import Model.Exceptions.EXPException;
import Model.Exceptions.STMTException;

import java.io.IOException;

public class RunExample extends Command {
    private Controller controller;

    public RunExample(String key, String description, Controller controller) {
        super(key, description);
        this.controller = controller;
    }

    @Override
    public void execute() {
        try {
            controller.allStep();
        }
        catch (Custom_Exception | ADTException | EXPException | STMTException | IOException e) {
            System.out.println(e.toString());
        }
    }
}
