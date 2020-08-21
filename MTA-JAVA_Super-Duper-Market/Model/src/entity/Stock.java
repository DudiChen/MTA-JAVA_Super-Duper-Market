package entity;

import java.util.Map;

public class Stock {

    private Map<Integer, StoreProduct> soldProduts;

    public boolean doesProductExist(Product product) {
        return this.soldProduts.containsKey(product.getId());
    }

    public Map<Integer, StoreProduct> getSoldProduts() {
        return soldProduts;
    }

    public Stock(Map<Integer, StoreProduct> soldProduts) {
        this.soldProduts = soldProduts;
    }
}
