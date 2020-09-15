package view.menu.item;

import entity.Product;
import entity.Store;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.function.Consumer;

public class StoreProductContent extends ListCell<Product> {

    @FXML
    private Label nameLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private ComboBox<Integer> quantityBox;
    private Store chosenStore;

    public StoreProductContent(Store chosenStore) {
        this.chosenStore = chosenStore;
        loadFXML();
    }

    // TODO: move to utils
    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../sample/product.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(Product product, boolean empty) {
        super.updateItem(product, empty);
        if (empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            nameLabel.setText(product.getName());
            idLabel.setText(idLabel.getText() + product.getId());
            StringBuilder price = new StringBuilder();
            price.append(priceLabel.getText()).append(chosenStore.getPriceOfProduct(product.getId())).append(" per ");
            switch (product.getPurchaseMethod()){
                case QUANTITY:
                    price.append("unit");
                    break;
                case WEIGHT:
                    price.append("kilo");
                    break;
            }
            priceLabel.setText(price.toString());
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
