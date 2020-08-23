package entity;

import java.util.*;

public class Market {

    private Map<Integer, Store> idToStore;
    private Map<Integer, Product> idToProduct;

    public Market() {

    }

    public Market(Map<Integer, Store> idToStore, Map<Integer, Product> idToProduct) {
        this.idToStore = idToStore;
        this.idToProduct = idToProduct;
    }

    public void addStore(Store store) {
        this.idToStore.put(store.getId(), store);
    }

    public void addProduct(Product product) {
        this.idToProduct.put(product.getId(), product);
    }

    public List<Store> getAllStores() {
        return Collections.unmodifiableList(new ArrayList<>(idToStore.values()));
    }

    public List<Product> getAllProducts() {
        return Collections.unmodifiableList(new ArrayList<>(idToProduct.values()));
    }

    public int getIdForStore() {
        Random rand = new Random();
        int result = rand.nextInt() * Integer.MAX_VALUE;
        while(!idToStore.containsKey(result)) {
            result = rand.nextInt() * Integer.MAX_VALUE;
        }
        return result;
    }

    public int getIdForProduct() {
        Random rand = new Random();
        int result = rand.nextInt() * Integer.MAX_VALUE;
        while(!idToProduct.containsKey(result)) {
            result = rand.nextInt() * Integer.MAX_VALUE;
        }
        return result;
    }
}
