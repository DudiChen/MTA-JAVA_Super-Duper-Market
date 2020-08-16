package entities;

import java.util.Map;

public class OLD_Store {
    private Coordinate coordinate;
    private Map<OLD_Product, Integer> productToPrice;
    private int ppk;
//    private List<Discount> discounts;
    private OLD_ID OLD_Id;

    public OLD_Store(Coordinate coordinate, Map<OLD_Product, Integer> productToPrice, int ppk, OLD_ID OLD_Id) {
        this.coordinate = coordinate;
        this.productToPrice = productToPrice;
        this.ppk = ppk;
        this.OLD_Id = OLD_Id;
    }


    public OLD_ID getOLD_Id() {
        return OLD_Id;
    }

    public Map<OLD_Product, Integer> getProductToPrice() {
        return productToPrice;
    }
}
