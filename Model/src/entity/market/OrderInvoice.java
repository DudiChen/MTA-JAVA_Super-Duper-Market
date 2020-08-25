package entity.market;

import entity.Order;
import entity.StoreProduct;

import java.util.Date;
import java.util.List;

public class OrderInvoice {

    private OrderStatus orderStatus = OrderStatus.ISSUED;
    private int orderId;
    private List<InvoiceProduct> invoiceProduct;
    private double totalPrice;
    private Date deliveryDate;
    private int storeId;
    private double shipmentPrice;

    public double getShipmentPrice() {
        return shipmentPrice;
    }

    public OrderInvoice(int id, List<InvoiceProduct> invoiceProducts, double totalPrice, Date deliveryDate, int storeId, double shipmentPrice) {
        this.orderId = id;
        this.invoiceProduct = invoiceProducts;
        this.totalPrice = totalPrice;
        this.deliveryDate = deliveryDate;
        this.storeId = storeId;
        this.shipmentPrice = shipmentPrice;
    }

    public void setStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public List<InvoiceProduct> getInvoiceProducts() {
        return invoiceProduct;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getStoreId() {
        return storeId;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }


    enum OrderStatus {
        ACCEPTED, CANCELED, ISSUED
    }
}
