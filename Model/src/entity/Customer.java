package entity;

import java.awt.*;

public class Customer {
    private int id;
    private String name;
    private Point location;
    private int totalOrders = 0;
    private double averageOrderCost = 0;
    private double averageShipmentCost = 0;

    public Customer(int id, String name, Point location) {  //, int totalOrders, double averageOrderCost, double averageShipmentCost) {
        this.id = id;
        this.name = name;
        this.location = location;
//        this.totalOrders = totalOrders;
//        this.averageOrderCost = averageOrderCost;
//        this.averageShipmentCost = averageShipmentCost;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Point getLocation() {
        return location;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public double getAverageOrderCost() {
        return averageOrderCost;
    }

    public double getAverageShipmentCost() {
        return averageShipmentCost;
    }

    public void addOrder(int orderCost, int shipmentCost) {
        this.averageOrderCost = calcAverageValue(this.averageOrderCost, this.totalOrders, orderCost);
        this.averageShipmentCost = calcAverageValue(this.averageShipmentCost, this.totalOrders, shipmentCost);
        this.totalOrders++;
    }

    private double calcAverageValue(double averageVariable, int prevTotalInstances, double newValue) {
        return ((averageVariable * prevTotalInstances) + newValue) / (prevTotalInstances + 1);
    }
}
