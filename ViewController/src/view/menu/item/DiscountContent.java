package view.menu.item;

import controller.Controller;
import entity.Discount;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;

public class DiscountContent extends ListCell<Discount> {

    private final Controller controller;
    @FXML
    private Label nameLabel;
    @FXML
    private Label quantityLabel;
    @FXML
    private ListView<Discount.Offer> productsInDiscountList;
    @FXML
    private Button discountButton;
    private BiConsumer<Discount, List<Discount.Offer>> onDiscountChoice;

    public DiscountContent(Controller controller, BiConsumer<Discount, List<Discount.Offer>> onDiscountChoice) {
        this.controller = controller;
        this.onDiscountChoice = onDiscountChoice;
        loadFXML();
    }

    // TODO: move to utils
    private void loadFXML() {
        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("../sample/discountContent.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/menu/sample/discountContent.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void updateItem(Discount discount, boolean empty) {
        super.updateItem(discount, empty);
        if (empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            this.quantityLabel.setText("If You Buy " + discount.getQuantity() + " Then You Get: ");
            this.productsInDiscountList.getItems().addAll(discount.getOffers());
            this.nameLabel.setText(discount.getName());
            this.productsInDiscountList.setCellFactory(param -> new OfferContent(discount, controller, this.onDiscountChoice));
            if(discount.getOperator().equals(Discount.DiscountOperator.ALL_OR_NOTHING)) {
                discountButton.setVisible(true);
                discountButton.setText("Get For Additional " + discount.getOffers().get(0).getForAdditional());
                discountButton.setOnAction(e -> onDiscountChoice.accept(discount, discount.getOffers()));
            }
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
