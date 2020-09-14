package view.menu;

import command.store.GetAllStoresCommand;
import controller.Controller;
import entity.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ProductsMenu implements Initializable {
    @FXML
    private ListView productsContentsList;
    private final Controller controller;
    private final Consumer<ActionEvent> onBack;
    private Parent content;
    @FXML
    private Button backButton;

    public ProductsMenu(Controller controller, List<Product> products, Consumer<ActionEvent> onBack) {
        System.out.println(products);
        this.controller = controller;
        this.content = loadFXML("storeProducts");
        this.onBack = onBack;
    }

    public Parent getContent() {
        return this.content;
    }

    //TODO: move to utils
    private Parent loadFXML(String name) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample/" + name + ".fxml"));
            fxmlLoader.setController(this);
            Parent page = fxmlLoader.load();
            return page;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void initialize(URL location, ResourceBundle resources) {
        this.backButton.setOnAction(event -> onBack.accept(event));
    }
}
