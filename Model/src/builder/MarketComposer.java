package builder;

import entity.Market;
import entity.Product;
import entity.Store;
import entity.StoreProduct;
import jaxb.generated.SDMItem;
import jaxb.generated.SDMStore;
import jaxb.generated.SuperDuperMarketDescriptor;

import java.util.HashMap;
import java.util.Map;

public class MarketComposer {
    Map<Integer, Product> idToProductMap = new HashMap<>();
    Map<Integer, Store> idToStoreMap = new HashMap<>();

    StoreBuilder storeBuilder;
    StoreProductBuilder storeProductBuilder;

    SuperDuperMarketDescriptor sdmDescriptor;

    public MarketComposer(SuperDuperMarketDescriptor sdmDescriptor) {
        this.sdmDescriptor = sdmDescriptor;
    }

    public Market construct()
    {
        idToProductMap = populateProducts();

        return null;
    }

    // TODO: unimplemented
    private Map<Integer, Product> populateProducts() {
//        for (SDMItem sourceProduct : sdmDescriptor.getSDMItems().getSDMItem()) {
//            idToProductMap.put(sourceProduct.getId(), new ProductBuilder().build(sourceProduct));
//        }
        return null;
    }
}
