package command.store;

import command.Command;
import controller.Controller;

public class ShowOrdersHistoryCommand implements Command {
    @Override
    public void execute(Controller controller) {
        controller.fetchOrdersHistoryToUI();
    }
}
