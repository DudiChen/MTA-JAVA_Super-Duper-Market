package entity.market;

import java.io.Serializable;

public class InvoiceProduct implements Serializable {
    private int id;
    private String name;
    private String purchaseMethod;
    private double price;
    private double quantity;
    private double totalPrice;
    private double shipmentCost;

    public InvoiceProduct(int id, String name, String purchaseMethod, double price, double quantity, double totalPrice, double shipmentCost) {
        this.id = id;
        this.name = name;
        this.purchaseMethod = purchaseMethod;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.shipmentCost = shipmentCost;
    }

    public String getName() {
        return name;
    }

    public String getPurchaseMethod() {
        return purchaseMethod;
    }

    public double getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getShipmentCost() {
        return this.shipmentCost;
    }

    public int getId() {
        return id;
    }
}
