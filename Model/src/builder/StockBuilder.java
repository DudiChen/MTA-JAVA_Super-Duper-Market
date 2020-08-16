package builder;

import entity.Stock;
import jaxb.generated.SDMPrices;

public class StockBuilder implements Builder<SDMPrices, Stock> {

    @Override
    public Stock build(SDMPrices source) {

        return new Stock();
    }
}
