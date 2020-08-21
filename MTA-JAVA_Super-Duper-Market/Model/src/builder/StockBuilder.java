package builder;

import entity.Stock;
import entity.StoreProduct;
import jaxb.generated.SDMPrices;

import java.util.Map;

public class StockBuilder implements Builder<SDMPrices, Stock> {

    @Override
    public Stock build(SDMPrices source) {
        Map<Integer, StoreProduct> idToStoreProduct = getIdToStoreProduct(source);
        return new Stock();
    }

    private Map<Integer, StoreProduct> getIdToStoreProduct(SDMPrices source) {
        source.getSDMSell().get(1).getItemId()
    }
}
