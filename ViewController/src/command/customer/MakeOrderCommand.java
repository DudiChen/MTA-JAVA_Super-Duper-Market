package command.customer;

import command.Command;
import controller.Controller;

public class MakeOrderCommand implements Command {
    @Override
    public void execute(Controller controller) {
        controller.makeOrder();
    }
}
