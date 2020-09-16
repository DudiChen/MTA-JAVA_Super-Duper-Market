package view.menu;

import command.store.GetAllStoresCommand;
import controller.Controller;
import entity.Product;
import entity.Store;
import entity.market.OrderInvoice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.util.Pair;
import view.TriConsumer;
import view.menu.item.StoreContent;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class StoresMenu implements Initializable {

    private final Controller controller;
    @FXML
    private ListView storesContentsList;
    private List<Store> storesDataList;
    private Parent content;
    private Consumer<Integer> onStoreIdChoice;
    private Parent mainScreen;
    private ProductsMenu currentProductsMenu;
    Consumer<ActionEvent> backToHere;
    public void setOnOrderPlaced(TriConsumer<Date, Point, List<Pair<Integer, Double>>> onOrderPlaced) {
        this.onOrderPlaced = onOrderPlaced;
    }

    private TriConsumer<Date, Point, List<Pair<Integer, Double>>> onOrderPlaced;

    public StoresMenu(List<Store> storesDataList, Consumer<Integer> onStoreIdChoice, Controller controller) {
        this.controller = controller;
        this.storesDataList = storesDataList;
        this.content = loadFXML("storesMenu");
        this.onStoreIdChoice = onStoreIdChoice;
        this.backToHere = event -> this.controller.getExecutor().executeOperation(new GetAllStoresCommand());
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

    public Parent getContent() {
        return this.content;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.storesContentsList.setCellFactory(param -> new StoreContent(onStoreIdChoice));
        this.storesContentsList.getItems().addAll(storesDataList);
    }

    public void orderFromStore(List<Product> products, Store store) {
        this.mainScreen = this.content;
        ProductsMenu storeProductsMenu = new ProductsMenu(this.controller, products, this.backToHere, store);
        storeProductsMenu.setOnOrderPlaced((d, p, id) -> {
            this.currentProductsMenu = storeProductsMenu;
            this.onOrderPlaced.apply(d, p, id);
        });
        this.content = storeProductsMenu.getContent();
    }

    public void confirmOrder(OrderInvoice orderInvoice) {
        this.mainScreen = this.content;
        ConfirmOrderScreen confirmOrderScreen = new ConfirmOrderScreen(orderInvoice, this.backToHere);
        this.content = confirmOrderScreen.getContent();
    }
}
