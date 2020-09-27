package entity.market;

import entity.Discount;

import java.io.Serializable;

public class InvoiceDiscountProduct implements Serializable {
    private int id;
    private String name;
    private double additionalCost;
    private double quantity;
    private String discountName;

    // TODO: Check if there is any unused ctors and remove
    public InvoiceDiscountProduct(int id, String name, double additionalCost, double quantity, String discountName) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.additionalCost = additionalCost;
        this.discountName = discountName;
    }

    public InvoiceDiscountProduct(Discount.Offer acceptedOffer, String productName) {
        this.id = acceptedOffer.getProductId();
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

    public int getId() {
        return id;
    }

    public String getDiscountName() {
        return discountName;
    }
}
