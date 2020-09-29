package view.menu.item;

import entity.Product;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Slider;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractProductContent extends ListCell<Product> implements Initializable {

    @FXML
    protected Label nameLabel;
    @FXML
    protected Label idLabel;
    @FXML
    protected Slider quantitySlider;
    @FXML
    protected Label quantityLabel;

    private Product product;
    private SimpleIntegerProperty productIdProperty;
    private SimpleDoubleProperty quantityProperty;

    public AbstractProductContent() {
        this.productIdProperty = new SimpleIntegerProperty();
        this.quantityProperty = new SimpleDoubleProperty();
        loadFXML(this.getFXMLName());
    }

    // TODO: move to utils
    protected void loadFXML(String name) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../sample/" + name + ".fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
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
            idLabel.setText("ID: " + product.getId());
            quantitySlider.setMin(0);
            quantitySlider.setValue(0);
            quantitySlider.setMax(20);
            switch (product.getPurchaseMethod()) {
                case QUANTITY:
                    quantitySlider.setMajorTickUnit(1);
                    quantitySlider.setMinorTickCount(0);
                    quantitySlider.setShowTickMarks(true);
                    quantitySlider.setShowTickLabels(true);
                    quantitySlider.setSnapToTicks(true);
                    quantitySlider.setMinHeight(Slider.USE_PREF_SIZE);
                    break;
                case WEIGHT:
                    quantityLabel.setLayoutY(quantitySlider.getLayoutY());
                    quantitySlider.valueProperty().addListener((observable, old_val, new_val) -> {
                        if(new_val.doubleValue() != 0){
                            quantityLabel.setText(String.format("%.2f", new_val));
                        }
                        else {
                            quantityLabel.setText(null);
                        }
                    });
                    break;
            }
            this.bind(this.product);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
    protected abstract String getFXMLName();
    protected abstract void bind(Product product);

    protected Product getProduct(){
        return this.product;
    }
}
