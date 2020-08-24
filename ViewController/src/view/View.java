package view;


import com.sun.tools.javac.util.Pair;
import command.Command;
import entity.Product;
import entity.Store;
import entity.market.OrderInvoice;
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
    public Consumer<Integer> onOrderAccepted;
    public Consumer<Integer> onOrderCanceled;
    public abstract void summarizeOrder(OrderInvoice orderInvoice);

    public abstract void displayError(String message);

    public abstract void fileLoadedSuccessfully();

    public abstract void showOrdersHistory(List<OrderInvoice> ordersHistory);

    @FunctionalInterface
    public interface TriConsumer<T, U, V> {
        void apply(T t, U u, V v) throws OrderValidationException;
    }
}
