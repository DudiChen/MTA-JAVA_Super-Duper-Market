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
        price.append("Price: ").append(chosenStore.getPriceOfProduct(product.getId())).append(" per ");
        switch (product.getPurchaseMethod()) {
            case QUANTITY:
                price.append("Unit");
                break;
            case WEIGHT:
                price.append("Kilo");
                break;
        }
        priceLabel.setText(price.toString());
    }
}
