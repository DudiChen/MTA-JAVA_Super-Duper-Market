package command;

//import command.Command;
//import com.sdp.model.AppData;
//import com.sdp.model.ID;
//import com.sdp.model.Store;
//import com.sdp.view.View;
import controller.Controller;

import java.util.ArrayList;
import java.util.List;

public class Executor {
    // TODO: [Dudi]: Make Controller member final
    private Controller controller;
    private final List<Command> commandsHistory = new ArrayList<>();

    public Executor(Controller controller) {
        this.controller = controller;
    }
//    private AppData appData;
//    private View view;

//    public AppData getModel() {
//        return this.appData;
//    }

//    public View getView() {
//        return this.view;
//    }

    public Executor() {
        //this.appData = new AppData();
        //this.view = new View(this, appData);
    }

    public void executeOperation(Command command) {
        this.commandsHistory.add(command);
        command.execute(controller);
    }
}
