package view.menu;

import entity.Store;
import entity.market.OrderInvoice;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import view.menu.item.InvoiceProductContent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ConfirmOrderScreen implements Initializable, Navigatable {
    private final OrderInvoice orderInvoice;
    private final Consumer<Integer> onOrderAccepted;
    private final Store store;
    @FXML
    private ListView invoiceProductsContents;
    @FXML
    private Label shipmentCostLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private Button approveButton;
    @FXML
    private Label storeNameLabel;
    private Parent content;

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

    public ConfirmOrderScreen(Store store, OrderInvoice orderInvoice, Consumer<Integer> onOrderAccepted) {
        this.onOrderAccepted = onOrderAccepted;
        this.orderInvoice = orderInvoice;
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
    }
}
