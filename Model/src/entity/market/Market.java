package entity.market;

import entity.Order;
import entity.Product;
import entity.Store;

import java.util.*;
import java.util.stream.Collectors;

// TODO : handle errors with proper exceptions
public class Market {

    private Map<Integer, Store> idToStore;
    private Map<Integer, Product> idToProduct;
    private Map<Integer, OrderInvoice> idToOrderInvoice;
    public Market() {

    }

    public Market(Map<Integer, Store> idToStore, Map<Integer, Product> idToProduct) {
        this.idToStore = idToStore;
        this.idToProduct = idToProduct;
    }

    public void addStore(Store store) {
        this.idToStore.put(store.getId(), store);
    }

    public void addProduct(Product product) {
        this.idToProduct.put(product.getId(), product);
    }

    public List<Store> getAllStores() {
        return Collections.unmodifiableList(new ArrayList<>(idToStore.values()));
    }

    public List<Product> getAllProducts() {
        return Collections.unmodifiableList(new ArrayList<>(idToProduct.values()));
    }

    public Store getStoreById(int id) {
        return idToStore.get(id);
    }

    public Product getProductById(int id) {
        return this.idToProduct.get(id);
    }

    public int receiveOrder(Order order, int storeId) {
        List<InvoiceProduct> invoiceProducts = order.getProductIdsToQuantity().stream()
                .map(pair -> new InvoiceProduct(
                        order.getId(),
                        this.idToProduct.get(pair.getKey()).getName(),
                        this.idToProduct.get(pair.getKey()).getPurchaseMethod().getName(),
                        this.idToStore.get(storeId).getPriceOfProduct(pair.getKey()),
                        pair.getKey(),
                        this.idToStore.get(storeId).getProductPriceWithQuantity(pair.getKey(), pair.getValue())
                        )
                ).collect(Collectors.toList());
        double shipmentCost = this.idToStore.get(storeId).getShipmentCost(order.getDestination());
        double totalPrice = invoiceProducts.stream()
                .map(InvoiceProduct::getPrice)
                .reduce((double) 0, Double::sum);

        this.idToOrderInvoice.put(order.getId(),
                new OrderInvoice(
                        order.getId(),
                        invoiceProducts,
                        shipmentCost + totalPrice,
                        order.getDeliveryDate()
                )
        );
        return order.getId();
    }

    public void approveOrder(int orderReceiptId) {
        this.idToOrderInvoice.get(orderReceiptId).setStatus(OrderInvoice.OrderStatus.ACCEPTED);
    }

    public OrderInvoice getOrderInvoice(int orderInvoiceId) {
        return this.idToOrderInvoice.get(orderInvoiceId);
    }

    public void cancelOrder(int orderInvoiceId) {
        this.idToOrderInvoice.get(orderInvoiceId).setStatus(OrderInvoice.OrderStatus.CANCELED);
    }

    public List<OrderInvoice> getOrdersHistory() {
        return Collections.unmodifiableList(new ArrayList<>(this.idToOrderInvoice.values()));
    }
}
