package entity;

import java.util.Map;

public class Stock {

    private Map<Integer, StoreProduct> soldProducts;

    public boolean doesProductExist(Product product) {
        return this.soldProducts.containsKey(product.getId());
    }

    public Map<Integer, StoreProduct> getSoldProducts() {
        return soldProducts;
    }

    public Stock(Map<Integer, StoreProduct> soldProducts) {
        this.soldProducts = soldProducts;
    }

    public Map<Integer, StoreProduct> getSoldProduts() {
        return this.soldProducts;
    }
}
