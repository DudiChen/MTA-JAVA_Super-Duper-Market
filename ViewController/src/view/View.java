package view;


import controller.Controller;
import entity.Product;
import entity.Store;
import entity.market.OrderInvoice;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Pair;
import view.menu.item.MapElement;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public abstract class View {

    public Consumer<Integer> onStoreIdChoice;
    public TriConsumer<Date, Point, List<Pair<Integer, Double>>> onOrderPlaced;
    public Consumer<Integer> onOrderAccepted;
    public Consumer<Integer> onOrderCanceled;
    public TriConsumer<Date, Point, List<Pair<Integer, Double>>> onDynamicOrder;

    public abstract void displayStores(List<Store> allStores);

    public abstract void displayProducts(List<Product> allProducts, List<Store> allStores);

    public abstract void summarizeOrder(OrderInvoice orderInvoice);

    public abstract void displayError(String message);

    public abstract void fileLoadedSuccessfully();

    public abstract void showOrdersHistory(List<OrderInvoice> ordersHistory);

    public abstract String promptUserFilePath();

    public abstract void showMainMenu();

    public abstract void setController(Controller controller);

    public abstract void displayStoresList(List<Store> stores);

    public abstract void showMap(List<MapElement> mapElements);

    public abstract void displayProductsForStore(List<Product> products, Store stores);

    public abstract StringProperty xmlProgressStateProperty();

    public abstract DoubleProperty xmlProgressBarProperty();
}
