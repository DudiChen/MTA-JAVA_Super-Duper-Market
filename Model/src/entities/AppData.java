package entities;

import adapters.AppDataAdapter;
import adapters.IdToProductAdapter;
import adapters.IdToStoreAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Map;

@XmlJavaTypeAdapter(AppDataAdapter.class)
public class AppData {

    private static AppData appData;

    private Map<Integer, Product> idToProduct;
    private Map<Integer, Store> idToStore;

    public AppData(Map<Integer, Product> idToProduct, Map<Integer, Store> idToStore) {
        this.idToProduct = idToProduct;
        this.idToStore = idToStore;
    }

    // implement observer design pattern - when state changes - update view of state change
    // expose state to view to ask for

    @XmlJavaTypeAdapter(IdToStoreAdapter.class)
    public Map<Integer, Store> getIdToStore() {
        return idToStore;
    }

    @XmlJavaTypeAdapter(IdToProductAdapter.class)
    public Map<Integer, Product> getIdToProduct() {
        return idToProduct;
    }
}
