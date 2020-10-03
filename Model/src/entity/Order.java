package entity;

import entity.market.MarketUtils;
import javafx.util.Pair;

import java.awt.*;
import java.util.Date;
import java.util.List;

public class Order {
    List<Pair<Integer, Double>> productIdsToQuantity;
    List<Discount.Offer> offersTaken;
    Point destination;
    Date deliveryDate;
    int storeId;
    int customerId;
    int id;

    public Order(int customerId, List<Pair<Integer, Double>> productIdsToQuantity, List<Discount.Offer> offersTaken, Point destination, Date deliveryDate, int storeId) {
        this.customerId =  customerId;
        this.productIdsToQuantity = productIdsToQuantity;
        this.offersTaken = offersTaken;
        this.destination = destination;
        this.deliveryDate = deliveryDate;
        this.id = this.hashCode();
        this.id = MarketUtils.generateIdForOrder();
        this.storeId = storeId;
    }
    // TODO: use customerId for InvoiceOrder Conversion and display
    public int getCustomerId() {
        return customerId;
    }

    public List<Pair<Integer, Double>> getProductIdsToQuantity() {
        return productIdsToQuantity;
    }
    // TODO: use getOffersTaken for InvoiceOrder Conversion and display
    public List<Discount.Offer> getOffersTaken() {
        return offersTaken;
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
