package view.menu.item;

import entity.market.InvoiceDiscountProduct;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class InvoiceDiscountProductContent extends ListCell<InvoiceDiscountProduct> {

    @FXML
    private Label additionalCostLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label quantityLabel;
    @FXML
    private Label discountNameLabel;

    public InvoiceDiscountProductContent() {
        loadFXML();
    }

    // TODO: move to utils
    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../sample/invoiceDiscountContent.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(InvoiceDiscountProduct invoiceProduct, boolean empty) {
        super.updateItem(invoiceProduct, empty);
        if (empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            double additionalCost = invoiceProduct.getAdditionalCost();
            int id = invoiceProduct.getProductId();
            String name = invoiceProduct.getName();
            double quantity = invoiceProduct.getQuantity();
            String discountName = invoiceProduct.getDiscountName();

            this.additionalCostLabel.setText(additionalCostLabel.getText() + additionalCost);
            this.discountNameLabel.setText(this.discountNameLabel.getText() + discountName + " Discount");
            this.idLabel.setText(this.idLabel.getText() + id);
            this.nameLabel.setText(this.nameLabel.getText() + name);
            this.quantityLabel.setText(this.quantityLabel.getText() + quantity);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
