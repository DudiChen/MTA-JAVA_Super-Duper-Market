package command.product;

import command.Command;
import command.Executor;
import controller.Controller;
//import com.sdp.model.Product;

public class AddProductCommand implements Command {
//    Product product;
    @Override
    public void execute(Controller controller) {
        controller.addNewProduct();
    }
}
