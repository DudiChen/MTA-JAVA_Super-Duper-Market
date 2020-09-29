package entity;

import javafx.util.Pair;

import java.util.List;

public class Discount {
    public String getName() {
        return name;
    }

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

    public double getQuantity() {
        return this.productIdQuantityPair.getValue();
    }

    public int getProductId() { return this.productIdQuantityPair.getKey(); }

    public boolean isDiscountMatch(int productId, double quantity) {
        return productId == this.productIdQuantityPair.getKey() && quantity <= this.productIdQuantityPair.getValue();
    }

    public int discountMatchInstances(int productId, double quantity) {
        int result = 0;
        if (productId == this.productIdQuantityPair.getKey() && quantity <= this.productIdQuantityPair.getValue()) {
            result =  (int)(this.productIdQuantityPair.getValue() / quantity);
        }
        return result;
    }

    public DiscountOperator getOperator() {
        return operator;
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
        String relatedDiscountName;
        private int productId;
        private double quantity;
        private int forAdditional;

        public Offer(String discountName, int productId, double quantity, int forAdditional) {
            this.relatedDiscountName = discountName;
            this.productId = productId;
            this.quantity = quantity;
            this.forAdditional = forAdditional;
        }

        public String getRelatedDiscountName() {
            return this.relatedDiscountName;
        }

        public int getProductId() {
            return productId;
        }

        public double getQuantity() {
            return quantity;
        }

        public int getForAdditional() {
            return forAdditional;
        }
    }
}
