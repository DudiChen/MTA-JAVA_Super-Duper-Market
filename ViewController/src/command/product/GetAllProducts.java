package command.product;

import command.Command;
//import com.sdp.controller.command.Executor;
import controller.Controller;

public class GetAllProducts implements Command {

    @Override
    public void execute(Controller controller) {
        controller.fetchAllProductsToUI();
    }
}
