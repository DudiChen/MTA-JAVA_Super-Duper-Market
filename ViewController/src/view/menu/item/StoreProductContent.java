package view.menu.item;

import entity.Product;
import entity.Store;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.util.function.BiConsumer;

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

    public void setOnHover(BiConsumer<Product, Point> onHover) {
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            if (!this.isEmpty() && this.getBoundsInLocal().contains(event.getX(), event.getY())) {
                onHover.accept(this.getProduct(), new Point((int)this.getLayoutX(), (int)this.getLayoutY()));
            }
        });
    }

    public void setOnUnHover(BiConsumer<Product, Point> onUnHover) {
        this.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            if(!this.getBoundsInLocal().contains(event.getX(), event.getY())) {
                onUnHover.accept(this.getProduct(), new Point((int)this.getLayoutX(), (int)this.getLayoutY()));
            }
        });
    }
}
