package entity.market;

import entity.*;
import exception.DiscountsRemovedException;
import exception.MarketIsEmptyException;
import javafx.util.Pair;
import util.ErrorMessage;

import javax.xml.bind.ValidationException;
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

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(this.idToCustomer.values());
    }

    public Store getStoreById(int id) {
        return idToStore.get(id);
    }

    public Product getProductById(int id) {
        return this.idToProduct.get(id);
    }

    public Customer getCustomerById(int id) { return this.idToCustomer.get(id); }

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
        List<InvoiceDiscountProduct> discountProducts = order.getOffersTaken().stream()
                .map(discountOffer -> new InvoiceDiscountProduct(
                        discountOffer.getProductId(),
                        this.idToProduct.get(discountOffer.getProductId()).getName(),
                        discountOffer.getForAdditional(),
                        discountOffer.getQuantity(),
                        discountOffer.getRelatedDiscountName()))
                .collect(Collectors.toList());
        double totalPrice = invoiceProducts.stream()
                .map(InvoiceProduct::getTotalPrice)
                .reduce(0.0, Double::sum);
        totalPrice += discountProducts.stream()
                .map(InvoiceDiscountProduct::getAdditionalCost)
                .reduce(0.0, Double::sum);

                this.idToOrderInvoice.put(order.getId(),
                new OrderInvoice(
                        order.getId(),
                        order.getCustomerId(),
                        invoiceProducts,
                        discountProducts,
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
        this.getOrderInvoice(orderInvoiceId).setDiscountProducts(
                acceptedOffers.stream()
                .map(offer -> new InvoiceDiscountProduct(offer, this.idToProduct.get(offer.getProductId()).getName()))
                .collect(Collectors.toList())
        );
        this.idToStore.get(storeId).setOrderDiscountProducts(orderInvoiceId, acceptedOffers);
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

//    public void deleteProductForStore(int productId, int storeId, Optional<String> discountsRemovedMessage) throws ValidationException {
    public void deleteProductForStore(int productId, int storeId) throws ValidationException, DiscountsRemovedException {
//        validateProductDeletionFromStore(storeId, productId, discountsRemovedMessage);
        try {
            validateProductDeletionFromStore(storeId, productId);
        } catch (DiscountsRemovedException e) {
            throw e;
        }
        finally {
            Store sellingStore = this.getStoreById(storeId);
            sellingStore.removeProduct(productId);
        }
    }

//    private void validateProductDeletionFromStore(int storeId, int productId, Optional<String> discountsRemovedMessage) throws ValidationException {
    private void validateProductDeletionFromStore(int storeId, int productId) throws ValidationException, DiscountsRemovedException {
        ErrorMessage errorMessage = new ErrorMessage("");
        boolean isNotSoldInAtLeastOneOtherStore = !checkIfProductSoldInAtLeastOneOtherStore(errorMessage, productId, storeId);
        boolean isLastProductSoldByStore = checkIfProductTheLastInStore(errorMessage, storeId, productId);
        if (isNotSoldInAtLeastOneOtherStore || isLastProductSoldByStore) {
            throw new ValidationException(errorMessage.getMessage());
        }
        boolean isProductAssociatedDiscount = handleIfProductAssociatedDiscount(storeId, productId);
    }

//    private boolean handleIfProductAssociatedDiscount(Optional<String> discountsRemovedMessage, int storeId, int productId) {
    private boolean handleIfProductAssociatedDiscount(int storeId, int productId) throws DiscountsRemovedException {
        Store store = this.getStoreById(storeId);
        List<Discount> productAssociatedDiscounts = store.getDiscountsByProductId(productId);
        boolean isProductAssociatedWithDiscounts = productAssociatedDiscounts.size() > 0;
        if (isProductAssociatedWithDiscounts) {
            StringBuilder message = new StringBuilder();
            String productName = this.getProductById(productId).getName();
            List<String> discountsNames = productAssociatedDiscounts.stream()
                    .map(Discount::getName).collect(Collectors.toList());
            StringBuilder discountsNamesBuilder = new StringBuilder();
            for (int i = 0; i < discountsNames.size(); i++) {
                if (i > 0) discountsNamesBuilder.append(", ");
                discountsNamesBuilder.append("\"").append(discountsNames.get(i)).append("\"");
            }
            message
                    .append("Removed the following Discounts associated with Product \"")
                    .append(productName).append("\" (Product ID: ")
                    .append(productId).append("): ").append(
                    discountsNamesBuilder.toString()
            )
                    .append(System.lineSeparator());
//            discountsRemovedMessage = Optional.of(message.toString());

            productAssociatedDiscounts.stream()
                    .map(Discount::getProductId)
                    .forEach(store::removeProductDiscounts);

            throw new DiscountsRemovedException(message.toString());
        }
        return isProductAssociatedWithDiscounts;
    }

    private boolean checkIfProductTheLastInStore(ErrorMessage errorMessage, int storeId, int productId) {
        boolean isLastProductInStore = this.getStoreById(storeId).isLastProductSold(productId);
        if (isLastProductInStore) {
            StringBuilder message = new StringBuilder();
            String productName = this.getProductById(productId).getName();
            message
                    .append("Cannot perform delete to Product \"")
                    .append(productName).append("\" (Product ID: ")
                    .append(productId).append(") in store since it is the last product available.")
                    .append(System.lineSeparator());
            errorMessage.appendMessage(message.toString());
        }
        return isLastProductInStore;
    }

    private boolean checkIfProductSoldInAtLeastOneOtherStore(ErrorMessage errorMessage, int productId, int storeId) {
        int numberOfStoresSellingProduct = (int) this.idToStore.values().stream()
                .filter(store -> store.getId() != storeId)
                .filter(store -> store.isProductSold(productId))
                .count();
        boolean soldInAtLeastOneOtherStore = numberOfStoresSellingProduct > 0;
        if (!soldInAtLeastOneOtherStore) {
            StringBuilder message = new StringBuilder();
            String productName = this.getProductById(productId).getName();
            message
                    .append("Cannot perform delete to Product \"")
                    .append(productName).append("\" (Product ID: ")
                    .append(productId).append(") in store since it has to be sold in at least one store.")
                    .append(System.lineSeparator());
            errorMessage.appendMessage(message.toString());
        }
        return (numberOfStoresSellingProduct > 0);
    }

    public void changePriceForProduct(int storeId, int productId, double newPrice) {
        Store sellingStore = this.idToStore.get(storeId);
        sellingStore.updateProductPrice(productId, newPrice);
    }

    public void addProductToStore(int storeId, int productId, double price) {
        this.idToStore.get(storeId).addProductToStock(this.getProductById(productId), price);
    }

    public boolean isAvailableDiscount(Discount discount, List<Pair<Integer, Double>> orderProducts, int timesUsedDiscount) {
        int discountProductId = discount.getProductId();
        double discountProductQuantity = discount.getQuantity();
        double quantityOrderedOfProduct = orderProducts.stream()
                .filter(productIdToQuantityPair -> productIdToQuantityPair.getKey() == discountProductId)
                .map(Pair::getValue).reduce(0.0, Double::sum);
        int timesEligibleForDiscount = (int) ((quantityOrderedOfProduct - (discountProductQuantity * timesUsedDiscount)) / discountProductQuantity);
        return timesEligibleForDiscount > 0;
    }
}
