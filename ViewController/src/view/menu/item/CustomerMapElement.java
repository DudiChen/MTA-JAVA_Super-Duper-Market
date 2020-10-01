package view.menu.item;

import entity.Customer;
import entity.Store;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.stage.Popup;

import java.awt.*;

public class CustomerMapElement implements MapElement {
    private final Customer customer;
    private Popup parent;

    public CustomerMapElement(Customer customer) {
        this.customer = customer;
    }

    @Override
    public Point getPoint() {
        return this.customer.getLocation();
    }

    @Override
    public String getIconName() {
        return "customer.png";
    }

    @Override
    public Popup getParent() {
        return this.parent;
    }

    @Override
    public void setParent(Popup parent) {

    }

    @Override
    public Node getContent() {
        ListView<Customer> customerContainer = new ListView<>();
        customerContainer.setCellFactory(param -> new CustomerContent());
        customerContainer.getItems().add(this.customer);
        return customerContainer;
    }
}
