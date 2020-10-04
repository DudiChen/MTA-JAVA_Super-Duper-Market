package view.menu;

import controller.Controller;
import entity.Store;
import entity.market.InvoiceDiscountProduct;
import entity.market.InvoiceProduct;
import entity.market.OrderInvoice;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import view.menu.item.InvoiceDiscountProductContent;
import view.menu.item.InvoiceProductContent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ConfirmOrderMenu implements Initializable, Navigatable {
    private final OrderInvoice orderInvoice;
    private final Consumer<Integer> onOrderAccepted;
    private final Store store;
    private final Controller controller;
    @FXML
    private ListView invoiceProductsContents;
    @FXML
    private ListView invoiceDiscountsContents;
    @FXML
    private Label shipmentCostLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private Button approveButton;
    @FXML
    private Label storeNameLabel;
    @FXML
    private Label orderingCustomerLabel;
    private Parent content;

    // TODO: move to utils
    private Parent loadFXML(String name) {
        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample/" + name + ".fxml"));
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/menu/sample/" + name + ".fxml"));
            fxmlLoader.setController(this);
            Parent page = fxmlLoader.load();
            return page;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ConfirmOrderMenu(Store store, OrderInvoice orderInvoice, Consumer<Integer> onOrderAccepted, Controller controller) {
        this.onOrderAccepted = onOrderAccepted;
        this.orderInvoice = orderInvoice;
        this.controller = controller;
        this.store = store;
        this.content = loadFXML("confirmOrder");
    }

    @Override
    public Parent getContent() {
        return this.content;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.storeNameLabel.setText(this.storeNameLabel.getText() + this.store.getName() + " ID: " + this.store.getId() + " At: x: " + store.getLocation().getX() + " y: " + store.getLocation().getY());
            this.approveButton.setOnAction(e -> this.onOrderAccepted.accept(orderInvoice.getOrderId()));
        this.shipmentCostLabel.setText(this.shipmentCostLabel.getText() + String.format("%.2f", orderInvoice.getShipmentPrice()));
        this.totalLabel.setText(this.totalLabel.getText() + String.format("%.2f",orderInvoice.getTotalPrice()));
        this.invoiceProductsContents.setCellFactory(param -> new InvoiceProductContent());
        this.invoiceProductsContents.getItems().addAll(orderInvoice.getInvoiceProducts());
        this.orderingCustomerLabel.setText("Ordering Customer: " +  this.controller.getCustomerById(orderInvoice.getCustomerId()).getName());
        List<InvoiceDiscountProduct> discountProducts = orderInvoice.getDiscountProducts();
        if(discountProducts != null) {
            this.invoiceDiscountsContents.getItems().addAll(discountProducts);
            this.invoiceDiscountsContents.setCellFactory(param -> new InvoiceDiscountProductContent());
        }
    }
}
