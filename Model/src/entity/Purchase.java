package entity;

import javafx.util.Pair;

import java.awt.*;
import java.util.Date;
import java.util.List;

public class Purchase {
    List<Pair<Integer, Integer>> productIdsToQuantity;
    Point destination;
    Date deliveryDate;

    public Purchase(List<Pair<Integer, Integer>> productIdsToQuantity, Point destination, Date deliveryDate) {
        this.productIdsToQuantity = productIdsToQuantity;
        this.destination = destination;
        this.deliveryDate = deliveryDate;
    }
}
