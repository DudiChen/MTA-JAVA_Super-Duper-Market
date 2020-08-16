package adapters;

import com.sdp.model.*;
import com.sdp.resource.jaxb.SDMStore;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.*;

public class StoreAdapter extends XmlAdapter<SDMStore, Store> {

    private Map<Product, Integer> productToPrice;
    @XmlJavaTypeAdapter(CoordinateAdapter.class)
    private Coordinate coordinate;

    @XmlJavaTypeAdapter(ProductToPriceAdapter.class)
    public Map<Product, Integer> getProductToPrice() {
        return productToPrice;
    }

    @Override
    public Store unmarshal(SDMStore store) {
            return new Store(
                    this.coordinate,
                    this.productToPrice,
                    store.getDeliveryPpk(),
                    store.getId()
            );
    }

    @Override
    public SDMStore marshal(Store v) throws Exception {
        return null;
    }
}
