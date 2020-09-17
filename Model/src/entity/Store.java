package entity;


import entity.market.OrderInvoice;
import exception.ProductIdNotFoundException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Store {
    private Point coordinate;
    private Stock stock;

    public int getPpk() {
        return ppk;
    }

    private int ppk;
    //    private List<Discount> discounts;
    private int id;
    private String name;
    private double totalShipmentIncome;
    private List<OrderInvoice> ordersHistory;

    public Store(Point point, Stock stock, int ppk, int id, String name) {
        this.coordinate = point;
        this.stock = stock;
        this.ppk = ppk;
        this.id = id;
        this.name = name;
        this.totalShipmentIncome = 0;
        this.ordersHistory = new ArrayList<>();
    }

    public void addToTotalShipmentIncome(double shipmentCost) {
        this.totalShipmentIncome += shipmentCost;
    }

    public void addOrder(OrderInvoice order) {
        this.ordersHistory.add(order);
    }

    public int getTotalProductSales(int productId) {
        return (int)
                this.ordersHistory.stream()
                        .map(orderInvoice -> orderInvoice.getInvoiceProducts())
                        .flatMap(List::stream)
                        .filter(invoiceProduct -> invoiceProduct.getId() == productId)
                        .count();
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

    public double getProductPriceByPPK(int productId, double distance) throws ProductIdNotFoundException {
        if (!stock.doesProductIdExist(productId)) {
            throw new ProductIdNotFoundException();
        }
        return stock.getProductPrice(productId) + (ppk * distance);
    }

    public double getShipmentCost(Point destinationForShipping) {
        return this.ppk * this.coordinate.distance(destinationForShipping);
    }

    public Point getCoordinate() {
        return coordinate;
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
        return ordersHistory;
    }

    public double getTotalShipmentIncome() {
        return totalShipmentIncome;
    }
}
