package command.store;

import command.Command;
import controller.Controller;

public class LoadOrdersHistoryCommand implements Command {
    @Override
    public void execute(Controller controller) {
        controller.loadOrderHistory();
    }
}
