package view.menu;

import entity.market.OrderInvoice;
import javafx.event.ActionEvent;
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
    @FXML
    private ListView invoiceProductsContents;
    @FXML
    private Label shipmentCostLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private Button approveButton;
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

    public ConfirmOrderScreen(OrderInvoice orderInvoice, Consumer<Integer> onOrderAccepted) {
        this.onOrderAccepted = onOrderAccepted;
        this.orderInvoice = orderInvoice;
        this.content = loadFXML("confirmOrder");
    }

    @Override
    public Parent getContent() {
        return this.content;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.approveButton.setOnAction(e -> this.onOrderAccepted.accept(orderInvoice.getOrderId()));
        this.shipmentCostLabel.setText(this.shipmentCostLabel.getText() + orderInvoice.getShipmentPrice());
        this.totalLabel.setText(this.totalLabel.getText() + orderInvoice.getTotalPrice());
        this.invoiceProductsContents.setCellFactory(param -> new InvoiceProductContent());
        this.invoiceProductsContents.getItems().addAll(orderInvoice.getInvoiceProducts());
    }
}
