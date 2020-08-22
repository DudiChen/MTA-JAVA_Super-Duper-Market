package command.store;

import command.Command;
import controller.Controller;

public class PerformOrderCommand implements Command {
    @Override
    public void execute(Controller controller) {
        controller.performOrder();
    }
}
