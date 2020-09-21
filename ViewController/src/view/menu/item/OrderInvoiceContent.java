package view.menu.item;

import controller.Controller;
import entity.Product;
import entity.market.OrderInvoice;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderInvoiceContent extends ListCell<OrderInvoice> {

    private Controller controller;
    @FXML
    private Label idLabel;
    @FXML
    private Label deliveryDateLabel;
    @FXML
    private Label providingStoreLabel;
    @FXML
    private Label numberOfItemsLabel;
    @FXML
    private Label totalProductsPriceLabel;
    @FXML
    private Label shipmentPriceLabel;
    @FXML
    private Label totalPriceLabel;

    public OrderInvoiceContent(Controller controller) {
        this.controller = controller;
        this.loadFXML();
    }

    // TODO: move to utils
    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../sample/orderInvoice.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(OrderInvoice orderInvoice, boolean empty) {
        super.updateItem(orderInvoice, empty);
        if (empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            this.idLabel.setText("ID: " + orderInvoice.getOrderId());
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date dt = orderInvoice.getDeliveryDate();
            String s = sdf.format(dt);
            this.deliveryDateLabel.setText("Delivery Date: " + s);
            this.providingStoreLabel.setText("Providing Store: " + this.controller.getStoreNameByID(orderInvoice.getStoreId()) + ", ID: " + orderInvoice.getStoreId());
            this.numberOfItemsLabel.setText("Number of Items: " + orderInvoice.getInvoiceProducts().size());
            this.totalPriceLabel.setText("Total Price: " + String.format("%.2f", orderInvoice.getTotalPrice() - orderInvoice.getShipmentPrice()));
            this.shipmentPriceLabel.setText("Shipment Cost: " + String.format("%.2f", orderInvoice.getShipmentPrice()));
            this.totalPriceLabel.setText("Total Price: " + String.format("%.2f", orderInvoice.getTotalPrice()));
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
