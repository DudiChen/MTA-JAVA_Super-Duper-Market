package view.menu.item;

import entity.Product;
import entity.Store;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StoreProductContent extends AbstractProductContent {
    @FXML
    private Label priceLabel;
    private Store chosenStore;

    public StoreProductContent(Store chosenStore) {
        super();
        this.chosenStore = chosenStore;
    }

    @Override
    public String getFXMLName() {
        return "storeProduct";
    }

    @Override
    protected void bind(Product product) {
        StringBuilder price = new StringBuilder();
        price.append(this.priceLabel.getText()).append(chosenStore.getPriceOfProduct(product.getId())).append(" per ");
        switch (product.getPurchaseMethod()) {
            case QUANTITY:
                price.append("unit");
                break;
            case WEIGHT:
                price.append("kilo");
                break;
        }
        priceLabel.setText(price.toString());
    }
}
