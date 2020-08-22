package entity;

import java.awt.*;
import builder.Builder;
import builder.StoreBuilder;
import jaxb.generated.SuperDuperMarketDescriptor;

import java.util.Map;

public class Store {
    private Point coordinate;
    private Stock stock;
//    private Map<Integer, StoreProduct> idToProductMap;
    private int ppk;
    //    private List<Discount> discounts;
    private int id;

    public Store(Point point, Stock stock, int ppk, int id) {
        this.coordinate = point;
        this.stock = stock;
        this.ppk = ppk;
        this.id = id;
    }
    public Store(SuperDuperMarketDescriptor sdmDescriptor)
    {

    }

//    public static class StoreBuilder implements Builder<Store> {
//
//        public Store build() {
//
//        }
//    }

    public int getId() {
        return id;
    }

//    public Map<Integer, StoreProduct> getIdToProduct() {
//        return idToProductMap;
//    }



}