package view.menu;

import controller.Controller;
import entity.Product;
import entity.Store;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Pair;
import view.ApplicationContext;
import view.TriConsumer;
import view.menu.item.StoreContent;
import view.menu.item.StoreProductContent;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class StoresMenu implements Initializable, Navigatable {

    private final Stage primaryStage;
    @FXML
    private ListView storesContentsList;
    private List<Store> storesDataList;
    private Parent content;
    private Consumer<Integer> onStoreIdChoice;
    private ApplicationContext applicationContext;
    private TriConsumer<Date, Point, List<Pair<Integer, Double>>> onOrderPlaced;
    private Controller controller;

    public void setOnOrderPlaced(TriConsumer<Date, Point, List<Pair<Integer, Double>>> onOrderPlaced) {
        this.onOrderPlaced = onOrderPlaced;
    }

    public StoresMenu(List<Store> storesDataList, Consumer<Integer> onStoreIdChoice, ApplicationContext applicationContext, Stage primaryStage, Controller controller) {
        this.controller = controller;
        this.applicationContext = applicationContext;
        this.storesDataList = storesDataList;
        this.onStoreIdChoice = onStoreIdChoice;
        this.content = loadFXML("storesMenu");
        this.primaryStage = primaryStage;
    }

    // TODO: move to utils
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
    public Node getContent() {
        return this.content;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.storesContentsList.setCellFactory(param -> new StoreContent(onStoreIdChoice));
        this.storesContentsList.getItems().addAll(storesDataList);
    }

    public void orderFromStore(List<Product> products, Store store) {
        StoreProductsMenu<StoreProductContent> storeProductsMenu = new StoreProductsMenu<>(products, store, this.onOrderPlaced, this.primaryStage, this.controller);
        this.applicationContext.navigate(storeProductsMenu);
    }

}
