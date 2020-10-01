package entity.market;

import entity.*;
import exception.MarketIsEmptyException;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

// TODO : handle errors with proper exceptions
public class Market {

    private Map<Integer, Store> idToStore;
    private Map<Integer, Product> idToProduct;
    private Map<Integer, Customer> idToCustomer;
    private Map<Integer, OrderInvoice> idToOrderInvoice;

    public Market() {
        this.idToOrderInvoice = new HashMap<>();
    }

    public Market(Map<Integer, Store> idToStore, Map<Integer, Product> idToProduct, Map<Integer,Customer> idToCustomer) {
        this.idToStore = idToStore;
        this.idToProduct = idToProduct;
        this.idToOrderInvoice = new HashMap<>();
        this.idToCustomer = idToCustomer;
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

    public int receiveOrder(Order order) {
        List<InvoiceProduct> invoiceProducts = order.getProductIdsToQuantity().stream()
                .map(pair -> new InvoiceProduct(
                                pair.getKey(),
                                this.idToProduct.get(pair.getKey()).getName(),
                                this.idToProduct.get(pair.getKey()).getPurchaseMethod().getName(),
                                this.idToStore.get(order.getStoreId()).getPriceOfProduct(pair.getKey()),
                                pair.getValue(),
                                this.idToStore.get(order.getStoreId()).getProductPriceWithQuantity(pair.getKey(), pair.getValue()),
                                this.idToStore.get(order.getStoreId()).getShipmentCost(order.getDestination())
                        )
                ).collect(Collectors.toList());
        double shipmentCost = this.idToStore.get(order.getStoreId()).getShipmentCost(order.getDestination());
        double totalPrice = invoiceProducts.stream()
                .map(InvoiceProduct::getTotalPrice)
                .reduce((double) 0, Double::sum);

        this.idToOrderInvoice.put(order.getId(),
                new OrderInvoice(
                        order.getId(),
                        invoiceProducts,
                        shipmentCost + totalPrice,
                        order.getDeliveryDate(),
                        order.getStoreId(),
                        this.idToStore.get(order.getStoreId()).getShipmentCost(order.getDestination()))
        );
        return order.getId();
    }

    public List<Discount> getStoreDiscountsByProductIdQuantityPairs(int storeId, List<Pair<Integer, Double>> productIdQuantityPairs) {
        return this.idToStore.get(storeId).getMatchingDiscountsByProductIdQuantityPairs(productIdQuantityPairs);
    }

    public void approveOrder(int orderReceiptId) {
        OrderInvoice orderFinalization = this.idToOrderInvoice.get(orderReceiptId);
        Store providingStore = this.idToStore.get(orderFinalization.getStoreId());
        providingStore.addOrder(orderFinalization);
        orderFinalization.getInvoiceProducts().forEach(invoiceProduct -> providingStore.addToTotalShipmentIncome(invoiceProduct.getShipmentCost()));
        this.idToOrderInvoice.get(orderReceiptId).setStatus(OrderInvoice.OrderStatus.ACCEPTED);
    }

    public void addStoreOfferPurchasesToOrderInvoice(int storeId, int orderInvoiceId, List<Discount.Offer> acceptedOffers) {
        this.getOrderInvoice(storeId).setDiscountProducts(
                acceptedOffers.stream()
                .map(offer -> new InvoiceDiscountProduct(offer, this.idToProduct.get(offer.getProductId()).getName()))
                .collect(Collectors.toList())
        );
    }

    public OrderInvoice getOrderInvoice(int orderInvoiceId) {
        return this.idToOrderInvoice.get(orderInvoiceId);
    }

    public void cancelOrder(int orderInvoiceId) {
        this.idToOrderInvoice.get(orderInvoiceId).setStatus(OrderInvoice.OrderStatus.CANCELED);
    }

    public void setOrdersHistory(List<OrderInvoice> ordersHistory) {
        // make map from list
        this.idToOrderInvoice = ordersHistory.stream().collect(Collectors.toMap(OrderInvoice::getOrderId, o -> o));
    }

    public List<OrderInvoice> getOrdersHistory() throws MarketIsEmptyException {
        if (this.idToOrderInvoice.values().isEmpty()) {
            return new ArrayList<>();
        }
        else if(this.isEmpty()) {
            throw new MarketIsEmptyException();
        }
        return new ArrayList<>(this.idToOrderInvoice.values());
    }

    public boolean isEmpty() {
        return (idToProduct == null || idToProduct.isEmpty()) || (idToStore == null || idToStore.isEmpty()) ;
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(idToCustomer.values());
    }

    public Customer getCustomerById(int customerId) {
        return this.idToCustomer.get(customerId);
    }

    public void deleteProductForStore(int productId, int storeId) {
        // TODO:: validate all validations e.g not only selling store !
        Store sellingStore = this.idToStore.get(storeId);
        sellingStore.removeProduct(productId);
    }

    public void changePriceForProduct(int storeId, int productId, double newPrice) {
        Store sellingStore = this.idToStore.get(storeId);
        sellingStore.updateProductPrice(productId, newPrice);
    }

    public void addProductToStore(int storeId, int productId, int price) {
        this.idToStore.get(storeId).addProductToStock(productId, price);
    }
}
