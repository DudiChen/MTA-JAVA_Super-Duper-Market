package command.store;

import command.Command;
import controller.Controller;

public class SaveOrdersHistoryCommand implements Command {
    @Override
    public void execute(Controller controller) {
        controller.saveOrderHistory();
    }
}
