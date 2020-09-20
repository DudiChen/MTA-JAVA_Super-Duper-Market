package view.menu.item;

import entity.Store;
import entity.market.InvoiceProduct;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class InvoiceProductContent extends ListCell<InvoiceProduct> {

    @FXML
    private Label idLabel;
    @FXML
    private Label purchaseMethodLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label quantityLabel;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private Label nameLabel;

    public InvoiceProductContent() {
        loadFXML();
    }

    // TODO: move to utils
    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../sample/invoiceProduct.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(InvoiceProduct invoiceProduct, boolean empty) {
        super.updateItem(invoiceProduct, empty);
        if (empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            this.nameLabel.setText(invoiceProduct.getName());
            this.idLabel.setText(this.idLabel.getText() + invoiceProduct.getId());
            this.purchaseMethodLabel.setText(this.purchaseMethodLabel.getText() + invoiceProduct.getPurchaseMethod());
            this.priceLabel.setText(this.priceLabel.getText() + invoiceProduct.getPrice());
            this.quantityLabel.setText(this.quantityLabel.getText() + String.format("%.2f", invoiceProduct.getQuantity()));
            this.totalPriceLabel.setText(this.totalPriceLabel.getText() + String.format("%.2f", invoiceProduct.getTotalPrice()));
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
