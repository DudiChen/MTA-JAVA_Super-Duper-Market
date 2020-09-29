package view.menu.item;


import controller.Controller;
import entity.Discount;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.util.function.BiConsumer;

class OfferContent extends ListCell<Discount.Offer> {
    private final Discount discount;
    private final Controller controller;
    private BiConsumer<Discount, Discount.Offer> onDiscountChoice;

    @FXML
    private Label offerLabel;
    @FXML
    private Button offerButton;
    public OfferContent(Discount discount, Controller controller, BiConsumer<Discount, Discount.Offer> onDiscountChoice) {
        this.controller = controller;
        this.onDiscountChoice = onDiscountChoice;
        this.discount = discount;
        loadFXML();
    }

    // TODO: move to utils
    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../sample/offer.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Discount.Offer offer, boolean empty) {
        super.updateItem(offer, empty);
        if (empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            switch (discount.getOperator()) {
                case ONE_OF: case IRRELEVANT:
                    this.offerLabel.setText(getProductDescription(offer, true));
                    this.offerButton.setVisible(true);
                    this.offerButton.setOnAction(e -> this.onDiscountChoice.accept(discount, offer));
                    break;
                case ALL_OR_NOTHING:
                    this.offerLabel.setText(getProductDescription(offer, false));
                    break;
            }
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
    
    private String getProductDescription(Discount.Offer offer, boolean forAdditionalNeeded) {
        StringBuilder stringBuilder = new StringBuilder(offer.getQuantity() + " " + controller.getProductById(offer.getProductId()).getName());
        if(offer.getForAdditional() > 0 && forAdditionalNeeded) {
            stringBuilder.append(" For Additional ").append(offer.getForAdditional());
        }
        return stringBuilder.toString();
    }
}