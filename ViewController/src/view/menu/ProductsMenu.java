package view.menu;

import entity.Product;
import exception.OrderValidationException;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Pair;
import view.TriConsumer;
import view.menu.item.*;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ProductsMenu<T extends AbstractProductContent> implements Initializable, Navigatable {

    private final ProductsContentFactory productsContentFactory;
    private Parent content;
    private List<Product> products;
    private TriConsumer<Date, Point, List<Pair<Integer, Double>>> onOrderPlaced;
    private List<Pair<SimpleIntegerProperty, SimpleDoubleProperty>> chosenProductToQuantity;
    @FXML
    private Button orderButton;
    @FXML
    private DatePicker deliveryDatePicker;
    @FXML
    private ListView productsList;

    public ProductsMenu(List<Product> products, ProductsContentFactory productsContentFactory) {
        this.productsContentFactory = productsContentFactory;
        this.products = products;
        this.chosenProductToQuantity = new ArrayList<>();
        this.content = loadFXML("storeProducts");
    }

    //TODO: move to utils
    private Parent loadFXML(String name) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample/" + name + ".fxml"));
            fxmlLoader.setController(this);
            Parent page = fxmlLoader.load();
            return page;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.orderButton.setOnAction(this::onOrder);
        this.productsList.setCellFactory(param -> {
            AbstractProductContent productContent = this.productsContentFactory.getItem();
//
//            if (this.chosenStore != null) {
//                ProductContent storeProductContent = new StoreProductContent(chosenStore);
//            } else {
//                productContent = new ProductContent();
//            }
            Pair<SimpleIntegerProperty, SimpleDoubleProperty> bindings = new Pair<>(new SimpleIntegerProperty(), new SimpleDoubleProperty());
            this.chosenProductToQuantity.add(bindings);
            bindings.getKey().bindBidirectional(productContent.getProductIdProperty());
            bindings.getValue().bindBidirectional(productContent.getQuantityProperty());
            return productContent;
        });
        this.productsList.getItems().addAll(products);
    }

    private void onOrder(ActionEvent actionEvent) {
        StringBuilder err = new StringBuilder();
        Date date = null;
        // validate everything
        if (this.deliveryDatePicker.getValue() == null) {
            err.append("Please enter a delivery date first").append(System.lineSeparator());
        } else {
            date = Date.from(this.deliveryDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        Point point = new Point(0, 0);

        // filter all 0 quantities and transform from binding properties to values
        List<Pair<Integer, Double>> chosenProductToQuantity =
                this.chosenProductToQuantity.stream()
                        .filter(pair -> pair.getValue().getValue() != 0)
                        .map(pair -> new Pair<>(pair.getKey().get(), pair.getValue().get()))
                        .distinct()
                        .collect(Collectors.toList());

        if (chosenProductToQuantity.stream().map(Pair::getValue).mapToDouble(Double::doubleValue).sum() == 0) {
            err.append("Please choose products to order").append(System.lineSeparator());
        }
        if (!err.toString().isEmpty()) {
            Alert invalidInputAlert = new Alert(Alert.AlertType.WARNING);
            invalidInputAlert.setContentText(err.toString());
            invalidInputAlert.show();
            return;
        }
        try {
            onOrderPlaced.apply(date, point, chosenProductToQuantity);
        } catch (OrderValidationException e) {
            e.printStackTrace();
        }
    }

    public void setOnOrderPlaced(TriConsumer<Date, Point, List<Pair<Integer, Double>>> onOrderPlaced) {
        this.onOrderPlaced = onOrderPlaced;
    }

    @Override
    public Node getContent() {
        return this.content;
    }
}
