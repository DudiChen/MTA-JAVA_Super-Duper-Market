package view.menu.item;

import entity.Product;
import entity.Store;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.NumberStringConverter;

import java.awt.*;
import java.net.URL;
import java.text.ParsePosition;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

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
        Pattern pattern;
//        TextFormatter formatter;
        this.product = product;
        try {
            priceType.setText(priceType.getText() + price.toString());
            switch (product.getPurchaseMethod()) {
                case QUANTITY:
                    priceInput.setText("" + (int) chosenStore.getPriceOfProduct(product.getId()));
                    price.append("Unit");
                    priceInput.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue,
                                            String newValue) {
                            try {
                                int res = Integer.parseInt(priceInput.getText());
                                onPriceChange.accept(product.getId(), (double) res);
                            } catch (NumberFormatException ignored) {

                            }
                        }
                    });
                    break;
                case WEIGHT:
                    price.append("Kilo");
                    priceInput.setText("" + chosenStore.getPriceOfProduct(product.getId()));
                    priceInput.addEventFilter(KeyEvent.KEY_TYPED, event -> {
                        char ar[] = event.getCharacter().toCharArray();
                        if (ar.length == 0) {
                            return;
                        }
                        char ch = ar[event.getCharacter().toCharArray().length - 1];
                        if (!(ch >= '0' && ch <= '9') || priceInput.getText().length() == 0 && ch == '0') {
                            System.out.println("The char you entered is not a number");
                            event.consume();
                        }
                    });

                    priceInput.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue,
                                            String newValue) {
                            try {
                                double res = Double.parseDouble(priceInput.getText());
                                onPriceChange.accept(product.getId(), res);
                            } catch (NumberFormatException ignored) {}
                        }
                    });
                    break;
            }
        } catch (NoSuchElementException e) {
        }
    }

    public void setOnHover(BiConsumer<Product, Point> onHover) {
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            if (!this.isEmpty() && this.getBoundsInLocal().contains(event.getX(), event.getY())) {
                onHover.accept(this.getProduct(), new Point((int) this.getLayoutX(), (int) this.getLayoutY()));
            }
        });
    }

    public void setOnUnHover(BiConsumer<Product, Point> onUnHover) {
        this.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            if (!this.getBoundsInLocal().contains(event.getX(), event.getY())) {
                onUnHover.accept(this.getProduct(), new Point((int) this.getLayoutX(), (int) this.getLayoutY()));
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
