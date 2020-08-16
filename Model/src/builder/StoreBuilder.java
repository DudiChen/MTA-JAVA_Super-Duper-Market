package builder;

import entity.Store;
import jaxb.generated.SDMStore;

import java.awt.*;

public class StoreBuilder implements Builder<SDMStore, Store> {

    @Override
    public Store build(SDMStore source) {
        return new Store(
                new PointBuilder(source.getLocation()).build(),
                new StockBuilder(source.getSDMPrices()).build(),
                source.getDeliveryPpk(),
                source.getId()
        );
    }
}
