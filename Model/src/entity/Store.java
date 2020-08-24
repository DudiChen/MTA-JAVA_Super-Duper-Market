package entity;

import java.awt.*;

import exception.ProductIdNotFoundException;

public class Store {
    private Point coordinate;
    private Stock stock;
    private int ppk;
    //    private List<Discount> discounts;
    private int id;

    public Store(Point point, Stock stock, int ppk, int id) {
        this.coordinate = point;
        this.stock = stock;
        this.ppk = ppk;
        this.id = id;
    }

    public double getPriceOfProduct(int productId) {
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

    public double getProductPriceWithQuantity(Integer productId, Integer quantity) {
        return this.stock.getProductPrice(productId) * quantity;
    }
}
