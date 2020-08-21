package entity;

import java.util.Map;

public class Market {

    private Map<Integer, Store> idToStore;
    private Map<Integer, Product> idToProduct;

    public Market(Map<Integer, Store> idToStore, Map<Integer, Product> idToProduct) {
        this.idToStore = idToStore;
        this.idToProduct = idToProduct;
    }


    public void addStore(Store store)
    {

    }
}
