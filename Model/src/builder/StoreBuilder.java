package builder;

import entity.Discount;
import entity.Product;
import entity.Store;
import jaxb.generated.SDMDiscount;
import jaxb.generated.SDMStore;

import javax.xml.bind.ValidationException;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
                source.getId(),
                source.getName(),
                getDiscountMap(source)
        );
    }

    private Map<Integer, Discount> getDiscountMap(SDMStore source) {
        Map<Integer, Discount> result;
        if  (source.getSDMDiscounts() == null || source.getSDMDiscounts().getSDMDiscount().isEmpty()) {
            result = new HashMap<>();
        }
        else {
            result = source.getSDMDiscounts().getSDMDiscount()
                    .stream()
                    .map(sdmDiscount -> new DiscountBuilder().build(sdmDiscount))
                    .collect(Collectors.toMap(Discount::getProductId,discount -> discount));
        }

        return result;
    }
}
