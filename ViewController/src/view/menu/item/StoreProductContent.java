package view.menu.item;

import entity.Product;
import entity.Store;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class StoreProductContent extends AbstractProductContent {
    @FXML
    private Label priceType;
    @FXML
    private TextField priceInput;
    @FXML
    private Button deleteButton;
    private Store chosenStore;
    private Consumer<Integer> onDelete;
    private BiConsumer<Integer, Double> onPriceChange;
    private Product product;

    public StoreProductContent(Store chosenStore) {
        super();
        this.chosenStore = chosenStore;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.deleteButton.setOnAction(event -> onDelete.accept(this.product.getId()));
    }

    @Override
    public String getFXMLName() {
        return "storeProduct";
    }

    @Override
    protected void bind(Product product) {
        StringBuilder price = new StringBuilder();
        this.product = product;
        try {
            priceType.setText(priceType.getText() + price.toString());
            switch (product.getPurchaseMethod()) {
                case QUANTITY:
                    priceInput.setText("" + (int)chosenStore.getPriceOfProduct(product.getId()));
                    price.append("Unit");
                    priceInput.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue,
                                            String newValue) {
                            if (!newValue.matches("\\d*")) {
                                priceInput.setText(newValue.replaceAll("[^\\d]", ""));
                            }
                            else {
                                onPriceChange.accept(product.getId(), Double.parseDouble(priceInput.getText()));
                            }
                        }
                    });
                    break;
                case WEIGHT:
                    price.append("Kilo");
                    priceInput.setText("" + chosenStore.getPriceOfProduct(product.getId()));
                    priceInput.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue,
                                            String newValue) {
                            if (!newValue.matches("[0-9]*<range>}(\\.[0-9]*)?")) {
                                priceInput.setText(newValue.replaceAll("[^\\d.]", ""));
                                StringBuilder aus = new StringBuilder(newValue);
                                boolean firstPointFound = false;
                                for (int i = 0; i < aus.length(); i++){
                                    if(aus.charAt(i) == '.') {
                                        if(!firstPointFound)
                                            firstPointFound = true;
                                        else
                                            aus.deleteCharAt(i);
                                    }
                                }
                                newValue = aus.toString();
                                onPriceChange.accept(product.getId(), Double.parseDouble(priceInput.getText()));
                            }
                        }
                    });
                    break;
            }

            // force the field to be numeric only

        }catch(NoSuchElementException e) {
        }

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

    public void setOnDelete(Consumer<Integer> onDelete) {
        this.onDelete = onDelete;
    }

    public void setOnPriceChange(BiConsumer<Integer, Double> onPriceChange) {
        this.onPriceChange = onPriceChange;
    }
}
