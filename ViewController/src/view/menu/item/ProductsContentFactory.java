package view.menu.item;

import entity.Store;

import java.util.List;

public class ProductsContentFactory {
    private Store store;
    private List<Store> allStores;

    public ProductsContentFactory(Store store) {
        this.store = store;
    }

    public ProductsContentFactory(List<Store> allStores) {
        this.allStores = allStores;
    }

    public AbstractProductContent getItem() {
        AbstractProductContent item = null;
        if(this.store != null) {
            item = new StoreProductContent(store);
        }
        else if(this.allStores != null) {
            ProductContent productContent = new ProductContent(allStores);
            item = productContent;
        }
        return item;
    }
}
