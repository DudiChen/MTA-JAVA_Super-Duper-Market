package command;

import controller.Controller;

public class ShowMapCommand implements Command {
    @Override
    public void execute(Controller controller) {
        controller.fetchMapToUI();
    }
}
