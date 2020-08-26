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
    private final Controller controller;
    private final List<Command> commandsHistory = new ArrayList<>();

    public Executor(Controller controller) {
        this.controller = controller;
    }

    public void executeOperation(Command command) {
        this.commandsHistory.add(command);
        command.execute(controller);
    }
}
