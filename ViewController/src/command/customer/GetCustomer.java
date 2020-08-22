package command.customer;

import command.Command;
//import command.Executor;
import controller.Controller;

public class GetCustomer implements Command {
    @Override
    public void execute(Controller controller) {
        controller.getCustomerToUI();
    }
}
