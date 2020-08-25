package view.menu;

import command.Command;
import controller.Controller;

import java.io.IOException;

public class MenuItemActionable extends MenuItem {
    Controller controller;
    private Command command;

    public MenuItemActionable(Controller controller, String titleNameString) {
        super(titleNameString);
        this.controller = controller;
    }

    // TODO: Consider changing method name
    public void setOnSelectedCommand(Command command) {
        this.command = command;
    }

    public void removeOnSelectedListener() {
        command = null;
    }

    private void executeCommand() {
        if (command != null)
        {
            command.execute(controller);
        }
    }

    @Override
    public boolean startMenuItem() {
        executeCommand();
        System.out.println("Enter any key to continue...");
        try {
            System.in.read();
        } catch (IOException e) {}
        return true;
    }

}
