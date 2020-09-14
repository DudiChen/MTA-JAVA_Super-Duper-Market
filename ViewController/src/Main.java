import controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;
import view.DesktopView;

public class Main extends Application {

    public static void main(String[] args) {
//        View view = new ConsoleView();
//        Controller controller = new Controller(view);
//        controller.run();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Controller controller = new Controller(new DesktopView(primaryStage));
        controller.run();
    }
}
