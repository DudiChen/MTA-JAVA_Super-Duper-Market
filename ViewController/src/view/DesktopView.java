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
import javafx.scene.control.Tab;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.menu.StoresMenu;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DesktopView extends View {

    private Stage primaryStage;
    private FileChooser fileChooser;
    private Parent mainMenu;
    private StoresMenu storesMenu;

    @FXML
    private Tab storesTab;
    @FXML
    private Tab productsTab;
    @FXML
    private Tab ordersTab;

    private Controller controller;

    public DesktopView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.fileChooser = new FileChooser();
    }

    @Override
    public void displayStores(List<Store> allStores) {
        if (!storesTab.isSelected()) {
            return;
        }
        this.storesMenu = new StoresMenu(allStores, onStoreIdChoice, controller);
        this.storesTab.setContent(storesMenu.getContent());
    }


    @Override
    public void displayProducts(List<Product> allProducts, List<Store> allStores) {
    }

    @Override
    public void summarizeOrder(OrderInvoice orderInvoice) {

    }

    @Override
    public void displayError(String message) {
    }

    @Override
    public void fileLoadedSuccessfully() {
        System.out.println("file loaded successful");
    }

    @Override
    public void showOrdersHistory(List<OrderInvoice> ordersHistory) {

    }

    @Override
    public String promptUserFilePath() {
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        return selectedFile.getAbsolutePath();
    }

    @Override
    public void showMainMenu() {
        this.mainMenu = loadFXML("mainMenu");
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
    public void displayProductsList(List<Product> products, Store store) {
        if (!this.storesTab.isSelected()) {
            return;
        }
        this.storesMenu.orderFromStore(products, store);
        this.storesTab.setContent(this.storesMenu.getContent());
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
