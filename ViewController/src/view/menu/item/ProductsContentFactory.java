package view.menu.item;

import entity.Product;
import entity.Store;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ProductsContentFactory {
    private Optional<BiConsumer<Product, Point>> onUnHover;
    private Optional<BiConsumer<Product, Point>> onHover;
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
            if(this.onHover.isPresent()) {
                ((StoreProductContent)item).setOnHover(this.onHover.get());
            }
            if(this.onUnHover.isPresent()) {
                ((StoreProductContent)item).setOnUnHover(this.onUnHover.get());
            }
        }
        else if(this.allStores != null) {
            item = new ProductContent(allStores);
        }
        return item;
    }

    public void setOnUnHover(BiConsumer<Product, Point> onUnHover) {
        this.onUnHover = Optional.of(onUnHover);
    }

    public void setOnHover(BiConsumer<Product, Point> onHover) {
        this.onHover = Optional.of(onHover);
    }
}
