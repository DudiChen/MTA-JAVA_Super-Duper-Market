package view.menu;

import controller.Controller;
import entity.Discount;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import view.menu.item.DiscountContent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

public class DiscountsMenu implements Navigatable, Initializable {

    private final List<Discount> discounts;
    private final Parent content;
    private final Controller controller;
    @FXML
    private ListView<Discount> discountsContentsList;
    private BiConsumer<Discount, List<Discount.Offer>> onDiscountChoice;

    public DiscountsMenu(List<Discount> discounts, Controller controller, BiConsumer<Discount, List<Discount.Offer>> onDiscountChoice) {
        this.controller = controller;
        this.onDiscountChoice = onDiscountChoice;
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
        discountsContentsList.setCellFactory(param  -> new DiscountContent(controller, this.onDiscountChoice));
    }
}
