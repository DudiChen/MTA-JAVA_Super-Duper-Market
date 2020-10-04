package view.menu.item;

import entity.Store;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.util.function.Consumer;

public class StoreContent extends ListCell<Store> {

    private Consumer<Integer> onStoreIdChoice;

    @FXML
    private Label nameLabel;

    @FXML
    private Label idLabel;

    @FXML
    private Label ppkLabel;

    @FXML
    private Label shipmentIncomesLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Button orderButton;

    public StoreContent(Consumer<Integer> onStoreIdChoice) {
        this.onStoreIdChoice = onStoreIdChoice;
        loadFXML();
    }

    // TODO: move to utils
    private void loadFXML() {
        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("../sample/store.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/menu/sample/store.fxml"));
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
            idLabel.setText("ID: " + store.getId());
            ppkLabel.setText("Price Per Kilometer: " + store.getPpk());
            shipmentIncomesLabel.setText("Total Shipments Incomes: " + store.getTotalShipmentIncome());
            orderButton.setOnAction(event -> this.onStoreIdChoice.accept(store.getId()));
            locationLabel.setText(locationLabel.getText() + " X: " + store.getLocation().getX() + " Y: " + store.getLocation().getY());
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
