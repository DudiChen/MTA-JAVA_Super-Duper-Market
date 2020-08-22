package command;

import controller.Controller;

@FunctionalInterface
public interface Command {
    void execute(Controller controller);
}
