package view.menu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import view.menu.Navigatable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BackButtonMenu implements Initializable, Navigatable {

    private final Parent content;
    private Runnable navigateBack;
    @FXML
    private Button backButton;
    @FXML
    private VBox mainContent;

    public BackButtonMenu(Runnable navigateBack) {
        this.navigateBack = navigateBack;
        this.content = loadFXML("backButton");
    }

    // TODO: move to utils
    private Parent loadFXML(String name) {
        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/menu/sample/" + name + ".fxml"));
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/menu/sample/" + name + ".fxml"));
            fxmlLoader.setController(this);
            Parent page = fxmlLoader.load();
            return page;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public VBox getMainContent() {
        return mainContent;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.backButton.setOnAction(e -> navigateBack.run());
    }

    @Override
    public Node getContent() {
        return this.content;
    }
}
