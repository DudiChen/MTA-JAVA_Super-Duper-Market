package view.menu.item;

import controller.Controller;
import entity.Discount;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;

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
    public DiscountContent(Controller controller) {
        this.controller = controller;
        loadFXML();
    }

    // TODO: move to utils
    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../sample/discountContent.fxml"));
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
            this.productsInDiscountList.setCellFactory(param -> new OfferContent(discount, controller, this::onDiscountChoice));
            if(discount.getOperator().equals(Discount.DiscountOperator.ALL_OR_NOTHING)) {
                discountButton.setVisible(true);
                discountButton.setText("Get For Additional " + discount.getOffers().get(0).getForAdditional());
                discountButton.setOnAction(e -> onDiscountChoice(discount, null));
            }
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    private void onDiscountChoice(Discount discount, Discount.Offer offer) {
        System.out.println(offer);
    }
}
