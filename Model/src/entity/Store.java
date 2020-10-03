package entity;


import entity.market.InvoiceDiscountProduct;
import entity.market.OrderInvoice;
import exception.ProductIdNotFoundException;
import javafx.util.Pair;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Store {
    private Point location;
    private Stock stock;

    public int getPpk() {
        return ppk;
    }
    private int ppk;
    private int id;
    private String name;
    private double totalShipmentIncome;
    private Map<Integer, OrderInvoice> ordersHistory;
    private Map<Integer, List<Discount>> productIdToDiscounts;

    public Store(Point point, Stock stock, int ppk, int id, String name, Map<Integer, List<Discount>> productIdToDiscounts) {
        this.location = point;
        this.stock = stock;
        this.ppk = ppk;
        this.id = id;
        this.name = name;
        this.totalShipmentIncome = 0;
        this.ordersHistory = new HashMap<>();
        this.productIdToDiscounts = productIdToDiscounts;
    }

    public void addToTotalShipmentIncome(double shipmentCost) {
        this.totalShipmentIncome += shipmentCost;
    }

    public void addOrder(OrderInvoice order) {
        this.ordersHistory.put(order.getOrderId(), order);
    }

    public void setOrderDiscountProducts(int orderInvoiceId, List<Discount.Offer> offerPurchases) {
        this.ordersHistory.get(orderInvoiceId).setDiscountProducts(
                offerPurchases.stream().map(offer -> new InvoiceDiscountProduct(
                        offer.getProductId(),
                        this.stock.getSoldProducts().get(offer.getProductId()).getName(),
                        offer.getForAdditional(),
                        offer.getQuantity(),
                        offer.getRelatedDiscountName()))
                .collect(Collectors.toList())
        );
    }

    public int getTotalProductSales(int productId) {
        // TODO: verify Exercise requirements does not contradict returning Discounts product count as well
        int totalSoldCount = (int) this.ordersHistory.values().stream()
                .map(OrderInvoice::getInvoiceProducts)
                .flatMap(List::stream)
                .filter(invoiceProduct -> invoiceProduct.getId() == productId)
                .count();
        totalSoldCount += (int) this.ordersHistory.values().stream()
                .map(OrderInvoice::getDiscountProducts)
                .flatMap(List::stream)
                .filter(discountProduct -> discountProduct.getProductId() == productId)
                .count();
        return totalSoldCount;
    }

    public double getPriceOfProduct(int productId) {
        if(!this.stock.doesProductIdExist(productId)) {
            throw new NoSuchElementException();
        }
        return this.stock.getProductPrice(productId);
    }

    public int getId() {
        return id;
    }

    public Stock getStock() {
        return stock;
    }

    // TODO: method not in use.
    public double getProductPriceByPPK(int productId, double distance) throws ProductIdNotFoundException {
        if (!stock.doesProductIdExist(productId)) {
            throw new ProductIdNotFoundException();
        }
        return stock.getProductPrice(productId) + (ppk * distance);
    }

    public double getShipmentCost(Point destinationForShipping) {
        return this.ppk * this.location.distance(destinationForShipping);
    }

    public Point getLocation() {
        return location;
    }

    public boolean isProductSold(int productId) {
        return this.getStock().doesProductIdExist(productId);
    }

    public double getProductPriceWithQuantity(Integer productId, double quantity) {
        return this.stock.getProductPrice(productId) * quantity;
    }

    public String getName() {
        return name;
    }

    public List<OrderInvoice> getOrdersHistory() {
        return new ArrayList<>(ordersHistory.values());
    }

    public double getTotalShipmentIncome() {
        return totalShipmentIncome;
    }

    // TODO: Check which Discount methods are not in use and remove
//    public List<Discount> getDiscountsPerPurchase(int productId, double quantity) {
//        List<Discount> result = new ArrayList<>();
//
//        return result;
//    }

    public List<Discount> getDiscountsByProductId(int productId) {
        return this.productIdToDiscounts.get(productId);
    }

    public Map<Integer,List<Discount>> getDiscountsMapByProductIdList(List<Integer> productIds) {
        List<Discount> result = new ArrayList<>();
        return productIds.stream()
                .filter(productId -> getDiscountsByProductId(productId)  != null)
//                .flatMap(productId -> this.productIdToDiscounts.get(productId).stream())
                .collect(Collectors.toMap(productId -> productId, this::getDiscountsByProductId));
    }

    public List<Discount> getMatchingDiscountsByProductIdQuantityPairs(List<Pair<Integer, Double>> productIdQuantityPairs) {
        Map<Integer,List<Discount>> discountsMap = getDiscountsMapByProductIdList(
                productIdQuantityPairs.stream()
                .map(Pair::getKey)
                .collect(Collectors.toList()));

        return matchDiscountsByProductIdQuantityPairs(discountsMap, productIdQuantityPairs);
    }

    private List<Discount> matchDiscountsByProductIdQuantityPairs(Map<Integer,List<Discount>> discountsMap, List<Pair<Integer, Double>> productIdQuantityPairs) {
        List<Discount> matchingDiscounts = new ArrayList<>();
        for (Pair<Integer,Double> pair : productIdQuantityPairs) {
            matchingDiscounts.addAll(
                    discountsMap.get(pair.getKey()).stream()
                    .filter(discount -> discount.isDiscountMatch(pair.getKey(), pair.getValue()))
                    .map(discount -> {
                        List<Discount> instancesOfDiscounts = new ArrayList<>();
                        for (int i = 0; i < discount.discountMatchInstances(pair.getKey(), pair.getValue()); i++) {
                            instancesOfDiscounts.add(discount);
                        }
                        return instancesOfDiscounts;
                    }).flatMap(Collection::stream)
                    .collect(Collectors.toList())
            );
        }

        return matchingDiscounts;
    }

    public void removeProduct(int productId) {
        this.stock.delete(productId);
    }

    public void updateProductPrice(int productId, double newPrice) {
        this.stock.getSoldProducts().get(productId).setPrice(newPrice);
    }
    // TODO: Verify that this flow does not require any validation (e.g. existing productID)
    public void addProductToStock(Product product, double price) {
        this.stock.addSoldProduct(new StoreProduct(product, price));
    }
}
