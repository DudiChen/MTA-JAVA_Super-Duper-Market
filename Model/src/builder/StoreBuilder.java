package builder;

import entity.Product;
import entity.Store;
import jaxb.generated.SDMStore;

import javax.xml.bind.ValidationException;
import java.awt.*;
import java.util.Map;

public class StoreBuilder implements Builder<SDMStore, Store> {

    Map<Integer, Product> idToProduct;

    public StoreBuilder(Map<Integer, Product> idToProduct) {
        this.idToProduct = idToProduct;
    }

    @Override
    public Store build(SDMStore source) {
        return new Store(
                new PointBuilder().build(source.getLocation()),
                new StockBuilder(this.idToProduct).build(source.getSDMPrices()),
                source.getDeliveryPpk(),
                source.getId()
        );
    }
}
