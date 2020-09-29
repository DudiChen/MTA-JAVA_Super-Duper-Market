package view.menu.item;

import controller.Controller;
import entity.Discount;
import entity.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.function.Consumer;

public class DiscountContent extends ListCell<Discount> {

    private final Controller controller;
    @FXML
    private Label quantityLabel;
    @FXML
    private ListView<Product> productsInDiscountList;

    public DiscountContent(Controller controller) {
        this.controller = controller;
        loadFXML();
    }

    // TODO: move to utils
    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../sample/discountContent.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void updateItem(Discount discount, boolean empty) {
        super.updateItem(discount, empty);
        if(empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
            Product product = this.controller.getProductById(discount.getProductId());
            this.quantityLabel.setText(Double.toString(discount.getQuantity()));
//            discount.getOffers()
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
