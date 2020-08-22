package command.store;

import command.Command;
import command.Executor;
import controller.Controller;
import entity.Store;

import java.util.List;

public class GetAllStoresCommand implements Command {
    @Override
    public void execute(Controller controller) {
        controller.fetchAllStoresToUI();
    }
}
