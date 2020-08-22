package builder;

import entity.Product;
import entity.StoreProduct;
import jaxb.generated.SDMItem;
import jaxb.generated.SDMSell;
import jaxb.generated.SDMStore;

import java.awt.*;
import java.util.Map;

public class StoreProductBuilder implements Builder<SDMSell, StoreProduct> {

    Map<Integer, Product> idToProduct;

    public StoreProductBuilder(Map<Integer, Product> idToProduct) {
        this.idToProduct = idToProduct;
    }

    @Override
    public StoreProduct build(SDMSell source) {
        return new StoreProduct(idToProduct.get(source.getItemId()), source.getPrice());
    }
}

