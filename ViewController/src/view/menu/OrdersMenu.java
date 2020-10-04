package view.menu;
import command.store.LoadOrdersHistoryCommand;
import command.store.SaveOrdersHistoryCommand;
import controller.Controller;
import entity.Order;
import entity.market.OrderInvoice;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import view.menu.item.OrderInvoiceContent;
import view.menu.item.StoreContent;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OrdersMenu implements Initializable, Navigatable {

    private final Controller controller;
    private final List<OrderInvoice> ordersHistory;
    private Parent content;

    @FXML
    private ListView<OrderInvoice> orderInvoiceList;
    @FXML
    private Button saveHistoryButton;
    @FXML
    private Button loadHistoryButton;

    public OrdersMenu(List<OrderInvoice> ordersHistory, Controller controller) {
        this.controller = controller;
        this.ordersHistory = ordersHistory;
        this.content = loadFXML("ordersMenu");
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

    @Override
    public Node getContent() {
        return this.content;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.saveHistoryButton.setOnAction(e -> this.controller.getExecutor().executeOperation(new SaveOrdersHistoryCommand()));
        this.loadHistoryButton.setOnAction(e -> this.controller.getExecutor().executeOperation(new LoadOrdersHistoryCommand()));
        this.orderInvoiceList.setCellFactory(param -> new OrderInvoiceContent(this.controller));
        this.orderInvoiceList.getItems().addAll(ordersHistory);
    }
}
