package view.menu;

import controller.Controller;
import entity.Product;
import entity.Store;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import view.menu.item.StoreProductContent;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ProductsMenu implements Initializable {
    @FXML
    private ListView productsList;
    private final Controller controller;
    private final Consumer<ActionEvent> onBack;
    private Parent content;
    @FXML
    private Button backButton;
    private List<Product> products;
    private Store chosenStore;

    public ProductsMenu(Controller controller, List<Product> products, Consumer<ActionEvent> onBack, Store chosenStore) {
        this.products = products;
        this.controller = controller;
        this.onBack = onBack;
        this.chosenStore = chosenStore;
        this.content = loadFXML("storeProducts");
    }

    public ProductsMenu(Controller controller, List<Product> products, Consumer<ActionEvent> onBack) {
        this.products = products;
        this.controller = controller;
        this.onBack = onBack;
        this.content = loadFXML("storeProducts");
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.backButton.setOnAction(onBack::accept);
        if(this.chosenStore != null){
            this.productsList.setCellFactory(param -> new StoreProductContent(chosenStore));
        }
        else {
            //this.productsContentsList.setCellFactory(param -> new GeneralProductContent());
        }
        this.productsList.getItems().addAll(products);
    }
}
