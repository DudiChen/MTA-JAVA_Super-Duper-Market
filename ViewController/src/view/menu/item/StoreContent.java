package view.menu.item;

import controller.Controller;
import entity.Store;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.util.function.Consumer;

public class StoreContent extends ListCell<Store> {

    @FXML
    private Label nameLabel;

    @FXML
    private Label idLabel;

    @FXML
    private Label ppkLabel;

    @FXML
    private Label shipmentIncomesLabel;

    @FXML
    private Button orderButton;
    private Consumer<Integer> onStoreIdChoice;


    public StoreContent(Consumer<Integer> onStoreIdChoice) {
        this.onStoreIdChoice = onStoreIdChoice;
        loadFXML();
    }

    // TODO: move to utils
    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../sample/store.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Store store, boolean empty) {
        super.updateItem(store, empty);

        if(empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
            nameLabel.setText(store.getName());
            idLabel.setText(idLabel.getText() + store.getId());
            ppkLabel.setText(ppkLabel.getText() + store.getPpk());
            shipmentIncomesLabel.setText(shipmentIncomesLabel.getText() + store.getTotalShipmentIncome());
            orderButton.setOnAction(event -> this.onStoreIdChoice.accept(store.getId()));
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
