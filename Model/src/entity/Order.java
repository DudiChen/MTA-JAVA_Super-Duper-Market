package entity;

import entity.market.MarketUtils;
import javafx.util.Pair;

import java.awt.*;
import java.util.Date;
import java.util.List;

public class Order {
    List<Pair<Integer, Double>> productIdsToQuantity;
    Point destination;
    Date deliveryDate;
    int storeId;
    int id;

    public Order(List<Pair<Integer, Double>> productIdsToQuantity, Point destination, Date deliveryDate, int storeId) {
        this.productIdsToQuantity = productIdsToQuantity;
        this.destination = destination;
        this.deliveryDate = deliveryDate;
        this.id = this.hashCode();
        this.id = MarketUtils.generateIdForOrder();
        this.storeId = storeId;
    }

    public List<Pair<Integer, Double>> getProductIdsToQuantity() {
        return productIdsToQuantity;
    }

    public Point getDestination() {
        return destination;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public int getId() {
        return id;
    }

    public int getStoreId() {
        return storeId;
    }
}
