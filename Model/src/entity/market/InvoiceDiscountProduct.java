package entity.market;

import entity.Discount;

import java.io.Serializable;

public class InvoiceDiscountProduct implements Serializable {
    private int productId;
    private String name;
    private double additionalCost;
    private double quantity;
    private String discountName;

    public InvoiceDiscountProduct(int productId, String name, double additionalCost, double quantity, String discountName) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.additionalCost = additionalCost;
        this.discountName = discountName;
    }

    public InvoiceDiscountProduct(Discount.Offer acceptedOffer, String productName) {
        this.productId = acceptedOffer.getProductId();
        this.name = productName;
        this.quantity = acceptedOffer.getQuantity();
        this.additionalCost = acceptedOffer.getForAdditional();
        this.discountName = acceptedOffer.getRelatedDiscountName();
    }

    public String getName() {
        return name;
    }

    public double getAdditionalCost() {
        return this.additionalCost;
    }

    public double getQuantity() {
        return quantity;
    }

    public int getProductId() {
        return productId;
    }

    public String getDiscountName() {
        return discountName;
    }
}
