package entity.market;

public class InvoiceProduct {
    private int id;
    private String name;
    private String purchaseMethod;
    private double price;
    private int quantity;
    private double totalPrice;

    public InvoiceProduct(int id, String name, String purchaseMethod, double price, int quantity, double totalPrice) {
        this.id = id;
        this.name = name;
        this.purchaseMethod = purchaseMethod;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
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

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
