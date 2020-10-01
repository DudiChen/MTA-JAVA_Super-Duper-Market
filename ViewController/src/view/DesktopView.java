package view;

import command.Command;
import command.LoadFromXMLCommand;
import command.ShowMapCommand;
import command.store.GetAllProductsCommand;
import command.store.GetAllStoresCommand;
import command.store.ShowOrdersHistoryCommand;
import controller.Controller;
import entity.Product;
import entity.Store;
import entity.market.OrderInvoice;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.menu.*;
import view.menu.item.MapElement;
import view.menu.item.ProductContent;
import view.menu.item.ProductsContentFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DesktopView extends View {

    private Stage primaryStage;
    private FileChooser fileChooser;
    private Parent mainMenu;
    private StoresMenu storesMenu;
    private Controller controller;
    private ApplicationContext appContext;

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab storesTab;
    @FXML
    private Tab productsTab;
    @FXML
    private Tab ordersTab;
    @FXML
    private Tab mapTab;
    @FXML
    private Button loadXmlButton;
    @FXML
    private ProgressBar loadXmlProgressBar;
    @FXML
    private Label xmlProgressLabel;

    public DesktopView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.fileChooser = new FileChooser();
        this.appContext = ApplicationContext.getInstance();
    }

    @Override
    public void displayStores(List<Store> allStores) {
        if (!storesTab.isSelected()) {
            return;
        }
        this.appContext.setRoot(storesTab);
        this.storesMenu = new StoresMenu(allStores, onStoreIdChoice, this.appContext, primaryStage, controller);
        this.storesMenu.setOnOrderPlaced(onOrderPlaced);
        this.appContext.navigate(this.storesMenu);
    }


    @Override
    public void displayProducts(List<Product> allProducts, List<Store> allStores) {
        if (!productsTab.isSelected()) {
            return;
        }
        this.appContext.setRoot(productsTab);
        ProductsMenu<ProductContent> productsMenu = new ProductsMenu<>(allProducts, new ProductsContentFactory(allStores), controller);
        productsMenu.setOnOrderPlaced(this.onDynamicOrder);
        this.appContext.navigate(productsMenu);
    }

    @Override
    public void summarizeOrder(OrderInvoice orderInvoice) {
        this.appContext.navigate(new ConfirmOrderScreen(this.controller.getStoreById(orderInvoice.getStoreId()), orderInvoice, id -> {
            onOrderAccepted.accept(id);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Order ID " + id + " Received");
            alert.show();
            if (this.storesTab.isSelected()) {
                this.executeOperation(new GetAllStoresCommand());
            } else if (this.productsTab.isSelected()) {
                this.appContext.navigateBack();
            }
        }));
    }

    @Override
    public void displayError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    @Override
    public void fileLoadedSuccessfully() {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setContentText("File Loaded Successfully");
//        alert.show();
    }

    @Override
    public void showOrdersHistory(List<OrderInvoice> ordersHistory) {
        if (!ordersTab.isSelected()) {
            return;
        }
        this.appContext.setRoot(ordersTab);
        OrdersMenu ordersMenu = new OrdersMenu(ordersHistory, this.controller);
        this.appContext.navigate(ordersMenu);
    }

    @Override
    public String promptUserFilePath() {
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        return selectedFile.getAbsolutePath();
    }

    @Override
    public void showMainMenu() {
        this.mainMenu = loadFXML("mainMenu");
        assert this.mainMenu != null;
        primaryStage.setScene(new Scene(this.mainMenu));
        primaryStage.show();
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void displayStoresList(List<Store> stores) {

    }

    @Override
    public void showMap(List<MapElement> mapElements) {
        if (!mapTab.isSelected()) {
            return;
        }
        this.appContext.setRoot(mapTab);
        MapMenu mapMenu = new MapMenu(mapElements);
        this.appContext.navigate(mapMenu);
    }

    @Override
    public void displayProductsForStore(List<Product> products, Store store) {
//        if (!this.storesTab.isSelected()) {
//            return;
//        }
        this.storesMenu.orderFromStore(products, store);
    }

    @Override
    public StringProperty xmlProgressStateProperty() {
        return this.xmlProgressLabel.textProperty();
    }

    @Override
    public DoubleProperty xmlProgressBarProperty() {
        return loadXmlProgressBar.progressProperty();
    }

    // TODO: move to utils
    private Parent loadFXML(String name) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("menu/sample/" + name + ".fxml"));
            fxmlLoader.setController(this);
            Parent page = fxmlLoader.load();
            return page;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void fetchAllStoresToUI(Event event) {
        executeOperation(new GetAllStoresCommand());
    }

    public void fetchAllProductsToUI(Event event) {
        executeOperation(new GetAllProductsCommand());
    }

    public void fetchOrdersHistoryToUI(Event event) {
        executeOperation(new ShowOrdersHistoryCommand());
    }

    public void fetchMapToUI(Event event) {
        executeOperation(new ShowMapCommand());
    }

    public void loadXMLDataToUI(Event event) {
        executeOperation(new LoadFromXMLCommand());
    }

    void executeOperation(Command command) {
        this.controller.getExecutor().executeOperation(command);
    }
}
