package command.customer;

import command.Command;
import controller.Controller;

public class MakeDynamicOrderCommand implements Command {
    @Override
    public void execute(Controller controller) {
        controller.makeDynamicOrder();
    }
}
