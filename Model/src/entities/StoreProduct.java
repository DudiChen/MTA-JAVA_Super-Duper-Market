package entities;

public class StoreProduct {
    private final Product product;
    private double price;

    private StoreProduct(Product product)
    {
        this.product = product;
    }

    private StoreProduct(Product product, double price) {
        this.product = product;
        this.price = price;
    }

//    public StoreProduct createInstance()
//    {
//    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {

        this.price = price;
    }

    public int getId()
    {
        return product.getId();
    }

    public String getName()
    {
        return product.getName();
    }
}

