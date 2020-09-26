package entity;

import javafx.util.Pair;

import java.util.List;

public class Discount {
    private String name;
    private Pair<Integer, Double> productIdQuantityPair;
    private DiscountOperator operator;
    private List<Offer> offers;

    public Discount(String name, int productId, double quantity, DiscountOperator operator, List<Offer> offers) {
        this.name = name;
        this.productIdQuantityPair = new Pair<>(productId, quantity);
        this.operator = operator;
        this.offers = offers;
    }

    public List<Offer> getOffers() {
        return this.offers;
    }

    public int getProductId() { return this.productIdQuantityPair.getKey(); }

    public boolean isDiscountMatch(int productId, double quantity) {
        return productId == this.productIdQuantityPair.getKey() && quantity == this.productIdQuantityPair.getValue();
    }

    public enum DiscountOperator {
        IRRELEVANT("IRRELEVANT"),
        ONE_OF("ONE-OF"),
        ALL_OR_NOTHING("ALL-OR-NOTHING");

        private final String name;

        private DiscountOperator(String name) {this.name = name;}
        public String getName() {return this.name;}

        @Override
        public String toString() {
            return this.getName();
        }

        public static DiscountOperator getOperatorByString(String operatorString) {
            for (DiscountOperator operator : DiscountOperator.values()) {
                if (operator.getName().equals(operatorString)) return operator;
            }
            return IRRELEVANT;
        }
    }

    public static class Offer {
        private int productId;
        private double quantity;
        private int forAdditional;

        public Offer(int productId, double quantity, int forAdditional) {
            this.productId = productId;
            this.quantity = quantity;
            this.forAdditional = forAdditional;
        }
    }
}
