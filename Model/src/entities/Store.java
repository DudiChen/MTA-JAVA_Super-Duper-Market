package entities;

import java.util.Map;

public class Store {
    private Coordinate coordinate;
    private Map<Integer, StoreProduct> idToProductMap;
    private int ppk;
    //    private List<Discount> discounts;
    private int id;

    public Store(Coordinate coordinate, Map<Integer, StoreProduct> idToProductMap, int ppk, int id) {
        this.coordinate = coordinate;
        this.idToProductMap = idToProductMap;
        this.ppk = ppk;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Map<Integer, StoreProduct> getProductToPrice() {
        return idToProductMap;
    }
}
