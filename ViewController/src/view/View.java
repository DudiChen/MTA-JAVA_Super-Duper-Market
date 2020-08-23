package view;


import com.sun.tools.javac.util.Pair;
import entity.Order;
import entity.Product;
import entity.Store;
import exception.OrderValidationException;

import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public abstract class View {
    public abstract void displayStores(List<Store> allStores);
    public abstract void displayProducts(List<Product> allProducts);
    public Consumer<Integer> onStoreIdChoice;
    public TriConsumer<Date, Point, List<Pair<Integer, Integer>>> onOrderPlaced;

    @FunctionalInterface
    public interface TriConsumer<T, U, V> {
        void apply(T t, U u, V v) throws OrderValidationException;
    }
}
