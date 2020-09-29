package view.menu;

import controller.Controller;
import entity.Discount;
import entity.Product;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import view.menu.item.DiscountContent;
import view.menu.item.StoreContent;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DiscountsMenu implements Navigatable, Initializable {

    private final List<Discount> discounts;
    private final Parent content;
    private final Controller controller;
    @FXML
    private ListView<Discount> discountsContentsList;

    public DiscountsMenu(List<Discount> discounts, Controller controller) {
        this.controller = controller;
        this.discounts = discounts;
        this.content = loadFXML("discountsMenu");
    }

    // TODO: move to utils
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
    public Node getContent() {
        return this.content;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        discountsContentsList.getItems().addAll(discounts);
//
//        thenYouGetColumn.setCellValueFactory(param -> {
//            ObservableList<VBox> productsOfDiscount = param.getValue().getOffers().stream()
//                    .map(offer -> {
//                        Product product = controller.getProductById(offer.getProductId());
//                        StringBuilder s = new StringBuilder(offer.getQuantity() + " " + product.getName());
//                        if(offer.getForAdditional() > 0)
//                            s.append(" for additional: ").append(offer.getForAdditional());
//                        VBox content = new VBox();
//                        HBox hBox = new HBox();
//                        hBox.getChildren().add(new Label(s.toString()));
//                        hBox.getChildren().add(new Button("Get Discount"));
//                        content.getChildren().add(hBox);
//                        return content;
//                    })
//                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        discountsContentsList.setCellFactory(param  -> new DiscountContent(controller));


    }
}
