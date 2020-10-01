package view.menu.item;

import entity.Customer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import java.io.IOException;

public class CustomerContent extends ListCell<Customer> {

    @FXML
    private Label nameLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label totalOrdersLabel;
    @FXML
    private Label ordersAveragePriceLabel;
    @FXML
    private Label averageShipmentPriceLabel;

    public CustomerContent() {
        loadFXML();
    }

    // TODO: move to utils
    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../sample/customer.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Customer customer, boolean empty) {
        super.updateItem(customer, empty);

        if(empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
            this.nameLabel.setText(this.nameLabel.getText() + customer.getName());
            this.idLabel.setText(this.idLabel.getText() + customer.getId());
            this.locationLabel.setText(locationLabel.getText() + " X: " + customer.getLocation().getX() + " Y: " + customer.getLocation().getY());
            this.totalOrdersLabel.setText(this.totalOrdersLabel.getText() + customer.getTotalOrders());
            this.averageShipmentPriceLabel.setText(this.averageShipmentPriceLabel.getText() + customer.getAverageShipmentCost());
            this.ordersAveragePriceLabel.setText(this.ordersAveragePriceLabel.getText() + customer.getAverageOrderCost());
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
