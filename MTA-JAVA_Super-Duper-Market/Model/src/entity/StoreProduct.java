package entity;

public class StoreProduct {
    public Product getProduct() {
        return product;
    }

    private final Product product;
    private double price;

    public StoreProduct(Product product, double price) {
        this.product = product;
        this.price = price;
    }

    public int getId() {
        return this.product.getId();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {

        this.price = price;
    }

    public String getName()
    {
        return product.getName();
    }
}

