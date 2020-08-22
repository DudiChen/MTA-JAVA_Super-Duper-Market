package builder;

import entity.Product;
import entity.Stock;
import entity.StoreProduct;
import jaxb.generated.SDMItem;
import jaxb.generated.SDMPrices;
import jaxb.generated.SDMSell;

import java.util.Map;
import java.util.stream.Collectors;

public class StockBuilder implements Builder<SDMPrices, Stock> {

    Map<Integer, Product> idToProduct;

    public StockBuilder(Map<Integer, Product> idToProduct){
        this.idToProduct = idToProduct;
    }

    @Override
    public Stock build(SDMPrices source) {
        return new Stock(source.getSDMSell().stream()
                .collect(Collectors.toMap(SDMSell::getItemId, sell -> new StoreProductBuilder(idToProduct).build(sell))));
    }
}
