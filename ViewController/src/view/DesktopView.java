package view;

import command.Command;
import command.store.GetAllProductsCommand;
import command.store.GetAllStoresCommand;
import command.store.ShowOrdersHistoryCommand;
import controller.Controller;
import entity.Product;
import entity.Store;
import entity.market.OrderInvoice;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.menu.ConfirmOrderScreen;
import view.menu.OrdersMenu;
import view.menu.ProductsMenu;
import view.menu.StoresMenu;
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
    private Tab storesTab;
    @FXML
    private Tab productsTab;
    @FXML
    private Tab ordersTab;

    public DesktopView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.fileChooser = new FileChooser();
        this.appContext = new ApplicationContext();
    }

    @Override
    public void displayStores(List<Store> allStores) {
        if (!storesTab.isSelected()) {
            return;
        }
        this.appContext.setRoot(storesTab);
        this.storesMenu = new StoresMenu(allStores, onStoreIdChoice, this.appContext);
        this.storesMenu.setOnOrderPlaced(onOrderPlaced);
        this.appContext.navigate(this.storesMenu);
    }


    @Override
    public void displayProducts(List<Product> allProducts, List<Store> allStores) {
        if (!productsTab.isSelected()) {
            return;
        }
        this.appContext.setRoot(productsTab);
        ProductsMenu<ProductContent> productsMenu = new ProductsMenu<>(allProducts, new ProductsContentFactory(allStores));
        productsMenu.setOnOrderPlaced(this.onDynamicOrder);
        this.appContext.navigate(productsMenu);
    }

    @Override
    public void summarizeOrder(OrderInvoice orderInvoice) {
        if (!this.storesTab.isSelected()) {
            return;
        }
        this.appContext.navigate(new ConfirmOrderScreen(orderInvoice, id -> {
            onOrderAccepted.accept(id);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Order ID " + id + " Received");
            alert.show();
            this.executeOperation(new GetAllStoresCommand());
        }));
    }

    @Override
    public void displayError(String message) {
        System.out.println(message);
    }

    @Override
    public void fileLoadedSuccessfully() {
        System.out.println("file loaded successfuly");
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
    public void displayProductsForStore(List<Product> products, Store store) {
        if (!this.storesTab.isSelected()) {
            return;
        }
        this.storesMenu.orderFromStore(products, store);
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

    void executeOperation(Command command) {
        this.controller.getExecutor().executeOperation(command);
    }
}
