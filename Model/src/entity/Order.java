package entity;

import javafx.util.Pair;

import java.awt.*;
import java.util.Date;
import java.util.List;

public class Order {
    List<Pair<Integer, Integer>> productIdsToQuantity;
    Point destination;
    Date deliveryDate;

    public Order(List<Pair<Integer, Integer>> productIdsToQuantity, Point destination, Date deliveryDate) {
        this.productIdsToQuantity = productIdsToQuantity;
        this.destination = destination;
        this.deliveryDate = deliveryDate;
    }

    public List<Pair<Integer, Integer>> getProductIdsToQuantity() {
        return productIdsToQuantity;
    }

    public Point getDestination() {
        return destination;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }
}
