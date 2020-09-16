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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.util.Pair;
import view.ApplicationContext;
import view.TriConsumer;
import view.menu.item.StoreContent;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class StoresMenu implements Initializable, Navigatable {

    @FXML
    private ListView storesContentsList;
    private List<Store> storesDataList;
    private Parent content;
    private Consumer<Integer> onStoreIdChoice;
    private ApplicationContext applicationContext;
    private TriConsumer<Date, Point, List<Pair<Integer, Double>>> onOrderPlaced;

    public void setOnOrderPlaced(TriConsumer<Date, Point, List<Pair<Integer, Double>>> onOrderPlaced) {
        this.onOrderPlaced = onOrderPlaced;
    }


    public StoresMenu(List<Store> storesDataList, Consumer<Integer> onStoreIdChoice, ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.storesDataList = storesDataList;
        this.onStoreIdChoice = onStoreIdChoice;
        this.content = loadFXML("storesMenu");
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
        this.storesContentsList.setCellFactory(param -> new StoreContent(onStoreIdChoice, applicationContext));
        this.storesContentsList.getItems().addAll(storesDataList);
    }

    public void orderFromStore(List<Product> products, Store store) {
        ProductsMenu storeProductsMenu = new ProductsMenu(products, store);
        storeProductsMenu.setOnOrderPlaced((d, p, id) -> {
            this.onOrderPlaced.apply(d, p, id);
        });
        this.applicationContext.navigate(storeProductsMenu);
    }
}
