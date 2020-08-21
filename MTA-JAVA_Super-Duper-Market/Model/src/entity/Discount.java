package entity;

import javafx.util.Pair;

import java.util.List;

public class Discount {
    private String name;
    private Pair<Integer, Integer> productIDYouBuyQuantityOf;
    private DiscountOperator operator;
    private List<Offer> thenYouGet;

    private class Offer {
        private int productId;
        private int quantity;
        private int forAdditional;
    }
}
