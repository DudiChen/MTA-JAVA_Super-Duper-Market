package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Market {

    private Map<Integer, Store> idToStore;
    private Map<Integer, Product> idToProduct;

    public Market() {

    }

    public Market(Map<Integer, Store> idToStore, Map<Integer, Product> idToProduct) {
        this.idToStore = idToStore;
        this.idToProduct = idToProduct;
    }


    public void addStore(Store store)
    {

    }

    // TODO: Consider how to protect the list of Store (make final)
    public List<Store> getAllStores() {
        return new ArrayList<>(idToStore.values());
    }
}
