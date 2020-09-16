package view.menu.item;

import entity.Product;
import entity.Store;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class StoreProductContent extends ListCell<Product> implements Initializable {

    @FXML
    private Label nameLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Slider quantitySlider;
    @FXML
    private Label quantityLabel;
    private Store chosenStore;
    private Product product;
    private SimpleIntegerProperty productIdProperty;
    private SimpleDoubleProperty quantityProperty;

    public StoreProductContent(Store chosenStore) {
        this.productIdProperty = new SimpleIntegerProperty();
        this.quantityProperty = new SimpleDoubleProperty();
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
            this.product = product;
            this.productIdProperty.setValue(this.product.getId());
            nameLabel.setText(product.getName());
            idLabel.setText(idLabel.getText() + product.getId());
            StringBuilder price = new StringBuilder();
            price.append(priceLabel.getText()).append(chosenStore.getPriceOfProduct(product.getId())).append(" per ");
            quantitySlider.setMin(0);
            quantitySlider.setValue(0);
            quantitySlider.setMax(20);
            switch (product.getPurchaseMethod()) {
                case QUANTITY:
                    price.append("unit");
                    quantitySlider.setMajorTickUnit(1);
                    quantitySlider.setMinorTickCount(0);
                    quantitySlider.setShowTickMarks(true);
                    quantitySlider.setShowTickLabels(true);
                    quantitySlider.setSnapToTicks(true);
                    quantitySlider.setMinHeight(Slider.USE_PREF_SIZE);
                    break;
                case WEIGHT:
                    price.append("kilo");
                    quantityLabel.setLayoutY(quantitySlider.getLayoutY());
                    quantitySlider.valueProperty().addListener((observable, old_val, new_val) -> {
                        if(new_val.doubleValue() != 0){
                            quantityLabel.setText(String.format("%.2f", new_val));
                        }
                        else {
                            quantityLabel.setText(null);
                        }
                    });

                    System.out.println(quantityLabel.getText());
                    break;
            }
            priceLabel.setText(price.toString());
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    public SimpleIntegerProperty getProductIdProperty() {
        return productIdProperty;
    }

    public SimpleDoubleProperty getQuantityProperty() {
        return quantityProperty;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.quantitySlider.valueProperty().bindBidirectional(this.quantityProperty);
    }
}
