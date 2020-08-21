package builder;

import entity.Store;
import jaxb.generated.SDMStore;

import java.awt.*;

public class StoreBuilder implements Builder<SDMStore, Store> {

    @Override
    public Store build(SDMStore source) {
        return new Store(
                new PointBuilder().build(source.getLocation()),
                new StockBuilder().build(source.getSDMPrices()),
                source.getDeliveryPpk(),
                source.getId()
        );
    }
}
