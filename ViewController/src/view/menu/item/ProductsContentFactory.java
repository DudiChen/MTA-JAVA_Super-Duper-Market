package view.menu.item;

import entity.Product;
import entity.Store;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ProductsContentFactory {
    private Optional<Consumer<Integer>> onProductDelete;
    private Optional<BiConsumer<Integer, Double>> onProductPriceChange;
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
            this.onHover.ifPresent(((StoreProductContent) item)::setOnHover);
            this.onUnHover.ifPresent(((StoreProductContent) item)::setOnUnHover);
            if(this.onProductDelete.isPresent()) {
                ((StoreProductContent)item).setOnDelete(this.onProductDelete.get());
            }
            if(this.onProductPriceChange.isPresent()) {
                ((StoreProductContent)item).setOnPriceChange(this.onProductPriceChange.get());
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

    public void setOnDelete(Consumer<Integer> onProductDelete) {
        this.onProductDelete = Optional.of(onProductDelete);
    }

    public void setOnPriceChange(BiConsumer<Integer, Double> onProductPriceChange) {
        this.onProductPriceChange = Optional.of(onProductPriceChange);
    }
}
