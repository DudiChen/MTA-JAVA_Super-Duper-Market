package command;

import command.Command;
import controller.Controller;

public class LoadFromXMLCommand implements Command {
    @Override
    public void execute(Controller controller) {
        controller.loadXMLDataToUI();
    }
}
